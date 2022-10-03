package br.com.moraesit.nfeservice.api.models.empresa;

import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastroEmpresaRequest {
    @NotBlank(message = "Informe um CNPJ.")
    @CNPJ(message = "Informe um CNPJ válido.")
    private String cnpj;
    @NotBlank(message = "Informe o nome.")
    private String nome;
    @NotNull(message = "Certificado é obrigatório")
    private MultipartFile certificado;
    @NotBlank(message = "Senha do certificado é obrigatória")
    private String senhaCertificado;
}