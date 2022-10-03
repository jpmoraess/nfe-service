package br.com.moraesit.nfeservice.data.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotaEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chave;
    private String numero;
    private String serie;
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    private String cnpjEmitente;
    private String nomeEmitente;
    private String schema;
    private BigDecimal valor;
    @Lob
    private byte[] xml;
}
