package br.com.moraesit.nfeservice.api.models.empresa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarEmpresaResponse {
    private Long id;
    private String nome;
    private String cnpj;
}
