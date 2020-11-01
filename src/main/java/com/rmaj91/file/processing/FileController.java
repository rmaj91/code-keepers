package com.rmaj91.file.processing;

import com.rmaj91.file.processing.domain.FileProcessRequest;
import lombok.RequiredArgsConstructor;
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
    public void processFile(@RequestBody FileProcessRequest request) {
        fileProcessRequestValidator.validate(request);
        fileProcessService.processFile(request);
    }
}
