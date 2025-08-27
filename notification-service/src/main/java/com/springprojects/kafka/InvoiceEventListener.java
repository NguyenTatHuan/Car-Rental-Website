package com.springprojects.kafka;

import com.springprojects.dto.EmailRequestDto;
import com.springprojects.dto.kafka.InvoiceEvent;
import com.springprojects.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceEventListener {

    private final EmailService emailService;

    private void sendEmail(String to, String subject, String template, Map<String, Object> variables) {
        EmailRequestDto email = new EmailRequestDto(to, subject, template, variables);
        emailService.sendTemplateEmail(email);
    }

    @KafkaListener(
            topics = "invoice-created-topic",
            groupId = "notification-group-created",
            containerFactory = "invoiceKafkaListenerContainerFactory"
    )
    public void handleInvoiceCreated(InvoiceEvent event) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", event.getFullName());
        variables.put("invoiceCode", event.getInvoiceCode());
        variables.put("totalAmount", event.getTotalAmount());
        variables.put("carName", event.getCarName());
        variables.put("amount", event.getAmount());

        sendEmail(event.getEmail(), "Invoice for Car Rental Payment", "invoice-template", variables);
    }

}
