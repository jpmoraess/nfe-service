package br.com.moraesit.nfeservice.api.mapper;

import br.com.moraesit.nfeservice.api.models.conhecimentoTransporte.ConhecimentoTransporteModel;
import br.com.moraesit.nfeservice.api.models.conhecimentoTransporte.FiltroConhecimento;
import br.com.moraesit.nfeservice.data.entities.ConhecimentoTransporte;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConhecimentoMapper {
    public ConhecimentoTransporteModel entityToModel(ConhecimentoTransporte conhecimentoTransporte) {
        return ConhecimentoTransporteModel.builder()
                .id(conhecimentoTransporte.getId())
                .chave(conhecimentoTransporte.getChave())
                .numero(conhecimentoTransporte.getNumero())
                .serie(conhecimentoTransporte.getSerie())
                .nomeEmitente(conhecimentoTransporte.getNomeEmitente())
                .cnpjEmitente(conhecimentoTransporte.getCnpjEmitente())
                .valor(conhecimentoTransporte.getValor())
                .build();
    }

    public ConhecimentoTransporte filtroToEntity(FiltroConhecimento filtro) {
        return ConhecimentoTransporte.builder()
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
