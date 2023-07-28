package com.example.fileservice.util;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Component
public class FileUtil {

    private final static String ARCHIVE_PATH = "src/main/resources/archives/";
    private final static String ZIP_EXTENSION = ".zip";

    private String getFullArchivePath(String archivePath) {
        return ARCHIVE_PATH + archivePath + ZIP_EXTENSION;
    }


    public boolean writeFile(String fileName,
                             String archivePath,
                             @NonNull MultipartFile multipartFile) throws IOException {

        File newZipFile = File.createTempFile("temp", ".zip");
        FileOutputStream fOut = new FileOutputStream(newZipFile);
        ZipOutputStream zOut = new ZipOutputStream(fOut);

        File archive = new File(getFullArchivePath(archivePath));

        if (archive.exists()) {
            ZipFile zipFile = new ZipFile(archive);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = new ZipEntry(entries.nextElement());
                zOut.putNextEntry(entry);

                InputStream is = zipFile.getInputStream(entry);
                byte[] bytes = is.readAllBytes();
                zOut.write(bytes);
                zOut.closeEntry();
                is.close();
            }

            zipFile.close();
        }

        ZipEntry zipEntry = new ZipEntry(fileName + getExtension(multipartFile.getOriginalFilename()));
        zOut.putNextEntry(zipEntry);

        byte[] bytes = multipartFile.getBytes();
        zOut.write(bytes, 0, bytes.length);
        zOut.closeEntry();

        zOut.close();
        fOut.close();

        if (archive.exists() && archive.delete()) {
            if (!newZipFile.renameTo(archive))
                throw new IOException("Failed to replace old zip file");
            return true;
        }
        else return newZipFile.renameTo(archive);
    }

    public File getFileFromArchive(String archivePath, String fileName) {
        try (ZipFile zipFile = new ZipFile(ARCHIVE_PATH + archivePath + ZIP_EXTENSION)) {

            ZipEntry entry = zipFile.getEntry(fileName);

            if (entry == null) return null;

            File tempFile = Files.createTempFile("temp", null).toFile();
            tempFile.deleteOnExit();

            try (InputStream is = zipFile.getInputStream(entry);
                 FileOutputStream fos = new FileOutputStream(tempFile)) {
                byte[] bytes = new byte[1024];
                int length;
                while ((length = is.read(bytes)) >= 0) {
                    fos.write(bytes, 0, length);
                }
            }
            return tempFile;
        } catch (IOException e) {
            return null;
        }
    }

    public static String getExtension(String originalFileName) {
        return originalFileName.substring(
                originalFileName.lastIndexOf(".")
        );
    }

}
