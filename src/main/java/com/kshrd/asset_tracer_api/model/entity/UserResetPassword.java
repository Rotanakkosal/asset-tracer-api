package com.kshrd.asset_tracer_api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResetPassword {
    private UUID id;
    private UUID userId;
    private String code;
    private Date expireAt;
    private Timestamp createAt;
}
