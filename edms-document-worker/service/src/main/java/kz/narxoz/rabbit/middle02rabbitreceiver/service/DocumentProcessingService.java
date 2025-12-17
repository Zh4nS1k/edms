package kz.narxoz.rabbit.middle02rabbitreceiver.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import kz.narxoz.rabbit.middle02rabbitreceiver.dto.DocumentUploadedEvent;
import kz.narxoz.rabbit.middle02rabbitreceiver.model.DocumentProcessingResult;
import kz.narxoz.rabbit.middle02rabbitreceiver.repository.DocumentProcessingResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentProcessingService {

    private final MinioClient minioClient;
    private final DocumentProcessingResultRepository repository;

    @Value("${minio.bucket}")
    private String bucket;

    public void process(DocumentUploadedEvent event) {
        try {
            byte[] content = fetchFile(event.getFileName());
            String sha256 = DigestUtils.sha256Hex(content);

            DocumentProcessingResult result = new DocumentProcessingResult();
            result.setDocumentId(event.getId());
            result.setFileName(event.getFileName());
            result.setOriginalName(event.getOriginalName());
            result.setMimeType(event.getMimeType());
            result.setSize(event.getSize());
            result.setSha256(sha256);

            repository.save(result);
            log.info("Processed document id={} file={} size={} hash={}", event.getId(), event.getFileName(), event.getSize(), sha256);
        } catch (Exception e) {
            log.error("Failed to process document event id={} file={}", event.getId(), event.getFileName(), e);
            throw new IllegalStateException(e);
        }
    }

    private byte[] fetchFile(String fileName) throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucket)
                .object(fileName)
                .build();
        try (InputStream is = minioClient.getObject(args)) {
            return is.readAllBytes();
        }
    }
}
