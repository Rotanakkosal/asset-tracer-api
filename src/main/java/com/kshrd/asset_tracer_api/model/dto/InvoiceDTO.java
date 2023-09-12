package com.kshrd.asset_tracer_api.model.dto;

import com.kshrd.asset_tracer_api.model.entity.ItemDetail;
import com.kshrd.asset_tracer_api.model.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDTO {
    private UUID id;
    private String invoiceCode;
    private String purchaseBy;
    private Timestamp purchaseDate;
    private String supplier;
    private UUID organizationId;
    private String image;
    private Timestamp createdAt;
    private UUID createdBy;
    private List<ItemDetail> itemDetails;
}
