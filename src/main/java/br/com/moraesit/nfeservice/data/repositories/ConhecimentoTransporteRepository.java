package br.com.moraesit.nfeservice.data.repositories;

import br.com.moraesit.nfeservice.data.entities.ConhecimentoTransporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConhecimentoTransporteRepository extends JpaRepository<ConhecimentoTransporte, Long>,
        JpaSpecificationExecutor<ConhecimentoTransporte> {
}
