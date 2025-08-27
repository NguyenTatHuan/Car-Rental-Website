package com.springprojects.service.admin.invoice;

import com.springprojects.dto.invoice.InvoiceCreateRequest;
import com.springprojects.dto.invoice.InvoiceResponse;
import com.springprojects.dto.invoice.InvoiceUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface AdminInvoiceService {

    List<InvoiceResponse> getAllInvoices();

    InvoiceResponse getInvoiceById(UUID id);

    InvoiceResponse createInvoice(InvoiceCreateRequest request);

    InvoiceResponse updateInvoice(UUID id, InvoiceUpdateRequest request);

    void deleteInvoice(UUID id);

}
