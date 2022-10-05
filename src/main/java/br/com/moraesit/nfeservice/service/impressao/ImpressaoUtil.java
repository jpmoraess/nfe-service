package br.com.moraesit.nfeservice.service.impressao;

import java.util.Objects;

public class ImpressaoUtil {
    public static Impressao impressaoPadraoNFe(String xml) {
        Impressao impressaoNFe = new Impressao();
        impressaoNFe.setXml(xml);
        impressaoNFe.setPathExpression("/nfeProc/NFe/infNFe/det");
        impressaoNFe.setJasper(ImpressaoUtil.class.getResourceAsStream("/jasper/nfe/danfe.jasper"));
        impressaoNFe.getParametros().put("Logo", Objects.requireNonNull(ImpressaoUtil.class.getResourceAsStream("/img/nfe.jpg")));
        impressaoNFe.getParametros().put("SUBREPORT", Objects.requireNonNull(ImpressaoUtil.class.getResourceAsStream("/jasper/nfe/danfe_fatura.jasper")));
        return impressaoNFe;
    }
}
