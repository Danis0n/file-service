package com.example.fileservice.service;

import com.example.fileservice.dto.Download;
import com.example.fileservice.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FileService {

    void updateFileAvailability(String fileName, String archivePath, boolean isAvailable);

    Download download(String archivePath, String fileName);

    void upload(String fileName, String archivePath, MultipartFile file);

    List<FileDto> getFileList();
}
