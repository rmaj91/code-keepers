package com.rmaj91.file.processing;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.rmaj91.file.processing.domain.FileProcessRequest;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class FileUtils {

    public String createFileHash(String filePath, String algorithm) {
        try (FileInputStream fileInputStream = getFileInputStream(filePath)) {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(fileInputStream.readAllBytes());
            byte[] digest = messageDigest.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public PdfDocument getPdfDocument(FileProcessRequest fileProcessRequest) throws IOException {
        PdfReader reader = new PdfReader(fileProcessRequest.getFilePath());
        Files.createDirectories(Path.of(fileProcessRequest.getOutPath()));
        String fileName = Path.of(fileProcessRequest.getFilePath()).getFileName().toString();
        String separator = FileSystems.getDefault().getSeparator();
        PdfWriter writer = new PdfWriter(fileProcessRequest.getOutPath() + separator + fileName);
        return new PdfDocument(reader, writer);
    }

    protected FileInputStream getFileInputStream(String filePath) throws FileNotFoundException {
        return new FileInputStream(filePath);
    }

    public long getFileSizeBytes(String filePath) {
        try {
            return Files.size(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
