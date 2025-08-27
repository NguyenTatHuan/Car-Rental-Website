package com.springprojects.service.stripe;

import com.springprojects.entity.Invoice;
import com.springprojects.entity.Payment;
import com.springprojects.enums.PaymentMethod;
import com.springprojects.enums.PaymentStatus;
import com.springprojects.repository.InvoiceRepository;
import com.springprojects.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeService {

    @Value("${stripe.api.secret-key}")
    private String stripeApiKey;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    private final PaymentRepository paymentRepository;

    private final InvoiceRepository invoiceRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    private SessionCreateParams buildSessionParams(Invoice invoice,
                                                   String currency,
                                                   long amount,
                                                   String successUrl,
                                                   String cancelUrl,
                                                   PaymentStatus paymentStatus) {

        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?invoiceId=" + invoice.getId() + "&status=" + paymentStatus)
                .setCancelUrl(cancelUrl + "?invoiceId=" + invoice.getId() + "&status=" + paymentStatus)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount(amount)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(invoice.getInvoiceCode() + " - " + paymentStatus)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .putMetadata("invoice_id", invoice.getId().toString())
                .putMetadata("payment_status", paymentStatus.name())
                .build();
    }

    public List<Session> createCheckoutSession(UUID bookingId,
                                               String currency,
                                               String successUrl,
                                               String cancelUrl,
                                               double depositPercentage) throws StripeException {

        if (depositPercentage < 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit must be at least 30%");
        }

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found payment with bookingId: " + bookingId));

        payment.setMethod(PaymentMethod.STRIPE);
        paymentRepository.save(payment);

        double totalAmount = payment.getAmount();
        long depositAmount = Math.round((depositPercentage / 100.0) * totalAmount * 100);
        long remainingAmount = Math.round((totalAmount * 100) - depositAmount);

        Invoice depositInvoice = new Invoice();
        depositInvoice.setPayment(payment);
        depositInvoice.setTotalAmount(depositAmount / 100.0);
        depositInvoice.setInvoiceCode(generateInvoiceCode());
        invoiceRepository.save(depositInvoice);

        Invoice remainingInvoice = new Invoice();
        remainingInvoice.setPayment(payment);
        remainingInvoice.setTotalAmount(remainingAmount / 100.0);
        remainingInvoice.setInvoiceCode(generateInvoiceCode());
        invoiceRepository.save(remainingInvoice);

        Session depositSession = Session.create(
                buildSessionParams(depositInvoice, currency, depositAmount, successUrl, cancelUrl, PaymentStatus.DEPOSITED)
        );

        Session remainingSession = Session.create(
                buildSessionParams(remainingInvoice, currency, remainingAmount, successUrl, cancelUrl, PaymentStatus.SUCCESS)
        );

        return List.of(depositSession, remainingSession);
    }

    public void handleWebhook(String payload, String sigHeader) throws StripeException {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Webhook signature verification failed.", e);
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new IllegalStateException("Cannot deserialize session"));

            String invoiceIdStr = session.getMetadata().get("invoice_id");
            if (invoiceIdStr == null) return;

            UUID invoiceId = UUID.fromString(invoiceIdStr);
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found: " + invoiceId));

            Payment payment = invoice.getPayment();
            if (payment == null) return;

            List<Invoice> invoices = invoiceRepository.findByPaymentId(payment.getId());
            double total = invoices.stream().mapToDouble(Invoice::getTotalAmount).sum();
            double required = payment.getAmount();

            if (total >= required) {
                payment.setStatus(PaymentStatus.SUCCESS);
            } else if (total >= required * 0.3) {
                payment.setStatus(PaymentStatus.DEPOSITED);
            } else {
                payment.setStatus(PaymentStatus.PENDING);
            }
            paymentRepository.save(payment);
        }
    }

    private String generateInvoiceCode() {
        long count = invoiceRepository.count() + 1;
        String year = String.valueOf(java.time.LocalDate.now().getYear());
        return String.format("INV-%s-%04d", year, count);
    }

}
