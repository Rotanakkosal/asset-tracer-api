package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.exception.*;
import com.kshrd.asset_tracer_api.model.dto.NormalCategoryDTO;
import com.kshrd.asset_tracer_api.model.dto.NormalCategoryNameDTO;
import com.kshrd.asset_tracer_api.model.entity.NormalCategory;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.mapper.NormalCategoryMapper;
import com.kshrd.asset_tracer_api.model.request.NormalCategoryRequest;
import com.kshrd.asset_tracer_api.repository.NormalCategoryRepository;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.repository.OrganizationRepository;
import com.kshrd.asset_tracer_api.service.NormalCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NormalCategoryServiceImpl implements NormalCategoryService {

    private final NormalCategoryRepository normalCategoryRepository;
    private final NormalCategoryMapper normalCategoryMapper;
    private final OrganizationDetailRepository organizationDetailRepository;
    private final OrganizationRepository organizationRepository;
    List<NormalCategory> normalCategories;
    private UUID getCurrentUser() {

        Object getContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(getContext.equals("anonymousUser")) {
            throw new UnauthorizedExceptionHandler("Unauthorized User");
        }

        UserApp user = (UserApp) getContext;
        return user.getId();
    }

    @Override
    public List<NormalCategoryDTO> getAllNormalCategories(UUID orgId, Integer page, Integer size, String name, String sort) {

        normalCategories = normalCategoryRepository.getAllNormalCategories(orgId, page, size, name, sort);

        return normalCategoryMapper.INSTANCE.toNormalCategoryDto(normalCategories);
    }

    @Override
    public List<NormalCategoryNameDTO> getAllNormalCategoryName(UUID orgId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        normalCategories = normalCategoryRepository.getAllNormalCategoriesByOrg(orgId);

        if(normalCategories.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }
        return normalCategoryMapper.INSTANCE.toNormalCategoryNameDtos(normalCategories);
    }

    @Override
    public NormalCategoryDTO getNormalCategoryById(UUID normalCategoryId) {

        NormalCategory normalCategory = normalCategoryRepository.getNormalCategoryById(normalCategoryId);

        if (normalCategory == null) {
            throw new NotFoundExceptionHandler("Normal category not found");
        }

        return normalCategoryMapper.INSTANCE.toNormalCategoryDto(normalCategory);
    }

    @Override
    public UUID addNormalCategory(NormalCategoryRequest normalCategoryRequest) {

        var organizationId = normalCategoryRequest.getOrganizationId();

        if(organizationRepository.getOrganizationById(normalCategoryRequest.getOrganizationId()) == null) {
            throw new NotFoundExceptionHandler("Organization not found");
        }

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(roleName == null || !roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }
        else if (normalCategoryRequest.getName().isEmpty()) {
            throw new FieldEmptyExceptionHandler("Name field is empty");
        }
        else if (normalCategoryRequest.getName().isBlank()) {
            throw new FieldBlankExceptionHandler("Name field is blank");
        }
        else if(normalCategoryRepository.isExistCategoryName(normalCategoryRequest.getName(), normalCategoryRequest.getOrganizationId())) {
            throw new DataDuplicateExceptionHandler("Duplicate Normal Category Name");
        }

        return normalCategoryRepository.addNormalCategory(normalCategoryRequest, getCurrentUser());
    }

    @Override
    public UUID updateNormalCategory(UUID normalCategoryId, NormalCategoryRequest normalCategoryRequest) {
        var organizationId = normalCategoryRequest.getOrganizationId();
        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(roleName == null || !roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }
        else if (normalCategoryRequest.getName().isEmpty()) {
            throw new FieldEmptyExceptionHandler("Name field is empty");
        }
        else if (normalCategoryRequest.getName().isBlank()) {
            throw new FieldBlankExceptionHandler("Name field is blank");
        }
        else if (normalCategoryRepository.getNormalCategoryById(normalCategoryId) == null) {
            throw new NotFoundExceptionHandler("Normal category not found");
        }


        return normalCategoryRepository.updateNormalCategory(normalCategoryRequest, normalCategoryId, getCurrentUser());
    }

    @Override
    public UUID deleteNormalCategory(UUID normalCategoryId, UUID organizationId) {

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(roleName == null || !roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }
        else if (normalCategoryRepository.getNormalCategoryById(normalCategoryId) == null ) {
            throw new NotFoundExceptionHandler("You're deleting id is not found.");
        }

        return normalCategoryRepository.deleteNormalCategory(normalCategoryId, getCurrentUser());
    }
}
