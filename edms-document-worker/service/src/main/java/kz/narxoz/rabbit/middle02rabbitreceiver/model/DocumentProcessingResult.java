package kz.narxoz.rabbit.middle02rabbitreceiver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_processing_results")
@Getter
@Setter
@NoArgsConstructor
public class DocumentProcessingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long documentId;
    private String fileName;
    private String originalName;
    private String mimeType;
    private long size;
    private String sha256;
    private LocalDateTime processedAt = LocalDateTime.now();
}
