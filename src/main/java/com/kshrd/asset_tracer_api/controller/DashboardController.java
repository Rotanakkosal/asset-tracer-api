package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.repository.AssetRepository;
import com.kshrd.asset_tracer_api.repository.InvoiceRepository;
import com.kshrd.asset_tracer_api.repository.OrganizationRepository;
import com.kshrd.asset_tracer_api.repository.SuperCategoryRepository;
import com.kshrd.asset_tracer_api.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final AssetService assetService;
    private final OrganizationService organizationService;
    private final RoomService roomService;
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;
    private final OrganizationRepository organizationRepository;
    private final SuperCategoryService superCategoryService;
    @GetMapping("/{orgId}/count-all-asset")
    @Operation(summary = "In an organization")
    public ResponseEntity<?> getCountAllAssets(@PathVariable("orgId")UUID orgId){
        return BodyResponse.getBodyResponse(assetService.getCountAllAssets(orgId));
    }

    @GetMapping("/{orgId}/count-user-joined")
    public ResponseEntity<?> getCountUserJoined(@PathVariable("orgId") UUID orgId){
        return BodyResponse.getBodyResponse(organizationService.getCountUserJoined(orgId));
    }

    @GetMapping("/count-asset-by-status/{orgId}")
    public ResponseEntity<?> getAllAssetByStatus(@PathVariable UUID orgId, @RequestParam(required = false) String status){
        return BodyResponse.getBodyResponse(assetService.getAllAssetByStatus(orgId, status));
    }

    @GetMapping("/count-all-rooms/{orgId}")
    public ResponseEntity<?> getAllRooms(@PathVariable("orgId") UUID orgId){
        return BodyResponse.getBodyResponse(roomService.getCountAllRooms(orgId));
    }

    @GetMapping("/{orgId}/{superId}/count-asset")
    @Operation(summary = "In a super category")
    public ResponseEntity<?> getAllAssetBySuperCategory(@PathVariable("orgId") UUID orgId,
                                                        @PathVariable("superId") UUID superId){
        return BodyResponse.getBodyResponse(assetService.getAllAssetBySuperCategory(orgId, superId));
    }
    @GetMapping("/{normalId}/{orgId}/count-all-asset")
    @Operation(summary = "In a normal category")
    public ResponseEntity<?> getAllAssetByNormalCategory(@PathVariable("normalId") UUID normalId,
                                                         @PathVariable("orgId") UUID orgId){
        return BodyResponse.getBodyResponse(assetService.getAllAssetByNormalCategory(normalId, orgId));
    }

    @GetMapping("/{orgId}/get-all-invoice")
    public ResponseEntity<?> getAllInvoiceByOrgId(@PathVariable("orgId") UUID orgId){
        return BodyResponse.getBodyResponse(invoiceService.getAllInvoiceByOrgId(orgId));
    }

    @GetMapping("/{orgId}/last-three-invoice")
    public ResponseEntity<?> getLastThreeInvoices(@PathVariable("orgId") UUID orgId,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "3") Integer size) {
        return BodyResponse.getBodyResponse(invoiceService.getLastThreeInvoices(orgId, page, size), invoiceRepository.getCountData());
    }

    @GetMapping("/{orgId}/get-all-notification-request")
    @Operation(summary = "list notifications request")
    public ResponseEntity<?> getAllNotificationRequest(@PathVariable("orgId") UUID orgId,
                                                       @RequestParam (required = false) Integer page,
                                                       @RequestParam (required = false) Integer size){
        Integer countUser = organizationRepository.getCountUserRequest(orgId);
        return BodyResponse.getBodyResponse(organizationService.getAllNotificationRequest(orgId, page, size), countUser);
    }

    @GetMapping("/{orgId}/list-super-category")
    @Operation(summary = "count all assets in an organization by super category")
    public ResponseEntity<?> getAllAssetInSuperCategory(@PathVariable("orgId") UUID orgId){
        return BodyResponse.getBodyResponse(superCategoryService.getAllAssetInSuperCategory(orgId));
    }

    @GetMapping("/count-asset/{orgId}")
    public ResponseEntity<?> getCountAssetBySuperCategory(@PathVariable("orgId") UUID orgId){
        return BodyResponse.getBodyResponse(superCategoryService.getCountAssetBySuperCategory(orgId), 1);
    }
}

