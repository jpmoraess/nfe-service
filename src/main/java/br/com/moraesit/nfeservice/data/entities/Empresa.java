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

    @Column(name = "nsu_nfe")
    private String nsuNfe;

    @Column(name = "nsu_cte")
    private String nsuCte;

    private boolean favorita;

    private boolean ativa;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "certificado_id", referencedColumnName = "id")
    private Certificado certificado;
}
