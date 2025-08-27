package com.springproject.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "user.exchange";

    public static final String CREATED_ROUTING_KEY = "user.created";
    public static final String CREATED_QUEUE = "conversation.queue";

    public static final String UPDATED_ROUTING_KEY = "user.updated";
    public static final String UPDATED_QUEUE = "conversation.update.queue";

    public static final String DELETED_ROUTING_KEY = "user.deleted";
    public static final String DELETED_QUEUE = "conversation.delete.queue";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue createdQueue() {
        return new Queue(CREATED_QUEUE);
    }

    @Bean
    public Queue updatedQueue() {
        return new Queue(UPDATED_QUEUE);
    }

    @Bean
    public Queue deletedQueue() {
        return new Queue(DELETED_QUEUE);
    }

    @Bean
    public Binding createdBinding(@Qualifier("createdQueue") Queue createdQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(createdQueue)
                .to(exchange)
                .with(CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding updatedBinding(@Qualifier("updatedQueue") Queue updatedQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(updatedQueue)
                .to(exchange)
                .with(UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding deletedBinding(@Qualifier("deletedQueue") Queue deletedQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(deletedQueue)
                .to(exchange)
                .with(DELETED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

}
