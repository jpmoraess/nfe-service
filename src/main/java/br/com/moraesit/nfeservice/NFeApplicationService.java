package br.com.moraesit.nfeservice;

import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.*;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema.cce.TProcEvento;
import br.com.swconsultoria.nfe.schema.resnfe.ResNFe;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import br.com.swconsultoria.nfe.schema_4.retConsReciNFe.TNfeProc;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Slf4j
@Service
public class NFeApplicationService {

    private final CertificadoApplicationService certificadoApplicationService;

    public NFeApplicationService(CertificadoApplicationService certificadoApplicationService) {
        this.certificadoApplicationService = certificadoApplicationService;
    }

    public void downloadPorChave(final String cnpj, final String chave, final ConfiguracoesNfe configuracoes) {
        RetDistDFeInt retorno;

        try {
            retorno = Nfe.distribuicaoDfe(configuracoes, PessoaEnum.JURIDICA, cnpj, ConsultaDFeEnum.CHAVE, chave);
        } catch (NfeException e) {
            throw new RuntimeException(e);
        }

        if (retorno.getCStat().equals(StatusEnum.DOC_LOCALIZADO_PARA_DESTINATARIO.getCodigo())) {
            processaRetorno(retorno);
        } else {
            log.warn("[STATUS]: {}, [MOTIVO]: {}", retorno.getCStat(), retorno.getXMotivo());
        }
    }

    public void downloadPorNSU(final String cnpj, final String nsu, final ConfiguracoesNfe configuracoes) {
        RetDistDFeInt retorno;

        try {
            retorno = Nfe.distribuicaoDfe(configuracoes, PessoaEnum.JURIDICA, cnpj, ConsultaDFeEnum.NSU_UNICO, nsu);
        } catch (NfeException e) {
            throw new RuntimeException(e);
        }

        if (retorno.getCStat().equals(StatusEnum.DOC_LOCALIZADO_PARA_DESTINATARIO.getCodigo())) {
            processaRetorno(retorno);
        } else {
            log.warn("[STATUS]: {}, [MOTIVO]: {}", retorno.getCStat(), retorno.getXMotivo());
        }
    }

    private void processaRetorno(RetDistDFeInt retDistDFeInt) {
        final var docZipList = retDistDFeInt.getLoteDistDFeInt().getDocZip();

        docZipList.forEach(doc -> {
            switch (doc.getSchema()) {
                case "resNFe_v1.01.xsd": {
                    try {
                        final var xml = XmlNfeUtil.gZipToXml(doc.getValue());
                        final var resNFe = XmlNfeUtil.xmlToObject(xml, ResNFe.class);
                        log.error("[resNFe: ] {}", resNFe.getChNFe());
                    } catch (IOException | JAXBException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "procNFe_v4.00.xsd": {
                    try {
                        final var xml = XmlNfeUtil.gZipToXml(doc.getValue());
                        final var tNfeProc = XmlNfeUtil.xmlToObject(xml, TNfeProc.class);
                        log.error("[tNfeProc: ] {}", tNfeProc.toString());
                    } catch (IOException | JAXBException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "procEventoNFe_v1.00.xsd": {
                    try {
                        final var xml = XmlNfeUtil.gZipToXml(doc.getValue());
                        final var procEvento = XmlNfeUtil.xmlToObject(xml, TProcEvento.class);
                        log.error("[procEvento: ] {}", procEvento.toString());
                    } catch (IOException | JAXBException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                default: break;
            }
        });
    }

    public ConfiguracoesNfe configuracoesNfe(final String path, final String senha) {
        final Certificado certificado = certificadoApplicationService.certificadoA1(path, senha);

        final String pastaSchemas = "/schemas";

        try {
            return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.SP, AmbienteEnum.PRODUCAO, certificado, pastaSchemas);
        } catch (CertificadoException e) {
            throw new RuntimeException(e);
        }
    }
}
