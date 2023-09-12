package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.exception.*;
import com.kshrd.asset_tracer_api.model.dto.CountAssetByStatusDTO;
import com.kshrd.asset_tracer_api.model.dto.SuperCategoryDTO;
import com.kshrd.asset_tracer_api.model.dto.SuperCategoryNameDTO;
import com.kshrd.asset_tracer_api.model.entity.SuperCategory;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.mapper.SuperCategoryMapper;
import com.kshrd.asset_tracer_api.model.request.SuperCategoryRequest;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.repository.SuperCategoryRepository;
import com.kshrd.asset_tracer_api.service.SuperCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuperCategoryServiceImpl implements SuperCategoryService {

    private SuperCategoryMapper superCategoryMapper;
    private final SuperCategoryRepository superCategoryRepository;
    List<SuperCategory> superCategories;
    private final OrganizationDetailRepository organizationDetailRepository;

    private UUID getCurrentUser() {

        Object getContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(getContext.equals("anonymousUser")) {
            throw new UnauthorizedExceptionHandler("Unauthorized User");
        }

        UserApp user = (UserApp) getContext;
        return user.getId();
    }


    @Override
    public List<SuperCategoryDTO> getAllSuperCategories(UUID orgId, Integer page, Integer size, String name, String sort) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        superCategories = superCategoryRepository.getAllSuperCategories(orgId, page, size, name, sort);

        return superCategoryMapper.INSTANCE.toSuperCategoryDto(superCategories);
    }

    @Override
    public List<SuperCategoryNameDTO> getAllSuperCategoryNames(UUID orgId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        superCategories = superCategoryRepository.getAllSuperCategoryNames(orgId);

        return superCategoryMapper.INSTANCE.toSuperCategoryNameDtos(superCategories);
    }

    @Override
    public SuperCategoryDTO getSuperCategoryById(UUID superCategoryId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        SuperCategory superCategory = superCategoryRepository.getSuperCategoryById(superCategoryId);

        if (superCategory == null) {
            throw new NotFoundExceptionHandler("Super category not found");
        }

        return superCategoryMapper.INSTANCE.toSuperCategoryDto(superCategory);
    }

    @Override
    public UUID addSuperCategory(SuperCategoryRequest superCategoryRequest) {

        var organizationId = superCategoryRequest.getOrganizationId();

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(roleName == null || !roleName.equals("ADMIN")) {
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }
        else if (superCategoryRequest.getName().isEmpty()) {
            throw new FieldEmptyExceptionHandler("Name field is empty");
        }
        else if (superCategoryRequest.getName().isBlank()) {
            throw new FieldBlankExceptionHandler("Name field is blank");
        }
        else if(superCategoryRepository.isExistCategoryName(superCategoryRequest.getName(), superCategoryRequest.getOrganizationId())) {
            throw new DataDuplicateExceptionHandler("Duplicate Super Category Name");
        }

        return superCategoryRepository.addSuperCategory(superCategoryRequest, organizationId, getCurrentUser());
    }

    @Override
    public UUID updateSuperCategory(SuperCategoryRequest superCategoryRequest, UUID superCategoryId) {

        var organizationId = superCategoryRequest.getOrganizationId();

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(roleName == null || !roleName.equals("ADMIN")) {
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }
        else if (superCategoryRepository.getSuperCategoryById(superCategoryId) == null) {
            throw new NotFoundExceptionHandler("Super category not found");
        }
        else if (superCategoryRequest.getName().isEmpty()) {
            throw new FieldEmptyExceptionHandler("Name field is empty");
        }
        else if (superCategoryRequest.getName().isBlank()) {
            throw new FieldBlankExceptionHandler("Name field is blank");
        }

        return superCategoryRepository.updateSuperCategory(superCategoryRequest, superCategoryId, getCurrentUser());
    }

    @Override
    public UUID deleteSuperCategory(UUID superCategoryId, UUID orgId) {

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), orgId);

        if(roleName == null || !roleName.equals("ADMIN")) {
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }

        if(getCurrentUser() == null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        else if (superCategoryRepository.getSuperCategoryById(superCategoryId) == null) {
            throw new NotFoundExceptionHandler("Super category not found");
        }

        return superCategoryRepository.deleteSuperCategory(superCategoryId, getCurrentUser());
    }

    @Override
    public List<SuperCategoryNameDTO> getAllAssetInSuperCategory(UUID orgId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        superCategories = superCategoryRepository.getAllAssetInSuperCategory(orgId);
        return superCategoryMapper.INSTANCE.toSuperCategoryNameDtos(superCategories);
    }

    @Override
    public List<CountAssetByStatusDTO> getCountAssetBySuperCategory(UUID orgId) {

        List<SuperCategory> getData = superCategoryRepository.getCountAssetBySuperCategory(orgId);

        if(getData == null) {
            throw new NotFoundExceptionHandler("Data not found");
        }

        return superCategoryMapper.INSTANCE.toCountAssetByStatusDtos(getData);
    }
}
