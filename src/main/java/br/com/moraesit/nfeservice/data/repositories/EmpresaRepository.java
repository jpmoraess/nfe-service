package br.com.moraesit.nfeservice.data.repositories;

import br.com.moraesit.nfeservice.data.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);
}
