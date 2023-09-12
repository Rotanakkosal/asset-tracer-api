package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.request.AssetRequest;
import com.kshrd.asset_tracer_api.model.request.AssetRequest2;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.repository.AssetRepository;
import com.kshrd.asset_tracer_api.service.AssetService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/assets")
public class AssetController {
    private final AssetService assetService;
    private final AssetRepository assetRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssetById(@PathVariable("id") UUID assetId) {
        return BodyResponse.getBodyResponse(assetService.getAssetById(assetId));
    }

    enum SortProperty {NAME, TYPE, STATUS, NORMAL_CATEGORY}
    @GetMapping("/track/{orgId}")
    public ResponseEntity<?> getAllAssetList(@PathVariable("orgId") UUID orgId,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer size,
                                             @RequestParam(required = false) String assetName,
                                             @RequestParam(required = false) String status,
                                             @RequestParam(required = false) String roomName,
                                             @RequestParam(required = false) String normalCategoryName,
                                             @RequestParam(required = false) String sort) {

        Integer countData = assetRepository.getCountAssets(orgId, assetName, status, roomName, normalCategoryName);
        return BodyResponse.getBodyResponse(assetService.getAllAssets(orgId, page, size, assetName, status, roomName, normalCategoryName, sort), countData);
    }


    @PostMapping
    public ResponseEntity<?> addAsset(@RequestBody AssetRequest assetRequest) {
        UUID addId = assetService.addAsset(assetRequest);
        return BodyResponse.getBodyResponse(assetService.getAssetById(addId));
    }

    @PostMapping("/multiple")
    public ResponseEntity<?> addMultipleAsset(@RequestBody List<AssetRequest> assetRequests) {
        return BodyResponse.getBodyResponse(assetService.addMultipleAsset(assetRequests));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAsset(@RequestBody AssetRequest2 assetRequest2, @PathVariable("id") UUID assetId) {
        return BodyResponse.getBodyResponse(assetService.getAssetById(assetService.updateAsset(assetRequest2, assetId)));
    }

    @DeleteMapping("/{assetId}/{organizationId}")
    public ResponseEntity<?> deleteAsset(@PathVariable UUID assetId, @PathVariable UUID organizationId) {
        return BodyResponse.getBodyResponse(assetService.deleteAsset(assetId, organizationId));
    }

    @GetMapping("/{orgId}/get-all")
    public ResponseEntity<?> getAllAssetsByOrgId(@PathVariable UUID orgId){
        return BodyResponse.getBodyResponse(assetService.getAllAssetsByOrgId(orgId), assetRepository.getCountData(orgId));
    }

    @GetMapping("/{orgId}/get-last-three")
    public ResponseEntity<?> getLastThreeAssets(@PathVariable UUID orgId,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "3") Integer size){
        return BodyResponse.getBodyResponse(assetService.getLastThreeAssets(orgId, page, size), assetRepository.getCountData(orgId));
    }

    @PutMapping("/status/{assetId}/{status}")
    public ResponseEntity<?> changeStatus(@PathVariable UUID assetId, @PathVariable String status) {
        return BodyResponse.getBodyResponse(assetService.changeStatus(assetId, status));
    }
}
