package com.kshrd.asset_tracer_api.model.request;

import com.kshrd.asset_tracer_api.model.entity.ItemDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequest {
    private String invoiceCode;
    private String purchaseBy;
    private Date purchaseDate;
    private String image;
    private UUID organizationId;
    private String supplier;
    private List<ItemDetailRequest> itemDetailRequests;
}
