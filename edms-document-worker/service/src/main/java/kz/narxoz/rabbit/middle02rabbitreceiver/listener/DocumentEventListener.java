package kz.narxoz.rabbit.middle02rabbitreceiver.listener;

import kz.narxoz.rabbit.middle02rabbitreceiver.dto.DocumentUploadedEvent;
import kz.narxoz.rabbit.middle02rabbitreceiver.service.DocumentProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentEventListener {

    private final DocumentProcessingService processingService;

    @RabbitListener(queues = "${document.events.queue}")
    public void handle(DocumentUploadedEvent event,
                       @Header(name = "amqp_receivedRoutingKey", required = false) String routingKey) {
        log.info("Received document event id={} file={} routing={}", event.getId(), event.getFileName(), routingKey);
        processingService.process(event);
    }
}
