package br.com.moraesit.nfeservice.api.controllers;

import br.com.moraesit.nfeservice.api.mapper.EmpresaMapper;
import br.com.moraesit.nfeservice.api.models.empresa.CadastrarEmpresaRequest;
import br.com.moraesit.nfeservice.api.models.empresa.CadastrarEmpresaResponse;
import br.com.moraesit.nfeservice.api.models.empresa.EmpresaModel;
import br.com.moraesit.nfeservice.service.EmpresaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public List<EmpresaModel> findAll() {
        return empresaService.findAll()
                .stream()
                .map(empresaMapper::empresaToEmpresaModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EmpresaModel findById(@PathVariable Long id) {
        return empresaMapper.empresaToEmpresaModel(empresaService.findById(id));
    }

    @GetMapping("/cnpj/{cnpj}")
    public EmpresaModel findByCnpj(@PathVariable String cnpj) {
        return empresaMapper.empresaToEmpresaModel(empresaService.findByCnpj(cnpj));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CadastrarEmpresaResponse> cadastrar(@Valid @RequestBody CadastrarEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(empresaMapper.empresaToCadastrarEmpresaResponse(empresaService
                        .cadastrar(empresaMapper.cadastrarEmpresaRequestToEmpresa(request))));
    }
}
