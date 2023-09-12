package com.kshrd.asset_tracer_api.model.dto;

import com.kshrd.asset_tracer_api.model.entity.UserApp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationInvitedDTO {
    private UUID id;
    private String organizationName;
    private String logo;
    private String owner;
    private String userImage;
    private String email;
    private Boolean isActive;
    private Boolean isMember;
    private String status;
    private String createdAt;
}
