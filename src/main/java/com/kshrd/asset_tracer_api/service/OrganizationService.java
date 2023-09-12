package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.dto.*;
import com.kshrd.asset_tracer_api.model.entity.NotificationByUser;
import com.kshrd.asset_tracer_api.model.entity.Organization;
import com.kshrd.asset_tracer_api.model.request.OrganizationJoinRequest;
import com.kshrd.asset_tracer_api.model.request.OrganizationRequest;
import org.apache.catalina.User;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    List<OrganizationDTO> getAllOrganizations();
    List<OrganizationDTO> getAllOrganizations(Integer page, Integer size);
    OrganizationDTO getOrganizationById(UUID organizationId);
    UUID addOrganization(OrganizationRequest organizationRequest);
    UUID joinOrganization(OrganizationJoinRequest organizationJoinRequest);
    UUID updateOrganization(OrganizationRequest organizationRequest, UUID organizationId);
    boolean deleteOrganization(UUID organizationId);
    List<OrganizationDTO> getAllOrganizationsByUserId(UUID userId, String search, String sort);
    UserAppApproveDTO approveUserRequestJoin(UUID userId, UUID organizationId);
    UUID rejectUserRequestJoin(UUID userId, UUID organizationId);
    List<OrganizationDTO> getAllUsersByOrgId(UUID orgId);
    List<OrganizationDTO> getAllRequestUsersByUserId(String search, String sort);
    List<UserAppDTO> getAllJoinedUsersByOrgId(UUID orgId, String search, String sort);
    List<OrganizationDTO> getAllUserJoined(UUID orgId, String search, String sort);
    Integer getCountUserJoined(UUID orgId);
    List<UserAppDTO> getAllNotificationRequest(UUID orgId, Integer page, Integer size);
    List<OrganizationInvitedDTO> getAllInvitationFromOrganization(Integer page, Integer size, String search, String sort);
    OrganizationInvitedDTO approveInvitation(UUID orgId);
    OrganizationInvitedDTO rejectInvitation(UUID orgId);
    List<OrganizationDTO> getAllOrganizationsByOwner();
    List<NotificationByOrgDTO> getAllNotificationsByOrgId(UUID orgId, Integer page, Integer size, String search, String sort);
    List<NotificationByUser> getAllNotificationByUser();
    UUID removeMember(UUID userId, UUID orgId);
}
