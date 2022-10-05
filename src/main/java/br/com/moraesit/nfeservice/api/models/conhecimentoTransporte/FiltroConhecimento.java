package br.com.moraesit.nfeservice.api.models.conhecimentoTransporte;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroConhecimento {
    private Long id;
    private String chave;
    private String numero;
    private String serie;
    private String nomeEmitente;
    private String cnpjEmitente;
    private BigDecimal valor;
}
