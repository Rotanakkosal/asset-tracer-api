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
public class ItemDetail {
    private UUID id;
    private String name;
    private String model;
    private String image;
    private Integer qty;
    private Double unitPrice;
    private Integer discount;
    private String description;
    private UUID normalCategoryId;
    private String normalCategoryName;
    private String normalCategoryIcon;
    private UUID superCategoryId;
    private String superCategoryName;
    private String superCategoryIcon;
    private UUID invoiceId;
    private String invoiceCode;
    private UUID organizationId;
    private Timestamp createdAt;
    private UUID createdBy;
    private Timestamp updatedAt;
    private UUID updatedBy;
    private Timestamp deletedAt;
    private UUID deletedBy;
    private List<Asset> assets;
    private Integer countSet;
    private Integer countUnset;
    private Integer usage;
}
