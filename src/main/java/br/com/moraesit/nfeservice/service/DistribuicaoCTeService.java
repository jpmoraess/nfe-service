package br.com.moraesit.nfeservice.service;

import br.com.moraesit.nfeservice.data.entities.ConhecimentoTransporte;
import br.com.moraesit.nfeservice.data.entities.Empresa;
import br.com.moraesit.nfeservice.exception.BusinessException;
import br.com.moraesit.nfeservice.utils.ArquivoUtil;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.cte.Cte;
import br.com.swconsultoria.cte.dom.ConfiguracoesCte;
import br.com.swconsultoria.cte.dom.enuns.*;
import br.com.swconsultoria.cte.exception.CteException;
import br.com.swconsultoria.cte.schema_300.procCTe.TCTe;
import br.com.swconsultoria.cte.schema_300.retdistdfeint.RetDistDFeInt;
import br.com.swconsultoria.cte.util.XmlCteUtil;
import br.com.swconsultoria.nfe.util.ObjetoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DistribuicaoCTeService {

    private final static String SCHEMAS = "/schemas/cte";

    private final EmpresaService empresaService;
    private final CertificadoService certificadoService;
    private final ConhecimentoTransporteService conhecimentoTransporteService;

    public DistribuicaoCTeService(EmpresaService empresaService,
                                  CertificadoService certificadoService,
                                  ConhecimentoTransporteService conhecimentoTransporteService) {
        this.empresaService = empresaService;
        this.certificadoService = certificadoService;
        this.conhecimentoTransporteService = conhecimentoTransporteService;
    }

    public void consultar() throws CteException, JAXBException, CertificadoException, IOException {
        for (Empresa empresa : empresaService.listarTodas()) {
            consultar(empresa);
        }
    }

    private void consultar(Empresa empresa) throws CertificadoException, CteException, IOException, JAXBException {
        ConfiguracoesCte configuracao = criarConfiguracao(empresa);

        List<ConhecimentoTransporte> listaConhecimentosSalvar = new ArrayList<>();

        boolean continuarConsultando = true;

        while (continuarConsultando) {

            RetDistDFeInt retorno = Cte.distribuicaoDfe(
                    configuracao,
                    PessoaEnum.JURIDICA,
                    empresa.getCnpj(),
                    ConsultaDFeEnum.NSU,
                    ObjetoUtil.verifica(empresa.getNsuCte()).orElse("000000000000000"));

            if (!retorno.getCStat().equals(StatusCteEnum.DOC_LOCALIZADO_PARA_DESTINATARIO.getCodigo())) {
                if (retorno.getCStat().equals(StatusCteEnum.CONSUMO_INDEVIDO.getCodigo())) {
                    break;
                } else {
                    throw new BusinessException("Erro ao realizar a consulta de cte: " + retorno.getCStat() + " - " + retorno.getXMotivo());
                }
            }

            popularLista(empresa, listaConhecimentosSalvar, retorno);

            continuarConsultando = !retorno.getUltNSU().equals(retorno.getMaxNSU());

            empresa.setNsuCte(retorno.getUltNSU());
        }

        empresaService.atualizar(empresa);

        conhecimentoTransporteService.salvar(listaConhecimentosSalvar);
    }

    private void popularLista(Empresa empresa, List<ConhecimentoTransporte> listaConhecimentosSalvar,
                              RetDistDFeInt retorno) throws IOException, JAXBException {
        for (RetDistDFeInt.LoteDistDFeInt.DocZip doc : retorno.getLoteDistDFeInt().getDocZip()) {
            final var xml = XmlCteUtil.gZipToXml(doc.getValue());
            log.info("XML: " + xml);
            log.info("SCHEMA: " + doc.getSchema());
            log.info("NSU: " + doc.getNSU());

            switch (doc.getSchema()) {
                case "procCTe_v3.00.xsd":
                    TCTe cte = XmlCteUtil.xmlToObject(xml, TCTe.class);
                    final var conhecimentoTransporte = ConhecimentoTransporte.builder()
                            .chave(cte.getInfCte().getId().substring(3).trim())
                            .numero(cte.getInfCte().getIde().getNCT())
                            .serie(cte.getInfCte().getIde().getSerie())
                            .empresa(empresa)
                            .cnpjEmitente(cte.getInfCte().getEmit().getCNPJ())
                            .nomeEmitente(cte.getInfCte().getEmit().getXNome())
                            .valor(new BigDecimal(cte.getInfCte().getVPrest().getVTPrest()))
                            .xml(ArquivoUtil.compactaXml(xml))
                            .build();
                    listaConhecimentosSalvar.add(conhecimentoTransporte);
                    break;
                case "procEventoCTe_v3.00.xsd":
                    //TODO: implementar
                default:
                    break;
            }
        }
    }

    private ConfiguracoesCte criarConfiguracao(Empresa empresa) throws CertificadoException {
        final var certificado = certificadoService.a1(empresa);
        return ConfiguracoesCte.criarConfiguracoes(EstadosEnum.SP, AmbienteEnum.PRODUCAO, certificado, SCHEMAS);
    }
}
