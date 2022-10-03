package br.com.moraesit.nfeservice.service;

import br.com.moraesit.nfeservice.data.entities.Empresa;
import br.com.moraesit.nfeservice.data.repositories.EmpresaRepository;
import br.com.moraesit.nfeservice.exception.BusinessException;
import br.com.moraesit.nfeservice.service.storage.LocalStorageService;
import br.com.moraesit.nfeservice.service.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class EmpresaService {
    private final LocalStorageService storageService;

    private final EmpresaRepository empresaRepository;

    public EmpresaService(LocalStorageService storageService, EmpresaRepository empresaRepository) {
        this.storageService = storageService;
        this.empresaRepository = empresaRepository;
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));
    }

    public Empresa buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));
    }

    @Transactional
    public Empresa cadastrar(Empresa empresa, InputStream certificadoIs) {
        if (empresaRepository.existsByCnpj(empresa.getCnpj()))
            throw new BusinessException("Já existe uma empresa cadastrada com esse CNPJ.");

        empresa.getCertificado().setNome(storageService.generateFileName(empresa.getCertificado().getNome()));

        empresa = empresaRepository.save(empresa);

        empresaRepository.flush();

        storageService.store(StorageService.FileStorage.builder()
                .name(empresa.getCertificado().getNome())
                .inputStream(certificadoIs)
                .build());

        return empresa;
    }

    @Transactional
    public Empresa atualizar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }
}
