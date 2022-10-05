package br.com.moraesit.nfeservice.service;

import br.com.moraesit.nfeservice.data.entities.Empresa;
import br.com.moraesit.nfeservice.service.storage.LocalStorageService;
import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class CertificadoService {

    private final LocalStorageService storageService;

    public CertificadoService(LocalStorageService storageService) {
        this.storageService = storageService;
    }

    public Certificado a1(Empresa empresa) {
        try (var inputStream = storageService.retrieve(empresa.getCertificado().getNome())) {
            return br.com.swconsultoria.certificado.CertificadoService
                    .certificadoPfxBytes(inputStream.readAllBytes(), empresa.getCertificado().getSenha());
        } catch (CertificadoException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
