package kz.narxoz.rabbit.middle02rabbitreceiver.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${document.events.exchange}")
    private String exchangeName;

    @Value("${document.events.queue}")
    private String queueName;

    @Value("${document.events.routing.key}")
    private String routingKey;

    @Bean
    public TopicExchange documentExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public TopicExchange dlqExchange() {
        return new TopicExchange(exchangeName + ".dlq", true, false);
    }

    @Bean
    public Queue documentQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", exchangeName + ".dlq")
                .withArgument("x-dead-letter-routing-key", routingKey + ".dlq")
                .build();
    }

    @Bean
    public Queue documentDlq() {
        return QueueBuilder.durable(queueName + ".dlq").build();
    }

    @Bean
    public Binding documentBinding(Queue documentQueue, TopicExchange documentExchange) {
        return BindingBuilder.bind(documentQueue).to(documentExchange).with(routingKey);
    }

    @Bean
    public Binding documentDlqBinding(Queue documentDlq, TopicExchange dlqExchange) {
        return BindingBuilder.bind(documentDlq).to(dlqExchange).with(routingKey + ".dlq");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
