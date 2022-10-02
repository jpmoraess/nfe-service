package br.com.moraesit.nfeservice.service.storage;

import lombok.*;

import java.io.InputStream;
import java.util.UUID;

public interface StorageService {

    void store(final FileStorage fileStorage);

    void remove(final String fileName);

    InputStream retrieve(final String fileName);

    default void replace(final String oldFileName, final FileStorage fileStorage) {
        this.store(fileStorage);

        if (!oldFileName.isBlank() && !oldFileName.isEmpty())
            this.remove(oldFileName);
    }

    default String generateFileName(final String originalName) {
        return UUID.randomUUID() + "_" + originalName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FileStorage {
        private String name;
        private InputStream inputStream;
    }
}
