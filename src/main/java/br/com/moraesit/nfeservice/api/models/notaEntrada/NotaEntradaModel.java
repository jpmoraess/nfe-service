package br.com.moraesit.nfeservice.api.models.notaEntrada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotaEntradaModel {
    private Long id;
    private String chave;
    private String numero;
    private String serie;
    private String nomeEmitente;
    private String cnpjEmitente;
    private BigDecimal valor;
}
