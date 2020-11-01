package com.rmaj91.file.processing;

import com.rmaj91.file.processing.domain.FileProcessRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileProcessRequestValidator {

    public static long MEGABYTE_TO_BYTES_MULTIPLIER = 1024L*1024L;
    public static long MIN_FILE_SIZE_MEGA_BYTES = 1L;
    public static long MAX_FILE_SIZE_MEGA_BYTES = 20L;

    private final CertificateService certificateService;
    private final FileUtils fileUtils;

    public void validate(FileProcessRequest fileProcessRequest) {
        log.info("validation started");
        certificateService.validateCertificate(fileProcessRequest.getCertificate());
        validateFileSize(fileProcessRequest.getFilePath());
        validateOutputPath(fileProcessRequest.getOutPath());
        log.info("validating ended");
    }

    protected void validateFileSize(String filePath) {
        long fileSizeBytes = fileUtils.getFileSizeBytes(filePath);
        if (fileSizeBytes < MIN_FILE_SIZE_MEGA_BYTES * MEGABYTE_TO_BYTES_MULTIPLIER
            || fileSizeBytes > MAX_FILE_SIZE_MEGA_BYTES * MEGABYTE_TO_BYTES_MULTIPLIER) {
            log.error("Validation failed");
            throw new RuntimeException();
        }
    }

    protected void validateOutputPath(String filePath) {
        Path path = Path.of(filePath);
        if (isDirectory(path) && exists(path)) {
            log.error("Validation failed");
            throw new RuntimeException();
        }
    }
}
