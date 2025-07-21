package com.springprojects.config;

import com.springprojects.dto.kafka.BookingEvent;
import com.springprojects.dto.kafka.BookingResponseEvent;
import com.springprojects.dto.kafka.InvoiceEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.springprojects.dto.kafka");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return props;
    }

    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        FixedBackOff fixedBackOff = new FixedBackOff(100L, 1L);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(fixedBackOff);
        errorHandler.addNotRetryableExceptions(TimeoutException.class);
        errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.error("Kafka retry failed for record: {} on attempt {}", record, deliveryAttempt, ex);
        });
        return errorHandler;
    }

    @Bean
    public ConsumerFactory<String, BookingEvent> bookingEventConsumerFactory() {
        Map<String, Object> props = baseConsumerConfigs();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, BookingEvent.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingEvent> bookingKafkaListenerContainerFactory(DefaultErrorHandler kafkaErrorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, BookingEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookingEventConsumerFactory());
        factory.setCommonErrorHandler(kafkaErrorHandler);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BookingResponseEvent> bookingResponseConsumerFactory() {
        Map<String, Object> props = baseConsumerConfigs();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, BookingResponseEvent.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingResponseEvent> bookingResponseKafkaListenerContainerFactory(DefaultErrorHandler kafkaErrorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, BookingResponseEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookingResponseConsumerFactory());
        factory.setCommonErrorHandler(kafkaErrorHandler);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, InvoiceEvent> invoiceEventConsumerFactory() {
        Map<String, Object> props = baseConsumerConfigs();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, InvoiceEvent.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InvoiceEvent> invoiceKafkaListenerContainerFactory(DefaultErrorHandler kafkaErrorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, InvoiceEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(invoiceEventConsumerFactory());
        factory.setCommonErrorHandler(kafkaErrorHandler);
        return factory;
    }

}
