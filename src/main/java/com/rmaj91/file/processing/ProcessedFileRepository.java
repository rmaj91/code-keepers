package com.rmaj91.file.processing;

import com.rmaj91.file.processing.domain.ProcessedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ProcessedFileRepository extends JpaRepository<ProcessedFile, UUID> {
}
