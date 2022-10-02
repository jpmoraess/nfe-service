package br.com.moraesit.nfeservice.api.models.certificado;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CertificadoResponse {
    private Long id;
    private String name;
    private LocalDate expirationDate;
    private String contentType;
    private Long size;
}
