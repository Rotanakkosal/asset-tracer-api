package com.kshrd.asset_tracer_api.model.dto;

import com.kshrd.asset_tracer_api.model.entity.Organization;
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
public class SuperCategoryDTO {
    private UUID id;
    private String name;
    private String icon;
    private Timestamp createdAt;
    private UUID createdBy;
    private UUID organizationId;
    private Integer usage;
}
