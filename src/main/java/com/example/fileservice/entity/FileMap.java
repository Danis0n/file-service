package com.example.fileservice.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "file_map")
@NoArgsConstructor
@Getter @Setter
@AllArgsConstructor
public class FileMap {

    @EmbeddedId
    private FileMapKey id;

    @Column(name = "file_available")
    private Boolean fileAvailable;

}