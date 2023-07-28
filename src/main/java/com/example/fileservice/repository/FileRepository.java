package com.example.fileservice.repository;

import com.example.fileservice.entity.FileMap;
import com.example.fileservice.entity.FileMapKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@EnableAsync
@Repository
public interface FileRepository extends JpaRepository<FileMap, FileMapKey> {

    Optional<FileMap> findById(FileMapKey key);
    List<FileMap> findAllByFileAvailable(boolean available);
}
