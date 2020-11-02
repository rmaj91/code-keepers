package com.rmaj91.file.processing;

import com.rmaj91.file.processing.domain.FileProcessRequest;
import com.rmaj91.file.processing.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileProcessService fileProcessService;
    private final FileProcessRequestValidator fileProcessRequestValidator;

    @PostMapping("/process")
    public ResponseEntity<Message> processFile(@RequestBody FileProcessRequest request) {
        fileProcessRequestValidator.validate(request);
        String processedFile = fileProcessService.processFile(request);
        return ResponseEntity.ok(Message.of("Sucessfully processed file: " + processedFile));
    }
}
