package com.kshrd.asset_tracer_api.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.asset_tracer_api.model.dto.UserAppDTO;
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
public class Organization {
    private UUID id;
    private String name;
    private String code;
    private String address;
    private String logo;
    private String roleName;
    private Boolean isActive;
    private Timestamp createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID createdBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp updatedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID updatedBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp deletedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID deletedBy;
    private List<UserApp> users;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role role;
    private Integer totalUser;
    private String owner;
    private String organizationName;
    private String userImage;
    private String userName;
    private String email;
    private Boolean isMember;
    private String status;
}
