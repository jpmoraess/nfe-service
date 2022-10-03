package br.com.moraesit.nfeservice.data.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "certificado")
    private Empresa empresa;

    private String nome;

    private String senha;

    private LocalDate validade;

    private String contentType;

    private Long size;
}
