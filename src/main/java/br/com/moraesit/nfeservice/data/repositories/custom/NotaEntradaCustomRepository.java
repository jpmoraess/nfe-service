package br.com.moraesit.nfeservice.data.repositories.custom;

import br.com.moraesit.nfeservice.api.models.notaEntrada.FiltroNota;
import br.com.moraesit.nfeservice.data.repositories.projection.ResumoNotaEntrada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotaEntradaCustomRepository {
    Page<ResumoNotaEntrada> resumir(FiltroNota filtroNota, Pageable pageable);
}
