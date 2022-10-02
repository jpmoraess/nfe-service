package br.com.moraesit.nfeservice;

import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Slf4j
@Service
public class CertificadoApplicationService {

    public Certificado certificadoA1(final String path, final String password) {
        try {
            return CertificadoService.certificadoPfx(path, password);
        } catch (CertificadoException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
