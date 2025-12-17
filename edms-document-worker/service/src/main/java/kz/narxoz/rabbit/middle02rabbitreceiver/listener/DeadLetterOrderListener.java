package kz.narxoz.rabbit.middle02rabbitreceiver.listener;

import kz.narxoz.rabbit.middle02rabbitreceiver.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeadLetterOrderListener {

    @RabbitListener(queues = {
            "${mq.order.dlq.almaty}",
            "${mq.order.dlq.astana}",
            "${mq.order.dlq.shymkent}",
            "${mq.order.dlq.default}"
    })
    public void handleDeadLetters(OrderDTO order,
                                  @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
                                  @Header(AmqpHeaders.CONSUMER_QUEUE) String queue) {
        log.error("Order routed to DLQ queue='{}' routingKey='{}' payload={{restaurant='{}', courier='{}', foods={}, status='{}'}}",
                queue,
                routingKey,
                order.getRestaurant(),
                order.getCourier(),
                order.getFoods(),
                order.getStatus());
    }
}
