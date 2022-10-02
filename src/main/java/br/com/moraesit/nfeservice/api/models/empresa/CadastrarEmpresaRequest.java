package br.com.moraesit.nfeservice.api.models.empresa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@Getter
public class CadastrarEmpresaRequest {
    @NotEmpty(message = "Favor informar um CNPJ.")
    @CNPJ(message = "Informar um CNPJ válido.")
    private String cnpj;
    @NotEmpty(message = "Favor informar um nome.")
    private String nome;
}
