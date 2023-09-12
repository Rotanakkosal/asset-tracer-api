package com.kshrd.asset_tracer_api.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Invoice {
    private UUID id;
    private String invoiceCode;
    private String purchaseBy;
    private Timestamp purchaseDate;
    private String supplier;
    private Organization organization;
    private UUID organizationId;
    private String image;
    private Timestamp createdAt;
    private UUID createdBy;
    private Timestamp updatedAt;
    private UUID updatedBy;
    private Timestamp deletedAt;
    private UUID deletedBy;
    private List<ItemDetail> itemDetails;
}
