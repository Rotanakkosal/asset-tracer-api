package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.request.InvoiceRequest;
import com.kshrd.asset_tracer_api.model.request.InvoiceRequest2;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.repository.InvoiceRepository;
import com.kshrd.asset_tracer_api.repository.ItemDetailRepository;
import com.kshrd.asset_tracer_api.service.InvoiceService;
import com.kshrd.asset_tracer_api.service.serviceImp.InvoiceServiceImp;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;

    @GetMapping
    public ResponseEntity<?> getAllInvoices() {
        return BodyResponse.getBodyResponse(invoiceService.getAllInvoices());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable("id") UUID invoiceId) {
        return BodyResponse.getBodyResponse(invoiceService.getInvoiceById(invoiceId));
    }


    @PostMapping
    public ResponseEntity<?> addInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        UUID invoiceId = invoiceService.addInvoice(invoiceRequest);
        return BodyResponse.getBodyResponse(invoiceService.getInvoiceById(invoiceId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateInvoiceWithItem(@PathVariable("id") UUID invoiceId, @RequestBody InvoiceRequest invoiceRequest) {
        UUID id = invoiceService.updateInvoiceWithItem(invoiceId, invoiceRequest);
        return BodyResponse.getBodyResponse(invoiceService.getInvoiceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@RequestBody InvoiceRequest2 invoiceRequest2, @PathVariable("id") UUID invoiceId) {
        invoiceService.updateInvoice(invoiceRequest2, invoiceId);
        return BodyResponse.getBodyResponse(invoiceService.getInvoiceById(invoiceId));
    }

    @GetMapping("/filter/{orgId}")
    public ResponseEntity<?> getAllInvoice(@PathVariable("orgId") UUID orgId,
                                           @RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size,
                                           @RequestParam(required = false) String invoiceCode,
                                           @RequestParam(required = false) String purchaseBy,
                                           @RequestParam(required = false) String supplier,
                                           @RequestParam(required = false) String sort) {
        Integer countData = Integer.valueOf(invoiceRepository.getCountAllInvoices(orgId, invoiceCode, purchaseBy, supplier));
        return BodyResponse.getBodyResponse(invoiceService.getAllInvoice(orgId, page, size, invoiceCode, purchaseBy, supplier, sort), countData);
    }

    @GetMapping("/{orgId}/get-last-three")
    public ResponseEntity<?> getLastThreeInvoices(@PathVariable("orgId") UUID orgId,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "3") Integer size) {
        return BodyResponse.getBodyResponse(invoiceService.getLastThreeInvoices(orgId, page, size), invoiceRepository.getCountData());
    }
}
