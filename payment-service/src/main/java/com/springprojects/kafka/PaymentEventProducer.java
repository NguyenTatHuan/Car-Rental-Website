package com.springprojects.kafka;

import com.springprojects.dto.kafka.InvoiceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, InvoiceEvent> kafkaTemplate;

    private static final String TOPIC = "invoice-created-topic";

    public void sendInvoiceEvent(InvoiceEvent event) {
        kafkaTemplate.send(TOPIC, event.getInvoiceId().toString(), event);
    }

}
