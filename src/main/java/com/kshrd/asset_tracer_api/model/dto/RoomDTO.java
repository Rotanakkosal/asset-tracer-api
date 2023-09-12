package com.kshrd.asset_tracer_api.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.asset_tracer_api.model.entity.Organization;
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
public class RoomDTO {
    private UUID id;
    private String name;
    private String type;
    private String floor;
    private String description;
    private String image;
    private Timestamp createdAt;
    private UUID createdBy;
    private Organization organization;
    private UUID organizationId;
    private String  organizationName;
    private String createdByUsername;
    private Integer usage;
}
