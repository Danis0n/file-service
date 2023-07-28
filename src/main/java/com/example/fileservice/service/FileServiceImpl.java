package com.example.fileservice.service;

import com.example.fileservice.dto.Download;
import com.example.fileservice.dto.FileDto;
import com.example.fileservice.entity.FileMap;
import com.example.fileservice.entity.FileMapKey;
import com.example.fileservice.exception.handler.CustomFileNotFoundException;
import com.example.fileservice.exception.handler.CustomFileAlreadyExistsException;
import com.example.fileservice.exception.handler.FileWriteToArchiveException;
import com.example.fileservice.repository.FileRepository;
import com.example.fileservice.util.FileUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.fileservice.mapper.Mapper.mapFileDto;

@RequiredArgsConstructor
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileUtil fileUtil;

    @Override
    public Download download(String archivePath, String fileName) {
        Optional<FileMap> foundedFile =
                fileRepository.findById(new FileMapKey(archivePath, fileName));

        if (foundedFile.isEmpty() || !foundedFile.get().getFileAvailable()) {
            throw new CustomFileNotFoundException("File was not found in archive");
        }

        FileMap fileMap = foundedFile.get();

        FileMapKey key = fileMap.getId();
        File file = fileUtil.getFileFromArchive(
                key.getArchivePath(),
                key.getFileName()
        );

        if (file == null) {
            updateFileAvailability(fileName, archivePath, false);
            throw new CustomFileNotFoundException("File was not found in archive");
        }

        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return new Download(resource, fileName);

        } catch (FileNotFoundException e) {
            throw new CustomFileNotFoundException("File was not found in archive");
        }

    }

    @Override
    public void upload(String fileName,
                          String archivePath,
                          @NonNull MultipartFile multipartFile) {

        String fullFileName = fileName + FileUtil.getExtension(multipartFile.getOriginalFilename());
        Optional<FileMap> foundedFile =
                fileRepository.findById(new FileMapKey(archivePath, fullFileName));

        if (foundedFile.isPresent() &&
                foundedFile.get().getFileAvailable()) {
            throw new CustomFileAlreadyExistsException("File with current name already exists in archive");
        }

        addFile(archivePath, fileName, multipartFile);
    }

    @Override
    public List<FileDto> getFileList() {
        return fileRepository
                .findAllByFileAvailable(true)
                .stream().map(mapFileDto)
                .collect(Collectors.toList());
    }

    @Async
    @Override
    public void updateFileAvailability(String fileName, String archivePath, boolean isAvailable) {
        Optional<FileMap> foundedFile =
                fileRepository.findById(new FileMapKey(archivePath, fileName));
        if (foundedFile.isPresent()) {
            FileMap map = foundedFile.get();
            map.setFileAvailable(isAvailable);
            fileRepository.save(map);
        }
    }

    private void addFile(String archivePath,
                         String fileName,
                         @NonNull MultipartFile multipartFile) {
        try {
            boolean success = fileUtil.writeFile(fileName, archivePath, multipartFile);
            if (success) {
                fileRepository.save(
                        new FileMap(
                                new FileMapKey(
                                        archivePath,
                                        fileName +
                                                FileUtil.getExtension(multipartFile.getOriginalFilename())
                                ),
                                true
                        ));
            }
        } catch (IOException e) {
            throw new FileWriteToArchiveException("Exception while writing file to archive!");
        }
    }

}
