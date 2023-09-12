package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.dto.AssetDTO;
import com.kshrd.asset_tracer_api.model.dto.SuperCategoryNameDTO;
import com.kshrd.asset_tracer_api.model.request.AssetRequest;
import com.kshrd.asset_tracer_api.model.request.AssetRequest2;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

public interface AssetService {
    AssetDTO getAssetById(UUID assetId);
    UUID addAsset(AssetRequest assetRequest);
    List<AssetDTO> addMultipleAsset(List<AssetRequest> assetRequests);
    UUID updateAsset(AssetRequest2 assetRequest2, UUID assetId);
    List<AssetDTO> getAllAssets(UUID orgId, Integer page, Integer size, String assetName, String status, String roomName, String normalCategoryName, String sort);
    UUID deleteAsset(UUID assetId, UUID organizationId);
    List<AssetDTO> getAllAssetsByOrgId(UUID orgId);
    List<AssetDTO> getLastThreeAssets(UUID orgId, Integer page, Integer size);
    Integer getAllAssetByStatus(UUID orgId, String status);
    Integer getCountAllAssets(UUID orgId);
    Integer getAllAssetBySuperCategory(UUID orgId, UUID superId);
    Integer getAllAssetByNormalCategory(UUID normalId, UUID orgId);
    AssetDTO changeStatus(UUID assetId, String status);
}
