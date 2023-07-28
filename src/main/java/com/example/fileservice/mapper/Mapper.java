package com.example.fileservice.mapper;

import com.example.fileservice.dto.FileDto;
import com.example.fileservice.entity.FileMap;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class Mapper {

    public static Function<FileMap, FileDto> mapFileDto =
            fileMap -> new FileDto(
                    fileMap.getId().getArchivePath(),
                    fileMap.getId().getFileName()
            );
}
