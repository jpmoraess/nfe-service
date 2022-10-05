package br.com.moraesit.nfeservice.service.impressao;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

@Slf4j
@Service
public class ImpressaoService {

    /**
     * Cria a impressão no caminho definido e com o formato PDF
     *
     * @param impressao
     * @param destino
     * @throws JRException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void impressaoPdfArquivo(Impressao impressao, String destino) throws JRException, ParserConfigurationException, IOException, SAXException {
        JasperPrint jasperPrint = geraImpressao(impressao);
        JasperExportManager.exportReportToPdfFile(jasperPrint, destino);
    }

    /**
     * Cria a impressão retornando o byte[]
     *
     * @param impressao
     * @return
     * @throws JRException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public byte[] impressaoPdfByte(Impressao impressao) throws JRException, ParserConfigurationException, IOException, SAXException {
        JasperPrint jasperPrint = geraImpressao(impressao);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    /**
     * Cria a impressão no caminho definido e com o formato HTML
     *
     * @param impressao
     * @param destino
     * @throws JRException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void impressaoHtml(Impressao impressao, String destino) throws JRException, ParserConfigurationException, IOException, SAXException {
        JasperPrint jasperPrint = geraImpressao(impressao);
        JasperExportManager.exportReportToHtmlFile(jasperPrint, destino);
    }

    /**
     * Cria a impressão em um preview, use setVisible(true) para mostrar a janela.
     *
     * @param impressao
     * @return
     * @throws JRException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public JasperViewer impressaoPreview(Impressao impressao) throws JRException, ParserConfigurationException, IOException, SAXException {
        JasperPrint jasperPrint = geraImpressao(impressao);
        return new JasperViewer(jasperPrint, true);
    }

    private JasperPrint geraImpressao(Impressao impressao) throws ParserConfigurationException, IOException, SAXException, JRException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(impressao.getXml())));
        JRDataSource dataSource = new JRXmlDataSource(document, impressao.getPathExpression());
        return JasperFillManager.fillReport(impressao.getJasper(), impressao.getParametros(), dataSource);
    }
}
