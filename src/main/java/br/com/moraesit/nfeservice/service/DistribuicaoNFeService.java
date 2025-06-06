package br.com.moraesit.nfeservice.service;

import br.com.moraesit.nfeservice.data.entities.Empresa;
import br.com.moraesit.nfeservice.data.entities.NotaEntrada;
import br.com.moraesit.nfeservice.utils.ArquivoUtil;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.Evento;
import br.com.swconsultoria.nfe.dom.enuns.*;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema.envConfRecebto.TEnvEvento;
import br.com.swconsultoria.nfe.schema.envConfRecebto.TRetEnvEvento;
import br.com.swconsultoria.nfe.schema.resnfe.ResNFe;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNfeProc;
import br.com.swconsultoria.nfe.util.ManifestacaoUtil;
import br.com.swconsultoria.nfe.util.ObjetoUtil;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DistribuicaoNFeService {

    private final static String SCHEMAS = "/schemas/nfe";
    private final EmpresaService empresaService;
    private final CertificadoService certificadoService;
    private final NotaEntradaService notaEntradaService;

    public DistribuicaoNFeService(EmpresaService empresaService,
                                  CertificadoService certificadoService,
                                  NotaEntradaService notaEntradaService) {
        this.empresaService = empresaService;
        this.certificadoService = certificadoService;
        this.notaEntradaService = notaEntradaService;
    }

    public void consultar() throws JAXBException, NfeException, CertificadoException, IOException {
        for (Empresa empresa : empresaService.listarTodas()) {
            consultar(empresa);
        }
    }

    private void consultar(Empresa empresa) throws NfeException, CertificadoException, IOException, JAXBException {
        ConfiguracoesNfe configuracao = criarConfiguracao(empresa);

        List<String> listaNotasManifestar = new ArrayList<>();

        List<NotaEntrada> listaNotasSalvar = new ArrayList<>();

        boolean continuarConsultando = true;

        while (continuarConsultando) {

            RetDistDFeInt retorno = Nfe.distribuicaoDfe(
                    configuracao,
                    PessoaEnum.JURIDICA,
                    empresa.getCnpj(),
                    ConsultaDFeEnum.NSU,
                    ObjetoUtil.verifica(empresa.getNsuNfe()).orElse("000000000000000"));

            if (!retorno.getCStat().equals(StatusEnum.DOC_LOCALIZADO_PARA_DESTINATARIO.getCodigo())) {
                // parar o processamento caso o erro for de consumo indevido
                if (retorno.getCStat().equals(StatusEnum.CONSUMO_INDEVIDO.getCodigo())) {
                    break;
                } else {
                    // se o erro for nenhum documento localizado, continuar o processo para os demais
                    if (retorno.getCStat().equals(StatusEnum.NENHUM_DOC_LOCALIZADO_PARA_DESTINATARIO.getCodigo())) {
                        continue;
                    }
                    log.error("[ERRO AO CONSULTAR NOTAS FISCAIS]: STATUS: {}, MOTIVO: {}", retorno.getCStat(), retorno.getXMotivo());
                }
            }

            popularLista(empresa, listaNotasManifestar, listaNotasSalvar, retorno);

            continuarConsultando = !retorno.getUltNSU().equals(retorno.getMaxNSU());

            empresa.setNsuNfe(retorno.getUltNSU());
        }

        empresaService.atualizar(empresa);

        notaEntradaService.salvar(listaNotasSalvar);

        manifestarNotas(empresa, listaNotasManifestar, configuracao);
    }

    /**
     * Popular as listas de notas à salvar e notas à manifestar
     *
     * @param empresa              - empresa
     * @param listaNotasManifestar - listaNotasManifestar
     * @param listaNotasSalvar     - listaNotasSalvar
     * @param retorno              - retorno
     * @throws IOException
     * @throws JAXBException
     */
    private void popularLista(Empresa empresa, List<String> listaNotasManifestar,
                              List<NotaEntrada> listaNotasSalvar, RetDistDFeInt retorno)
            throws IOException, JAXBException {
        for (RetDistDFeInt.LoteDistDFeInt.DocZip doc : retorno.getLoteDistDFeInt().getDocZip()) {
            final var xml = XmlNfeUtil.gZipToXml(doc.getValue());
            log.info("XML: " + xml);
            log.info("SCHEMA: " + doc.getSchema());
            log.info("NSU: " + doc.getNSU());

            switch (doc.getSchema()) {
                case "resNFe_v1.01.xsd":
                    ResNFe resNFe = XmlNfeUtil.xmlToObject(xml, ResNFe.class);
                    final var chave = resNFe.getChNFe();
                    listaNotasManifestar.add(chave);
                    break;
                case "procNFe_v4.00.xsd":
                    TNfeProc nfe = XmlNfeUtil.xmlToObject(xml, TNfeProc.class);
                    final var notaEntrada = NotaEntrada.builder()
                            .chave(nfe.getNFe().getInfNFe().getId().substring(3).trim())
                            .numero(nfe.getNFe().getInfNFe().getIde().getNNF())
                            .serie(nfe.getNFe().getInfNFe().getIde().getSerie())
                            // formato vindo na nfe: 2022-09-06T10:05:44-03:00
                            .emissao(formatarDataHora(nfe.getNFe().getInfNFe().getIde().getDhEmi()))
                            .empresa(empresa)
                            .cnpjEmitente(nfe.getNFe().getInfNFe().getEmit().getCNPJ())
                            .nomeEmitente(nfe.getNFe().getInfNFe().getEmit().getXNome())
                            .schema(doc.getSchema())
                            .valor(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF()))
                            .xml(ArquivoUtil.compactaXml(xml))
                            .build();
                    listaNotasSalvar.add(notaEntrada);
                    break;
                default:
                    break;
            }
        }
    }

    private void manifestarNotas(Empresa empresa, List<String> chaves, ConfiguracoesNfe configuracao) throws NfeException {
        for (String chave : chaves) {
            Evento manifestacao = new Evento();
            manifestacao.setChave(chave);
            manifestacao.setCnpj(empresa.getCnpj());
            manifestacao.setMotivo("Manifestação notas resumidas");
            manifestacao.setDataEvento(LocalDateTime.now());
            manifestacao.setTipoManifestacao(ManifestacaoEnum.CIENCIA_DA_OPERACAO);

            // monta o evento de manifestação
            TEnvEvento tEnvEvento = ManifestacaoUtil.montaManifestacao(manifestacao, configuracao);

            // envia o evento de manifestação
            TRetEnvEvento retorno = Nfe.manifestacao(configuracao, tEnvEvento, false);

            if (!StatusEnum.LOTE_EVENTO_PROCESSADO.getCodigo().equals(retorno.getRetEvento().get(0).getInfEvento().getCStat())) {
                log.error("Erro ao manifestar chave: " + chave + ", status: " + retorno.getCStat() + ", motivo: " + retorno.getXMotivo());
            }
        }
    }

    private ConfiguracoesNfe criarConfiguracao(Empresa empresa) throws CertificadoException {
        final var certificado = certificadoService.a1(empresa);
        return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.SP, AmbienteEnum.PRODUCAO, certificado, SCHEMAS);
    }


    private LocalDateTime formatarDataHora(String str) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(str, dateTimeFormatter);
    }
}
