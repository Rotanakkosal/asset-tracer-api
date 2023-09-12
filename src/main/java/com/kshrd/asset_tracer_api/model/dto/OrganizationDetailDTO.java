package com.kshrd.asset_tracer_api.model.dto;

import com.kshrd.asset_tracer_api.model.entity.Organization;
import com.kshrd.asset_tracer_api.model.entity.Role;
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
public class OrganizationDetailDTO {
    private UUID userId;
    private Boolean isActive;
    private Timestamp createdAt;
    private UUID createdBy;
    private Organization organization;
    private Role role;
}
