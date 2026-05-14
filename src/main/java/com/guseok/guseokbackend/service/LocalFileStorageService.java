package com.guseok.guseokbackend.service;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private static final Path IMAGE_DIR =
        Paths.get(System.getProperty("user.dir"), "uploads", "images");

    private static final Path VIDEO_DIR =
        Paths.get(System.getProperty("user.dir"), "uploads", "videos");


    @Override
    public String storeImage(MultipartFile file) {
        validateImageFile(file);
        return store(file, IMAGE_DIR);
    }

    @Override
    public String storeVideo(MultipartFile file) {
        validateVideoFile(file);
        return store(file, VIDEO_DIR);
    }

    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    private void validateVideoFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    private String store(MultipartFile file, Path dir) {
        try {
            Files.createDirectories(dir);
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path destination = dir.resolve(filename);
            file.transferTo(destination.toFile());
            return destination.toString().replace("\\", "/");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}
