package com.kshrd.asset_tracer_api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDetailUpdateRequest {
    private UUID invoiceId;
    private String name;
    private String model;
    private String image;
    private Integer qty;
    private Double unitPrice;
    private Integer discount;
    private String description;
    private UUID normalCategoryId;
    private UUID organizationId;
}
