package br.com.moraesit.nfeservice.api.mapper;

import br.com.moraesit.nfeservice.api.models.empresa.CadastrarEmpresaRequest;
import br.com.moraesit.nfeservice.api.models.empresa.CadastrarEmpresaResponse;
import br.com.moraesit.nfeservice.api.models.empresa.EmpresaModel;
import br.com.moraesit.nfeservice.data.entities.Empresa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmpresaMapper {

    public Empresa cadastrarEmpresaRequestToEmpresa(CadastrarEmpresaRequest request) {
        return Empresa
                .builder()
                .cnpj(request.getCnpj())
                .nome(request.getNome())
                .ativa(true)
                .favorita(false)
                .build();
    }

    public CadastrarEmpresaResponse empresaToCadastrarEmpresaResponse(Empresa empresa) {
        return CadastrarEmpresaResponse.builder()
                .id(empresa.getId())
                .nome(empresa.getNome())
                .cnpj(empresa.getCnpj())
                .build();
    }

    public EmpresaModel empresaToEmpresaModel(Empresa empresa) {
        return EmpresaModel.builder()
                .id(empresa.getId())
                .nome(empresa.getNome())
                .cnpj(empresa.getCnpj())
                .ativa(empresa.isAtiva())
                .favorita(empresa.isFavorita())
                .build();
    }
}
