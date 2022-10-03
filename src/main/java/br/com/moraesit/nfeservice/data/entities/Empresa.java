package br.com.moraesit.nfeservice.data.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cnpj;

    private String nome;

    private String nsu;

    private boolean favorita;

    private boolean ativa;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "certificado_id", referencedColumnName = "id")
    private Certificado certificado;
}
