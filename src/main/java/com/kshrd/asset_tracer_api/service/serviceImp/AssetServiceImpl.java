package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.exception.*;
import com.kshrd.asset_tracer_api.model.dto.AssetDTO;
import com.kshrd.asset_tracer_api.model.entity.Asset;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.mapper.AssetMapper;
import com.kshrd.asset_tracer_api.model.request.AssetRequest;
import com.kshrd.asset_tracer_api.model.request.AssetRequest2;
import com.kshrd.asset_tracer_api.repository.AssetRepository;
import com.kshrd.asset_tracer_api.repository.ItemDetailRepository;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final ItemDetailRepository itemDetailRepository;
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
    public AssetDTO getAssetById(UUID assetId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        Asset asset = assetRepository.getAssetById(assetId);

        if(asset == null){
            throw new NotFoundExceptionHandler("Asset not found");
        }

        return assetMapper.toAssetDto(asset);
    }

    @Override
    public UUID addAsset(AssetRequest assetRequest) {

        var organizationId = assetRequest.getOrganizationId();
        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(roleName == null || !roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }

        if(assetRepository.isDuplicateLabelCode(assetRequest.getLabelCode(), organizationId)) {
            throw new DataDuplicateExceptionHandler("Duplication Label Code");
        }
        else if(assetRepository.isDuplicateSerialCode(assetRequest.getSerialCode(), organizationId)) {
            throw new DataDuplicateExceptionHandler("Duplication Serial Code");
        }

        return assetRepository.addAsset(assetRequest, getCurrentUser());
    }

    @Override
    public List<AssetDTO> addMultipleAsset(List<AssetRequest> assetRequests) {

        UUID organizationId = null;
        String roleName = null;

        for(AssetRequest assetRequest : assetRequests) {
            if(assetRequest.getLabelCode().isBlank()
                    || assetRequest.getLabelCode().isEmpty()) {
                assetRequest.setLabelCode(null);
            }
            else if(assetRequest.getSerialCode().isBlank()
                    || assetRequest.getSerialCode().isEmpty()) {
                assetRequest.setSerialCode(null);
            }
            else if(assetRepository.isDuplicateLabelCode(assetRequest.getLabelCode(), assetRequest.getOrganizationId())) {
                throw new DataDuplicateExceptionHandler("Duplication Label Code");
            }
            else if(assetRepository.isDuplicateSerialCode(assetRequest.getSerialCode(), assetRequest.getOrganizationId())) {
                throw new DataDuplicateExceptionHandler("Duplication Serial Code");
            }
        }

        List<Asset> assets = new ArrayList<>();

        for(AssetRequest assetRequest : assetRequests) {
           UUID id = assetRepository.addAsset(assetRequest, getCurrentUser());
           assets.add(assetRepository.getAssetById(id));
        }


        System.out.println(assets);
        return assetMapper.INSTANCE.toAssetDtos(assets);
    }


    @Override
    public UUID updateAsset(AssetRequest2 assetRequest2, UUID assetId) {

        var organizationId = assetRequest2.getOrganizationId();

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(roleName == null || !roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }

        else if(assetRepository.getAssetById(assetId) == null){
            throw new NotFoundExceptionHandler("Asset not found");
        }
        return assetRepository.updateAsset(assetRequest2, assetId, getCurrentUser());
    }

    @Override
    public List<AssetDTO> getAllAssets(UUID orgId, Integer page, Integer size, String assetName, String status, String roomName, String normalCategoryName, String sort) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        List<Asset> assets = assetRepository.getAllAssets(orgId, page, size, assetName, status, roomName, normalCategoryName, sort);

        if(assets.isEmpty()){
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return assetMapper.INSTANCE.toAssetDtos(assets);
    }

    @Override
    public UUID deleteAsset(UUID assetId, UUID organizationId) {

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(!roleName.equals("ADMIN")) {
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }
        else if (assetRepository.getAssetById(assetId) == null) {
            throw new NotFoundExceptionHandler("You're deleting id is not found.");
        }

        return assetRepository.deleteAsset(assetId);
    }

    @Override
    public List<AssetDTO> getAllAssetsByOrgId(UUID orgId) {
        if(getCurrentUser() == null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        List<Asset> assets = assetRepository.getAllAssetsByOrgId(orgId);

        return assetMapper.INSTANCE.toAssetDtos(assets);
    }

    @Override
    public List<AssetDTO> getLastThreeAssets(UUID orgId, Integer page, Integer size) {

        if(getCurrentUser() == null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        List<Asset> assets = assetRepository.getLastThreeAssets(orgId, page, size);

        return assetMapper.INSTANCE.toAssetDtos(assets);
    }

    @Override
    public Integer getAllAssetByStatus(UUID orgId, String status) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        return assetRepository.getCountAssetByStatus(orgId, status);
    }

    @Override
    public Integer getCountAllAssets(UUID orgId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        Integer data = assetRepository.getCountAllAssets(orgId);
        if(data == null){
            throw new NotFoundExceptionHandler("Data is empty");
        }
        return assetRepository.getCountAllAssets(orgId);
    }

    @Override
    public Integer getAllAssetBySuperCategory(UUID orgId, UUID superId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        Integer data = assetRepository.getAllAssetBySuperCategory(orgId, superId);
        if(data == null){
            throw new NotFoundExceptionHandler("Data is empty");
        }
        return data;
    }

    @Override
    public Integer getAllAssetByNormalCategory(UUID normalId, UUID orgId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        Integer data = assetRepository.getAllAssetByNormalCategory(normalId, orgId);
        if(data == null){
            throw new NotFoundExceptionHandler("Data is empty");
        }
        return data;
    }

    @Override
    public AssetDTO changeStatus(UUID assetId, String status) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        else if(assetRepository.getAssetById(assetId) == null) {
            throw new NotFoundExceptionHandler("Asset not found");
        }

        Asset asset = assetRepository.changeStatus(assetId, status);

        return assetMapper.INSTANCE.toAssetDto(asset);
    }
}
