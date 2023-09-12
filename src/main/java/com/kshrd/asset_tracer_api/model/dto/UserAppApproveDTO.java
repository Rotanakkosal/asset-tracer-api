package com.kshrd.asset_tracer_api.model.dto;

import com.kshrd.asset_tracer_api.model.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAppApproveDTO {
    private UUID id;
    private String email;
    private String name;
    private String gender;
    private String phone;
    private String address;
}
