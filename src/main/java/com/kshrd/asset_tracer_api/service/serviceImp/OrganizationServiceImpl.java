package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.exception.*;
import com.kshrd.asset_tracer_api.model.dto.*;
import com.kshrd.asset_tracer_api.model.entity.NotificationByUser;
import com.kshrd.asset_tracer_api.model.entity.Organization;
import com.kshrd.asset_tracer_api.model.entity.OrganizationDetail;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.mapper.OrganizationMapper;
import com.kshrd.asset_tracer_api.model.mapper.UserAppApproveMapper;
import com.kshrd.asset_tracer_api.model.mapper.UserAppMapper;
import com.kshrd.asset_tracer_api.model.request.OrganizationJoinRequest;
import com.kshrd.asset_tracer_api.model.request.OrganizationRequest;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.repository.OrganizationRepository;
import com.kshrd.asset_tracer_api.repository.UserAppRepository;
import com.kshrd.asset_tracer_api.service.OrganizationDetailService;
import com.kshrd.asset_tracer_api.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserAppRepository userAppRepository;
    private final OrganizationDetailRepository organizationDetailRepository;
    private UserAppApproveMapper userAppApproveMapper;
    private OrganizationMapper organizationMapper;
    private UserAppMapper userAppMapper;

    private UUID getCurrentUser() {
        Object getContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(getContext.equals("anonymousUser")) {
            throw new UnauthorizedExceptionHandler("Unauthorized User");
        }
        UserApp user = (UserApp) getContext;
        return user.getId();
    }

    @Override
    public List<OrganizationDTO> getAllOrganizations() {

        List<Organization> organizations = organizationRepository.getAllOrganizations();

        if(organizations.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return organizationMapper.INSTANCE.toOrganizationDto(organizations);
    }

    @Override
    public List<OrganizationDTO> getAllOrganizations(Integer page, Integer size) {

        List<Organization> organizations = organizationRepository.getAllOrganizationWithFilter(page, size);


        if (organizations.isEmpty()){
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return organizationMapper.INSTANCE.toOrganizationDto(organizations);
    }

    @Override
    public OrganizationDTO getOrganizationById(UUID organizationId) {

        Organization organization = organizationRepository.getOrganizationById(organizationId);

        if(organization == null){
            throw new NotFoundExceptionHandler("Organization not found");
        }

        return organizationMapper.INSTANCE.toOrganizationDto(organization);
    }

    @Override
    public UUID addOrganization(OrganizationRequest organizationRequest) {

        UserApp user = userAppRepository.getUserByID(getCurrentUser());

        if(user == null) {
            throw new NotFoundExceptionHandler("User not found");
        }
        else if (organizationRequest.getName().isEmpty()) {
            throw new FieldEmptyExceptionHandler("Organization name field is empty");
        }
        else if (organizationRequest.getName().isBlank()) {
            throw new FieldBlankExceptionHandler("Organization name field is blank");
        }

        String code = "";
        while (true) {
            Random random = new Random();
            code = String.format("%06d", random.nextInt(999999));

            if(organizationRepository.getOrganizationByCode(code) == null) {
                break;
            }
        }

        UUID organizationId = organizationRepository.addOrganization(organizationRequest, code, getCurrentUser());
        organizationRepository.addOrganizationDetail(getCurrentUser(), organizationId, getCurrentUser());

        return organizationId;
    }

    @Override
    public UUID joinOrganization(OrganizationJoinRequest organizationJoinRequest) {

        Organization organization = organizationRepository.getOrganizationByCode(organizationJoinRequest.getCode());

        if(organization == null) {
            throw new NotFoundExceptionHandler("Organization not found");
        }

        Boolean isHasOrganizationDetail = organizationDetailRepository.isHasOrganizationDetail(getCurrentUser(), organization.getId());

        if(organizationDetailRepository.isUserWasRejected(getCurrentUser(), organization.getId())) {
            return organizationRepository.joinOrganizationWhenUserWasRejected(getCurrentUser(), organization.getId());
        }
        else if(isHasOrganizationDetail) {
            throw new UserDuplicateExceptionHandler("Duplicate user");
        }
        else if (organizationJoinRequest.getCode().isEmpty()) {
            throw new FieldEmptyExceptionHandler("Organization code field is empty");
        }
        else if (organizationJoinRequest.getCode().isBlank()) {
            throw new FieldBlankExceptionHandler("Organization name field is blank");
        }

        return organizationRepository.joinOrganization(getCurrentUser(), organization.getId(), getCurrentUser());
    }

    @Override
    public UUID updateOrganization(OrganizationRequest organizationRequest, UUID organizationId) {

        if (organizationRepository.getOrganizationById(organizationId) == null) {
            throw new NotFoundExceptionHandler("Organization not found");
        }

        return organizationRepository.updateOrganization(organizationRequest, organizationId);
    }

    @Override
    public boolean deleteOrganization(UUID organizationId) {

        if (organizationRepository.getOrganizationById(organizationId) == null) {
            throw new NotFoundExceptionHandler("Organization not found");
        }

        return organizationRepository.deleteOrganization(organizationId);
    }

    @Override
    public List<OrganizationDTO> getAllOrganizationsByUserId(UUID userId, String search, String sort) {

        List<Organization> organizations = organizationRepository.getAllOrganizationsByByUserId(userId,search,sort, getCurrentUser());

        if(organizations.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return organizationMapper.INSTANCE.toOrganizationDto(organizations);
    }

    @Override
    public UserAppApproveDTO approveUserRequestJoin(UUID userId, UUID organizationId) {

        UserApp user = userAppRepository.getUserByID(userId);

        if(user == null ) {
            throw new NotFoundExceptionHandler("User not found");
        }
        else if(organizationRepository.getOrganizationById(organizationId) == null) {
            throw new NotFoundExceptionHandler("Organization not found");
        }

        organizationRepository.approveUserRequestJoin(userId, organizationId);

        return userAppApproveMapper.INSTANCE.toUserAppApproveDto(user);
    }

    @Override
    public UUID rejectUserRequestJoin(UUID userId, UUID organizationId) {

        UserApp user = userAppRepository.getUserByID(userId);

        if(user == null ) {
            throw new NotFoundExceptionHandler("User not found");
        }
        else if(organizationRepository.getOrganizationById(organizationId) == null) {
            throw new NotFoundExceptionHandler("Organization not found");
        }

        if(organizationDetailRepository.getExistUserInOrganization(userId, organizationId) == null) {
            throw new NotFoundExceptionHandler("User not found");
        }

        return organizationRepository.rejectUserRequestJoin(userId, organizationId, getCurrentUser());
    }

    @Override
    public List<OrganizationDTO> getAllUsersByOrgId(UUID orgId) {
        List<Organization> organizations = organizationRepository.getAllUsersByOrgId(orgId);

        if(organizations.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return organizationMapper.INSTANCE.toOrganizationDto(organizations);
    }

    @Override
    public List<OrganizationDTO> getAllRequestUsersByUserId(String search, String sort) {

        List<Organization> organizations = organizationRepository.getAllRequestUsersByUserId(search, sort, getCurrentUser());

        if(organizations.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return organizationMapper.INSTANCE.toOrganizationDto(organizations);
    }

    @Override
    public List<UserAppDTO> getAllJoinedUsersByOrgId(UUID orgId, String search, String sort) {

        List<UserApp> users = organizationRepository.getAllJoinedUsersByOrgId(orgId, search, sort, getCurrentUser());

        if(users.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return userAppMapper.INSTANCE.toUserAppDtos(users);
    }


    @Override
    public List<OrganizationDTO> getAllUserJoined(UUID orgId, String search, String sort) {
        List<Organization> organizations = organizationRepository.getAllUserJoined(orgId,search,sort, getCurrentUser());
        return organizationMapper.INSTANCE.toOrganizationDto(organizations);
    }

    @Override
    public Integer getCountUserJoined(UUID orgId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        return organizationRepository.getCountAllUserJoined(orgId);
    }

    @Override
    public List<UserAppDTO> getAllNotificationRequest(UUID orgId, Integer page, Integer size) {

        List<UserApp> users = organizationRepository.getAllNotificationRequest(orgId,page, size, getCurrentUser());

        if(users.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return userAppMapper.INSTANCE.toUserAppDtos(users);
    }

    @Override
    public List<OrganizationInvitedDTO> getAllInvitationFromOrganization(Integer page, Integer size, String search, String sort) {

        List<Organization> organizations = organizationRepository.getAllInvitationFromOrganization(page, size, search, sort, getCurrentUser());

        if(organizations.isEmpty()) {
            throw new NotFoundExceptionHandler("Data not found");
        }

        return organizationMapper.INSTANCE.tOrganizationInvitedDtos(organizations);
    }

    @Override
    public OrganizationInvitedDTO approveInvitation(UUID orgId) {

        UUID id = organizationDetailRepository.getOrganizationById(getCurrentUser(), orgId);

        if(id == null) {
            throw new NotFoundExceptionHandler("Data not found");
        }

        UUID organization_id = organizationDetailRepository.approveInvitation(getCurrentUser(), orgId);
        Organization org = organizationRepository.getOrganizationByOrgIdWithUserId(getCurrentUser(), organization_id);
        return organizationMapper.INSTANCE.toOrganizationInvitedDto(org);
    }

    @Override
    public OrganizationInvitedDTO rejectInvitation(UUID orgId) {
        UUID id = organizationDetailRepository.getOrganizationById(getCurrentUser(), orgId);

        if(id == null) {
            throw new NotFoundExceptionHandler("Data not found");
        }

        UUID organization_id = organizationDetailRepository.rejectInvitation(getCurrentUser(), orgId);
        Organization org = organizationRepository.getOrganizationByOrgIdWithUserId(getCurrentUser(), organization_id);
        return organizationMapper.INSTANCE.toOrganizationInvitedDto(org);
    }

    @Override
    public List<OrganizationDTO> getAllOrganizationsByOwner() {

        List<Organization> getAllOrganizations = organizationRepository.getAllOrganizationsByOwner(getCurrentUser());

        if(getAllOrganizations.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return organizationMapper.INSTANCE.toOrganizationDto(getAllOrganizations);
    }

    @Override
    public List<NotificationByOrgDTO> getAllNotificationsByOrgId(UUID orgId, Integer page, Integer size, String search, String sort) {

        List<UserApp> data = organizationRepository.getAllNotificationsByOrgId(orgId, page, size, search, sort);

        if(data.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return userAppMapper.INSTANCE.tNotificationByOrgDtos(data);
    }

    @Override
    public List<NotificationByUser> getAllNotificationByUser() {

        List<NotificationByUser> data = organizationRepository.getAllNotificationByUser(getCurrentUser());

        if(data.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }
        return data;
    }

    @Override
    public UUID removeMember(UUID userId, UUID orgId) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        else if(organizationDetailRepository.getExistUserInOrganization(userId, orgId) == null) {
            throw new NotFoundExceptionHandler("User not found");
        }

        return organizationDetailRepository.removeMember(userId, orgId, getCurrentUser());
    }
}
