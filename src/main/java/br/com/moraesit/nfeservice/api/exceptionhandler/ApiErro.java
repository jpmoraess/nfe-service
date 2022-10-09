package br.com.moraesit.nfeservice.api.exceptionhandler;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErro {
    private int status;
    private String mensagem;
}
