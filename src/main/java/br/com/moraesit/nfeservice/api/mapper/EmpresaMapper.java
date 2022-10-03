package br.com.moraesit.nfeservice.api.mapper;

import br.com.moraesit.nfeservice.api.models.empresa.CadastroEmpresaRequest;
import br.com.moraesit.nfeservice.api.models.empresa.CadastroEmpresaResponse;
import br.com.moraesit.nfeservice.api.models.empresa.EmpresaModel;
import br.com.moraesit.nfeservice.data.entities.Certificado;
import br.com.moraesit.nfeservice.data.entities.Empresa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class EmpresaMapper {

    public CadastroEmpresaResponse entityToResponse(Empresa empresa) {
        return CadastroEmpresaResponse.builder()
                .id(empresa.getId())
                .nome(empresa.getNome())
                .cnpj(empresa.getCnpj())
                .build();
    }

    public EmpresaModel entityToModel(Empresa empresa) {
        return EmpresaModel.builder()
                .id(empresa.getId())
                .nome(empresa.getNome())
                .cnpj(empresa.getCnpj())
                .ativa(empresa.isAtiva())
                .favorita(empresa.isFavorita())
                .build();
    }

    public Empresa requestToEntity(CadastroEmpresaRequest req) {
        return Empresa.builder()
                .id(0L)
                .nome(req.getNome())
                .cnpj(req.getCnpj())
                .nsu(null)
                .ativa(true)
                .favorita(false)
                .certificado(Certificado.builder()
                        .id(0L)
                        .nome(req.getCertificado().getOriginalFilename())
                        .size(req.getCertificado().getSize())
                        .contentType(req.getCertificado().getContentType())
                        .senha(req.getSenhaCertificado())
                        .validade(LocalDate.now())
                        .build())
                .build();
    }
}
