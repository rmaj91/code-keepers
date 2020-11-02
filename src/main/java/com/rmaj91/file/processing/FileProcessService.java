package com.rmaj91.file.processing;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.AreaBreakType;
import com.rmaj91.file.processing.domain.FileProcessRequest;
import com.rmaj91.file.processing.domain.ProcessedFile;
import com.rmaj91.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileProcessService {

    private final FileUtils fileUtils;
    private final ProcessedFileRepository processedFileRepository;
    private final CertificateService certificateService;
    private final UserRepository userRepository;

    public static final String MD5 = "MD5";


    // na runtime exceptionie
    @Transactional
    public String processFile(FileProcessRequest editFileInfo) {
        String hash = fileUtils.createFileHash(editFileInfo.getFilePath(), MD5);
        UUID uuid = UUID.randomUUID();
        persistProcessingFileInfo(editFileInfo.getFilePath(), MD5, hash, uuid);
        createModifiedFile(editFileInfo, hash, uuid);
        return editFileInfo.getFilePath();
    }

    private void createModifiedFile(FileProcessRequest fileProcessRequest, String hash, UUID uuid) {
        try (PdfDocument pdfDoc = fileUtils.getPdfDocument(fileProcessRequest)) {
            Document document = new Document(pdfDoc);
            Paragraph paragraph = createParagraph(hash, uuid, fileProcessRequest.getCertificate());
            pdfDoc.addNewPage(new PageSize(pdfDoc.getLastPage().getPageSize()));
            addParagraphOnLastPage(paragraph, document);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void addParagraphOnLastPage(Paragraph paragraph, Document document) {
        document.add(new AreaBreak(AreaBreakType.LAST_PAGE));
        document.add(paragraph);
    }

    private Paragraph createParagraph(String hash, UUID uuid, String base64Certificate) {
        X509Certificate x509Certificate = certificateService.getX509Certificate(base64Certificate);
        String commonName = certificateService.getCommonName(x509Certificate);
        String organizationName = certificateService.getOrganizationName(x509Certificate);
        Paragraph paragraph = new Paragraph();
        paragraph.add("commonName: " + commonName +"\n");
        paragraph.add("organization: " + organizationName +"\n");
        paragraph.add("hash: " + hash +"\n");
        paragraph.add("uuid: " + uuid +"\n");
        return paragraph;
    }

    protected void persistProcessingFileInfo(String filePath, String hashAlgorithm, String hash, UUID uuid) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProcessedFile processedFile = ProcessedFile.builder()
                .userId(userRepository.findByUsername(username).get().getId())
                .uuid(uuid.toString())
                .filePath(filePath)
                .hash(hash)
                .algorithm(hashAlgorithm)
                .build();
        processedFileRepository.save(processedFile);
    }
}
