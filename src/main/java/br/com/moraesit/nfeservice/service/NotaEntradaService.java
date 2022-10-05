package br.com.moraesit.nfeservice.service;

import br.com.moraesit.nfeservice.data.entities.NotaEntrada;
import br.com.moraesit.nfeservice.data.repositories.NotaEntradaRepository;
import br.com.moraesit.nfeservice.data.specs.NotaEntradaSpecs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
public class NotaEntradaService {
    private final NotaEntradaRepository notaEntradaRepository;

    public NotaEntradaService(NotaEntradaRepository notaEntradaRepository) {
        this.notaEntradaRepository = notaEntradaRepository;
    }

    public NotaEntrada buscar(Long id) {
        return notaEntradaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota não encontrada."));
    }

    public Page<NotaEntrada> pesquisar(final Long empresaId, final NotaEntrada notaEntrada, final Pageable pageable) {
        System.out.println("filtro: " + notaEntrada.toString());

        Specification<NotaEntrada> specification = NotaEntradaSpecs.filtrar(empresaId, notaEntrada);

        return notaEntradaRepository.findAll(specification, pageable);
    }

    @Transactional
    public void salvar(List<NotaEntrada> notas) {
        notaEntradaRepository.saveAll(notas);
    }
}
