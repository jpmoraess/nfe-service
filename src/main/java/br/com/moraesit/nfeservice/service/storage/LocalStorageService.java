package br.com.moraesit.nfeservice.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class LocalStorageService implements StorageService {

    private final Path path = Path.of("/usr/src/api/storage").normalize();

    @Override
    public void store(FileStorage fileStorage) {
        try {
            final var filePath = getFilePath(fileStorage.getName());
            Files.createDirectories(filePath.getParent());
            FileCopyUtils.copy(fileStorage.getInputStream(), Files.newOutputStream(filePath));
        } catch (Exception e) {
            log.error("Could not possible store file.");
            throw new RuntimeException("Could not possible store file.");
        }
    }

    @Override
    public void remove(String fileName) {
        try {
            final var filePath = getFilePath(fileName);
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            log.error("Could not possible delete file.");
            throw new RuntimeException("Could not possible delete file.");
        }
    }

    @Override
    public InputStream retrieve(String fileName) {
        try {
            final var filePath = getFilePath(fileName);
            return Files.newInputStream(filePath);
        } catch (Exception e) {
            log.error("Could not possible retrieve file.");
            throw new RuntimeException("Could not possible retrieve file.");
        }
    }

    private Path getFilePath(final String fileName) {
        return path.resolve(Path.of(fileName));
    }
}
