package com.kshrd.asset_tracer_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDetailAllDTO {
    private UUID id;
    private String name;
    private String image;
    private String invoiceId;
    private Integer countSet;
    private Integer countUnset;
    private String invoiceCode;
    private Integer usage;
}
