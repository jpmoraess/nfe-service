package br.com.moraesit.nfeservice.service;

import br.com.moraesit.nfeservice.data.entities.Certificado;
import br.com.moraesit.nfeservice.data.repositories.CertificadoRepository;
import br.com.moraesit.nfeservice.service.storage.LocalStorageService;
import br.com.moraesit.nfeservice.service.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class CertificadoService {

    private final CertificadoRepository certificadoRepository;
    private final LocalStorageService localStorageService;

    public CertificadoService(CertificadoRepository certificadoRepository, LocalStorageService localStorageService) {
        this.certificadoRepository = certificadoRepository;
        this.localStorageService = localStorageService;
    }

    public Certificado findById(final Long id) {
        return certificadoRepository
                .findById(id).orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado."));
    }

    @Transactional
    public Certificado save(final Certificado certificado, final InputStream inputStream) {
        final Optional<Certificado> certificadoOpt = certificadoRepository.findById(certificado.getId());

        if (certificadoOpt.isPresent()) {
            final var nomeAntigo = certificadoOpt.get().getName();
            certificadoOpt.get().setName(localStorageService.generateFileName(certificado.getName()));
            certificadoOpt.get().setPassword(certificado.getPassword());
            certificadoOpt.get().setExpirationDate(LocalDate.now());
            certificadoOpt.get().setSize(certificado.getSize());
            certificadoOpt.get().setContentType(certificado.getContentType());
            final var certificadoSalvo = certificadoRepository.save(certificadoOpt.get());
            certificadoRepository.flush();
            localStorageService.replace(nomeAntigo, StorageService.FileStorage.builder()
                    .name(certificadoSalvo.getName())
                    .inputStream(inputStream)
                    .build());
            return certificadoSalvo;
        } else {
            certificado.setName(localStorageService.generateFileName(certificado.getName()));
            final var certificadoSalvo = certificadoRepository.save(certificado);
            certificadoRepository.flush();
            localStorageService.store(StorageService.FileStorage.builder()
                    .name(certificadoSalvo.getName())
                    .inputStream(inputStream)
                    .build());
            return certificadoSalvo;
        }
    }

    @Transactional
    public void delete(final Long id) {
        final var certificado = findById(id);
        certificadoRepository.delete(certificado);
    }
}
