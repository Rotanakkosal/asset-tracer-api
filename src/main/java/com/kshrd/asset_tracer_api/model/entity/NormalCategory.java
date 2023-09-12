package com.kshrd.asset_tracer_api.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NormalCategory {
    private UUID id;
    private String name;
    private String icon;
    private Timestamp createdAt;
    private UUID createdBy;
    private Timestamp updatedAt;
    private UUID updatedBy;
    private Timestamp deletedAt;
    private UUID deletedBy;
    private UUID superCategoryId;
    private String superCategoryName;
    private String superCategoryIcon;
    private SuperCategory superCategory;
    private Organization organization;
    private UUID organizationId;
    private Integer usage;
}
