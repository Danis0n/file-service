package com.example.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

@Getter
@AllArgsConstructor
public class Download {
    InputStreamResource resource;
    String fileName;
}
