package com.kshrd.asset_tracer_api.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.asset_tracer_api.model.entity.Organization;
import com.kshrd.asset_tracer_api.model.entity.SuperCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NormalCategoryDTO {
    private UUID id;
    private String name;
    private String icon;
    private Timestamp createdAt;
    private UUID createdBy;
    private UUID superCategoryId;
    private String superCategoryName;
    private String superCategoryIcon;
    private SuperCategory superCategory;
    private UUID organizationId;
    private Integer usage;
}
