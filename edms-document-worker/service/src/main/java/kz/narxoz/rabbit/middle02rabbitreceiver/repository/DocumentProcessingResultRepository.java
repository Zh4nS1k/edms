package kz.narxoz.rabbit.middle02rabbitreceiver.repository;

import kz.narxoz.rabbit.middle02rabbitreceiver.model.DocumentProcessingResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentProcessingResultRepository extends JpaRepository<DocumentProcessingResult, Long> {
}
