package br.com.moraesit.nfeservice.api.controllers;

import br.com.moraesit.nfeservice.api.mapper.EmpresaMapper;
import br.com.moraesit.nfeservice.api.models.empresa.CadastroEmpresaRequest;
import br.com.moraesit.nfeservice.api.models.empresa.CadastroEmpresaResponse;
import br.com.moraesit.nfeservice.api.models.empresa.EmpresaModel;
import br.com.moraesit.nfeservice.service.EmpresaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {
    private final EmpresaMapper empresaMapper;
    private final EmpresaService empresaService;

    public EmpresaController(EmpresaMapper empresaMapper, EmpresaService empresaService) {
        this.empresaMapper = empresaMapper;
        this.empresaService = empresaService;
    }
    @GetMapping
    public List<EmpresaModel> listarTodas() {
        return empresaService.listarTodas()
                .stream()
                .map(empresaMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EmpresaModel buscarPorId(@PathVariable Long id) {
        return empresaMapper.entityToModel(empresaService.buscarPorId(id));
    }

    @GetMapping("/cnpj/{cnpj}")
    public EmpresaModel buscarPorCnpj(@PathVariable String cnpj) {
        return empresaMapper.entityToModel(empresaService.buscarPorCnpj(cnpj));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CadastroEmpresaResponse> cadastrar(@Valid final CadastroEmpresaRequest req) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(empresaMapper.entityToResponse(empresaService
                        .cadastrar(empresaMapper.requestToEntity(req), req.getCertificado().getInputStream())));
    }
}
