package com.rmaj91.file.processing.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileProcessRequest {

    private String certificate;
    private String filePath;
    private String outPath;
}
