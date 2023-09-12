package com.kshrd.asset_tracer_api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationByUser {
    private String orgId;
    private String orgName;
    private String orgLogo;
    private String owner;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String email;
}
