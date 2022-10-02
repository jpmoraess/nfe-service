package br.com.moraesit.nfeservice.api.models.certificado;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UploadCertificadoRequest {

    @NotNull
    private MultipartFile file;

    @NotBlank
    private String password;

    @NotNull
    private Long certificadoId;
}
