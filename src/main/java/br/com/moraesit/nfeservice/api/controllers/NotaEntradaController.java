package br.com.moraesit.nfeservice.api.controllers;


import br.com.moraesit.nfeservice.api.mapper.NotaMapper;
import br.com.moraesit.nfeservice.api.models.notaEntrada.FiltroNota;
import br.com.moraesit.nfeservice.api.models.notaEntrada.NotaEntradaModel;
import br.com.moraesit.nfeservice.data.repositories.projection.ResumoNotaEntrada;
import br.com.moraesit.nfeservice.service.DistribuicaoNFeService;
import br.com.moraesit.nfeservice.service.NotaEntradaService;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.exception.NfeException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/nota-entrada")
@CrossOrigin(origins = "*")
public class NotaEntradaController {
    private final NotaMapper notaMapper;
    private final NotaEntradaService notaEntradaService;
    private final DistribuicaoNFeService distribuicaoNFeService;

    public NotaEntradaController(NotaMapper notaMapper,
                                 NotaEntradaService notaEntradaService,
                                 DistribuicaoNFeService distribuicaoNFeService) {
        this.notaMapper = notaMapper;
        this.notaEntradaService = notaEntradaService;
        this.distribuicaoNFeService = distribuicaoNFeService;
    }

    @GetMapping
    public Page<ResumoNotaEntrada> resumir(
            @RequestParam("empresaId") final Long empresaId,
            FiltroNota filtroNota,
            Pageable pageable,
            @RequestParam(defaultValue = "emissao, desc") final String[] sort) {
        filtroNota.setEmpresaId(empresaId);

        return notaEntradaService.resumir(filtroNota, sort, pageable);
    }

    @GetMapping("/{id}")
    public NotaEntradaModel buscar(@PathVariable Long id) {
        return notaMapper.entityToModel(notaEntradaService.buscar(id));
    }

    @GetMapping("/consultar")
    public void consultar() throws JAXBException, NfeException, CertificadoException, IOException {
        distribuicaoNFeService.consultar();
    }

    @GetMapping("/{id}/danfe")
    public ResponseEntity<byte[]> danfe(@PathVariable Long id) throws JRException, IOException, ParserConfigurationException, SAXException {
        final var danfe = notaEntradaService.bytesDanfe(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(danfe);
    }

    @GetMapping("/{id}/danfe-html")
    public ResponseEntity<?> danfeHtml(@PathVariable Long id) throws JRException, IOException, ParserConfigurationException, SAXException {
        notaEntradaService.htmlDanfe(id);
        return ResponseEntity.ok().build();
    }
}
