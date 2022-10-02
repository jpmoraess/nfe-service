package br.com.moraesit.nfeservice.api.controllers;

import br.com.moraesit.nfeservice.api.mapper.CertificadoMapper;
import br.com.moraesit.nfeservice.api.models.certificado.CertificadoResponse;
import br.com.moraesit.nfeservice.api.models.certificado.UploadCertificadoRequest;
import br.com.moraesit.nfeservice.service.CertificadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/certificados")
public class CertificadoController {
    private final CertificadoMapper certificadoMapper;
    private final CertificadoService certificadoService;

    public CertificadoController(CertificadoMapper certificadoMapper, CertificadoService certificadoService) {
        this.certificadoMapper = certificadoMapper;
        this.certificadoService = certificadoService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CertificadoResponse findById(@PathVariable final Long id) {
        final var certificado = certificadoService.findById(id);
        return certificadoMapper.certificadoToCertificadoResponse(certificado);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CertificadoResponse upload(@Valid final UploadCertificadoRequest request) throws IOException {
        final var certificado = certificadoService
                .save(certificadoMapper.uploadRequestToCertificado(request), request.getFile().getInputStream());
        return certificadoMapper.certificadoToCertificadoResponse(certificado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        certificadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
