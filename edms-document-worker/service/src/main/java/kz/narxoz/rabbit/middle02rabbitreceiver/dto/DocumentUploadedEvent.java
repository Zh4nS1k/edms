package kz.narxoz.rabbit.middle02rabbitreceiver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentUploadedEvent {
    private Long id;
    private String fileName;
    private String originalName;
    private String mimeType;
    private long size;
    private LocalDateTime addedTime;
}
