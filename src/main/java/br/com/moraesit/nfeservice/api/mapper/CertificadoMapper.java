package br.com.moraesit.nfeservice.api.mapper;

import br.com.moraesit.nfeservice.api.models.certificado.CertificadoResponse;
import br.com.moraesit.nfeservice.api.models.certificado.UploadCertificadoRequest;
import br.com.moraesit.nfeservice.data.entities.Certificado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class CertificadoMapper {

    public CertificadoResponse certificadoToCertificadoResponse(Certificado certificado) {
        return CertificadoResponse.builder()
                .id(certificado.getId())
                .name(certificado.getName())
                .expirationDate(certificado.getExpirationDate())
                .contentType(certificado.getContentType())
                .size(certificado.getSize())
                .build();
    }

    public Certificado uploadRequestToCertificado(UploadCertificadoRequest request) {
        return Certificado.builder()
                .id(request.getCertificadoId())
                .name(request.getFile().getOriginalFilename())
                .password(request.getPassword())
                .contentType(request.getFile().getContentType())
                .size(request.getFile().getSize())
                .expirationDate(LocalDate.now())
                .build();
    }
}
