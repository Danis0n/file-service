package com.example.fileservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FileMapKey implements Serializable {

    @Column(name = "archive_path")
    private String archivePath;

    @Column(name = "file_name")
    private String fileName;

}
