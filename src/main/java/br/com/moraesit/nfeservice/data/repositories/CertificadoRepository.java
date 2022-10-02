package br.com.moraesit.nfeservice.data.repositories;

import br.com.moraesit.nfeservice.data.entities.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
}
