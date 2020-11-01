package com.rmaj91.file.processing.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedFile {

    @Id
    private String uuid;
    private String hash;
    private String algorithm;
    @CreationTimestamp
    private LocalDateTime timestamp;
    private String filePath;
    private long userId;
}
