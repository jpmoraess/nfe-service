package br.com.moraesit.nfeservice.api.mapper;

import br.com.moraesit.nfeservice.api.models.notaEntrada.FiltroNota;
import br.com.moraesit.nfeservice.api.models.notaEntrada.NotaEntradaModel;
import br.com.moraesit.nfeservice.data.entities.NotaEntrada;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotaMapper {

    public NotaEntradaModel entityToModel(NotaEntrada nota) {
        return NotaEntradaModel.builder()
                .id(nota.getId())
                .chave(nota.getChave())
                .numero(nota.getNumero())
                .serie(nota.getSerie())
                .nomeEmitente(nota.getNomeEmitente())
                .cnpjEmitente(nota.getCnpjEmitente())
                .valor(nota.getValor())
                .build();
    }

    public NotaEntrada filtroToEntity(FiltroNota filtro) {
        return NotaEntrada.builder()
                .id(filtro.getId())
                .chave(filtro.getChave())
                .numero(filtro.getNumero())
                .serie(filtro.getSerie())
                .nomeEmitente(filtro.getNomeEmitente())
                .cnpjEmitente(filtro.getCnpjEmitente())
                .valor(filtro.getValor())
                .build();
    }
}
