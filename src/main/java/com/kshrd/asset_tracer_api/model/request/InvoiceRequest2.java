package com.kshrd.asset_tracer_api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequest2 {
    private String invoiceCode;
    private String purchaseBy;
    private Date purchaseDate;
    private String supplier;
    private String image;
    private UUID organizationId;
}
