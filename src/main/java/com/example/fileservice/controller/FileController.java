package com.example.fileservice.controller;

import com.example.fileservice.dto.Download;
import com.example.fileservice.dto.FileDto;
import com.example.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/v1/file")
@RestController()
public class FileController {

    private final FileService fileService;

    @GetMapping("/{archivePath}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
                                                 @PathVariable String archivePath) {
        Download resource = fileService.download(archivePath, fileName);
        return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFileName() + "\""
                        )
                        .body(resource.getResource());

    }

    @GetMapping
    public ResponseEntity<List<FileDto>> getFileList() {
        return ResponseEntity.ok(fileService.getFileList());
    }

    @PostMapping("/{archivePath}/{fileName}")
    public ResponseEntity<Void> upload(@PathVariable String fileName,
                                       @PathVariable String archivePath,
                                       @RequestParam("file") MultipartFile file) {
            fileService.upload(fileName, archivePath, file);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
