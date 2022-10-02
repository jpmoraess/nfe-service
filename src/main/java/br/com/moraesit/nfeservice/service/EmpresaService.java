package br.com.moraesit.nfeservice.service;

import br.com.moraesit.nfeservice.data.entities.Empresa;
import br.com.moraesit.nfeservice.data.repositories.EmpresaRepository;
import br.com.moraesit.nfeservice.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    public Empresa findById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));
    }

    public Empresa findByCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));
    }

    @Transactional
    public Empresa cadastrar(Empresa empresa) {
        if (empresaRepository.existsByCnpj(empresa.getCnpj()))
            throw new BusinessException("Já existe uma empresa cadastrada com esse CNPJ.");
        return empresaRepository.save(empresa);
    }
}
