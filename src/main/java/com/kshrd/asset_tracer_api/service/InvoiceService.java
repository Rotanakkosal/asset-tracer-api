package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.dto.InvoiceDTO;
import com.kshrd.asset_tracer_api.model.entity.Invoice;
import com.kshrd.asset_tracer_api.model.request.InvoiceRequest;
import com.kshrd.asset_tracer_api.model.request.InvoiceRequest2;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    List <InvoiceDTO> getAllInvoices();
    InvoiceDTO getInvoiceById(UUID invoiceId);
    UUID addInvoice(InvoiceRequest invoiceRequest);
    UUID updateInvoice(InvoiceRequest2 invoiceRequest2, UUID invoiceId);
    List<InvoiceDTO> getAllInvoice(UUID orgId, Integer page, Integer size, String invoiceCode, String purchaseBy, String supplier, String sort);
    UUID deleteInvoiceById(UUID invoiceId, UUID organizationId);
    UUID updateInvoiceWithItem(UUID invoiceId, InvoiceRequest invoiceRequest);

    List<InvoiceDTO> getLastThreeInvoices(UUID orgId, Integer page, Integer size);

    Integer getAllInvoiceByOrgId(UUID orgId);
}
