package br.com.moraesit.nfeservice.service;

import br.com.moraesit.nfeservice.data.entities.ConhecimentoTransporte;
import br.com.moraesit.nfeservice.data.repositories.ConhecimentoTransporteRepository;
import br.com.moraesit.nfeservice.data.specs.ConhecimentoTransporteSpecs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
public class ConhecimentoTransporteService {

    private final ConhecimentoTransporteRepository conhecimentoTransporteRepository;

    public ConhecimentoTransporteService(ConhecimentoTransporteRepository conhecimentoTransporteRepository) {
        this.conhecimentoTransporteRepository = conhecimentoTransporteRepository;
    }

    public ConhecimentoTransporte buscar(Long id) {
        return conhecimentoTransporteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conhecimento de transporte não encontrado."));
    }

    public Page<ConhecimentoTransporte> pesquisar(final Long empresaId,
                                                  final ConhecimentoTransporte conhecimentoTransporte,
                                                  final Pageable pageable) {
        var specification = ConhecimentoTransporteSpecs
                .filtrar(empresaId, conhecimentoTransporte);
        return conhecimentoTransporteRepository.findAll(specification, pageable);
    }

    @Transactional
    public void salvar(List<ConhecimentoTransporte> conhecimentos) {
        conhecimentoTransporteRepository.saveAll(conhecimentos);
    }
}
