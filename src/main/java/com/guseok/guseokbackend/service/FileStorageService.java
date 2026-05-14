package com.guseok.guseokbackend.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeImage(MultipartFile file);
    String storeVideo(MultipartFile file);
}
