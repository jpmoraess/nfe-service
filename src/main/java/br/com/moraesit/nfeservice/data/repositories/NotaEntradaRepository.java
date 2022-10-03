package br.com.moraesit.nfeservice.data.repositories;

import br.com.moraesit.nfeservice.data.entities.NotaEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaEntradaRepository extends JpaRepository<NotaEntrada, Long>, JpaSpecificationExecutor<NotaEntrada> {
}
