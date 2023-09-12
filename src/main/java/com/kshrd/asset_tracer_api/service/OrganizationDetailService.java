package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.dto.OrganizationDetailDTO;
import com.kshrd.asset_tracer_api.model.entity.OrganizationDetail;
import com.kshrd.asset_tracer_api.model.request.OrganizationDetailRequest;

import java.util.List;
import java.util.UUID;

public interface OrganizationDetailService {
    OrganizationDetailDTO addNewOrganizationDetail(OrganizationDetailRequest organizationDetailRequest);
//    OrganizationDetailDTO joinOrganization
    OrganizationDetailDTO getOrganizationDetail(UUID userId, UUID organizationId);
    List<OrganizationDetailDTO> getAllOrganizationsDetailByUserId(UUID userId);

    List<OrganizationDetailDTO> getAllRequestUsers(UUID organizationId);
    List<OrganizationDetailDTO> getAllJoinedUsers(UUID organizationId);
    UUID deleteUserFromOrganization(UUID userId, UUID orgId);
}
