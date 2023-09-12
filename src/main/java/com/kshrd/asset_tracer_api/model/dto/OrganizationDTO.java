package com.kshrd.asset_tracer_api.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.asset_tracer_api.model.entity.Role;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationDTO {
    private UUID id;
    private String name;
    private String code;
    private String address;
    private String logo;
    private String roleName;
    private Boolean isActive;
    private Boolean isMember;
    private String status;
    private Timestamp createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role role;
    private List<UserAppDTO> users;
    private String userName;
    private String userImage;
    private Integer totalUser;
}
