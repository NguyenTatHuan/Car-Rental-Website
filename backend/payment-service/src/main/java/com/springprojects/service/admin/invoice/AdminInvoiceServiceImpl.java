package com.springprojects.service.admin.invoice;

import com.springprojects.dto.invoice.InvoiceCreateRequest;
import com.springprojects.dto.invoice.InvoiceResponse;
import com.springprojects.dto.invoice.InvoiceUpdateRequest;
import com.springprojects.dto.kafka.BookingEvent;
import com.springprojects.dto.kafka.InvoiceEvent;
import com.springprojects.entity.Invoice;
import com.springprojects.entity.Payment;
import com.springprojects.enums.PaymentStatus;
import com.springprojects.kafka.PaymentEventProducer;
import com.springprojects.repository.InvoiceRepository;
import com.springprojects.repository.PaymentRepository;
import com.springprojects.service.redis.BookingCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInvoiceServiceImpl implements AdminInvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final PaymentRepository paymentRepository;

    private final BookingCacheService bookingCacheService;

    private final PaymentEventProducer paymentEventProducer;

    private InvoiceResponse mapToDto(Invoice invoice) {
        InvoiceResponse dto = new InvoiceResponse();
        dto.setId(invoice.getId());
        dto.setPaymentId(invoice.getPayment().getId());
        dto.setInvoiceCode(invoice.getInvoiceCode());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setIssuedDate(invoice.getIssuedDate());
        return dto;
    }

    private String generateInvoiceCode() {
        long count = invoiceRepository.count() + 1;
        String year = String.valueOf(LocalDate.now().getYear());
        return String.format("INV-%s-%04d", year, count);
    }

    private void updatePaymentStatus(Payment payment) {
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

    @Override
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceResponse getInvoiceById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found invoice with ID:" + id));
        return mapToDto(invoice);
    }

    @Override
    public InvoiceResponse createInvoice(InvoiceCreateRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found payment with ID:" + request.getPaymentId()));

        Invoice invoice = new Invoice();
        invoice.setPayment(payment);
        invoice.setTotalAmount(request.getTotalAmount());
        invoice.setInvoiceCode(generateInvoiceCode());
        Invoice saved = invoiceRepository.save(invoice);

        updatePaymentStatus(payment);

        BookingEvent booking = bookingCacheService.getBookingEvent(payment.getBookingId());
        if (booking != null) {
            InvoiceEvent event = new InvoiceEvent();
            event.setInvoiceId(saved.getId());
            event.setPaymentId(payment.getId());
            event.setInvoiceCode(saved.getInvoiceCode());
            event.setTotalAmount(saved.getTotalAmount());
            event.setAmount(payment.getAmount());
            event.setUserId(booking.getUserId());
            event.setFullName(booking.getFullName());
            event.setEmail(booking.getEmail());
            event.setCarName(booking.getCarName());
            paymentEventProducer.sendInvoiceEvent(event);
        }

        return mapToDto(saved);
    }

    @Override
    public InvoiceResponse updateInvoice(UUID id, InvoiceUpdateRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found invoice with ID:" + id));
        invoice.setTotalAmount(request.getTotalAmount());
        Invoice updated = invoiceRepository.save(invoice);
        updatePaymentStatus(invoice.getPayment());
        return mapToDto(updated);
    }

    @Override
    public void deleteInvoice(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found invoice with ID:" + id));
        Payment payment = invoice.getPayment();
        invoiceRepository.deleteById(id);
        updatePaymentStatus(payment);
    }

}
