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
public class NotificationByOrgDTO {
    private UUID id;
    private String name;
    private String email;
    private String image;
    private Timestamp createdAt;
}
