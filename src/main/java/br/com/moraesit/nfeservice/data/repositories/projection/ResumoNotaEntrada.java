package br.com.moraesit.nfeservice.data.repositories.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumoNotaEntrada {
    private Long id;
    private LocalDateTime emissao;
    private String chave;
    private String numero;
    private String serie;
    private String nomeEmitente;
    private String cnpjEmitente;
    private BigDecimal valor;
}
