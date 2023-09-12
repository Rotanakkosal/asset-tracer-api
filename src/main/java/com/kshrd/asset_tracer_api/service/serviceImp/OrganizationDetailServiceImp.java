package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.exception.FieldBlankExceptionHandler;
import com.kshrd.asset_tracer_api.exception.FieldEmptyExceptionHandler;
import com.kshrd.asset_tracer_api.exception.NotFoundExceptionHandler;
import com.kshrd.asset_tracer_api.exception.UnauthorizedExceptionHandler;
import com.kshrd.asset_tracer_api.model.dto.OrganizationDetailDTO;
import com.kshrd.asset_tracer_api.model.entity.Organization;
import com.kshrd.asset_tracer_api.model.entity.OrganizationDetail;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.mapper.OrganizationDetailMapper;
import com.kshrd.asset_tracer_api.model.request.OrganizationDetailRequest;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.repository.OrganizationRepository;
import com.kshrd.asset_tracer_api.repository.UserAppRepository;
import com.kshrd.asset_tracer_api.service.OrganizationDetailService;
import com.kshrd.asset_tracer_api.service.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationDetailServiceImp implements OrganizationDetailService {

    private final OrganizationDetailRepository organizationDetailRepository;
    private final OrganizationRepository organizationRepository;
    private final UserAppRepository userAppRepository;
    private OrganizationDetailMapper organizationDetailMapper;

    private UUID getCurrentUser() {

        Object getContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(getContext.equals("anonymousUser")) {
            throw new UnauthorizedExceptionHandler("Unauthorized User");
        }

        UserApp user = (UserApp) getContext;
        return user.getId();
    }

    @Override
    public OrganizationDetailDTO addNewOrganizationDetail(OrganizationDetailRequest organizationDetailRequest) {

        if(userAppRepository.getUserByID(organizationDetailRequest.getUserId()) == null) {
            throw new NotFoundExceptionHandler("User not found");
        }
        else if (organizationDetailRequest.getOrganizationName().isEmpty()) {
            throw new FieldEmptyExceptionHandler("Organization name field is empty");
        }
        else if (organizationDetailRequest.getOrganizationName().isBlank()) {
            throw new FieldBlankExceptionHandler("Organization name field is blank");
        }

        Random random;
        String code;

        while (true) {
            random = new Random();
            code = String.format("%06d", random.nextInt(999999));

            if(organizationRepository.getOrganizationByCode(code) == null) {
                break;
            }
        }

        UUID id = organizationDetailRepository.addOrganization(organizationDetailRequest, code);
        OrganizationDetail organizationDetail = organizationDetailRepository.userAddNewOrganizationDetail(organizationDetailRequest.getUserId(), id);

        System.out.println(organizationDetail);
        return organizationDetailMapper.INSTANCE.toOrganizationDetailDto(organizationDetail);
    }

    @Override
    public OrganizationDetailDTO getOrganizationDetail(UUID userId, UUID organizationId) {
        return null;
    }

    @Override
    public List<OrganizationDetailDTO> getAllOrganizationsDetailByUserId(UUID userId) {

        List<OrganizationDetail> organizationDetails = organizationDetailRepository.getAllOrganizationsDetailByUserId(userId);

        if(organizationDetails.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return organizationDetailMapper.INSTANCE.toOrganizationDetailDtos(organizationDetails);
    }

    @Override
    public List<OrganizationDetailDTO> getAllRequestUsers(UUID orgId) {

        List<OrganizationDetail> organizationDetails = organizationDetailRepository.getAllRequestUsers(orgId);

        if(organizationDetails.isEmpty()){
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return organizationDetailMapper.INSTANCE.toOrganizationDetailDtos(organizationDetails);
    }

    @Override
    public List<OrganizationDetailDTO> getAllJoinedUsers(UUID organizationId) {

        List<OrganizationDetail> organizationDetails = organizationDetailRepository.getAllJoinedUsers(organizationId);

        if(organizationDetails.isEmpty()){
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return organizationDetailMapper.INSTANCE.toOrganizationDetailDtos(organizationDetails);
    }

    @Override
    public UUID deleteUserFromOrganization(UUID userId, UUID orgId) {

        UUID id = organizationDetailRepository.getExistUserInOrganization(userId, orgId);

        if(id == null) {
            throw new NotFoundExceptionHandler("Data not found");
        }

        return organizationDetailRepository.deleteUserFromOrganization(userId, orgId, getCurrentUser());
    }
}
