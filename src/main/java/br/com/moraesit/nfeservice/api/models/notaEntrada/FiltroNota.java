package br.com.moraesit.nfeservice.api.models.notaEntrada;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroNota {
    private Long id;
    private String chave;
    private String numero;
    private String serie;
    private String nomeEmitente;
    private String cnpjEmitente;
    private BigDecimal valor;
}
