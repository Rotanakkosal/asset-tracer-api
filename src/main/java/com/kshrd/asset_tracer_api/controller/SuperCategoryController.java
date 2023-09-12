package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.request.SuperCategoryRequest;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.repository.SuperCategoryRepository;
import com.kshrd.asset_tracer_api.service.SuperCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/super-categories")
@SecurityRequirement(name = "bearerAuth")
public class SuperCategoryController {
    private final SuperCategoryService superCategoryService;
    private final SuperCategoryRepository superCategoryRepository;

    @GetMapping("/by-organization/{orgId}")
    public ResponseEntity<?> getAllSuperCategories(
            @PathVariable UUID orgId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sort) {
        Integer countData = Integer.valueOf(superCategoryRepository.getCountAllSuperCategories(orgId, name));
        return BodyResponse.getBodyResponse(superCategoryService.getAllSuperCategories(orgId, page, size, name, sort), countData);
    }

    @GetMapping("/name/{orgId}")
    public ResponseEntity<?> getAllSuperCategoryNames(@PathVariable UUID orgId) {
        return BodyResponse.getBodyResponse(superCategoryService.getAllSuperCategoryNames(orgId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSuperCategoryById(@PathVariable("id") UUID superCategoryId) {
        return BodyResponse.getBodyResponse(superCategoryService.getSuperCategoryById(superCategoryId));
    }

    @PostMapping
    public ResponseEntity<?> addSuperCategory(@RequestBody SuperCategoryRequest superCategoryRequest) {
        UUID getId = superCategoryService.addSuperCategory(superCategoryRequest);
        return BodyResponse.getBodyResponse(superCategoryService.getSuperCategoryById(getId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSuperCategory(@RequestBody SuperCategoryRequest superCategoryRequest,
                                                 @PathVariable("id") UUID superCategoryId) {
        superCategoryService.updateSuperCategory(superCategoryRequest, superCategoryId);
        return BodyResponse.getBodyResponse(superCategoryService.getSuperCategoryById(superCategoryId));
    }

    @DeleteMapping("/{superCategoryId}/{orgId}")
    public ResponseEntity<?> deleteSuperCategory(@PathVariable UUID superCategoryId, @PathVariable UUID orgId) {
        return BodyResponse.getBodyResponse(superCategoryService.deleteSuperCategory(superCategoryId, orgId));
    }

    @GetMapping("/count-asset/{orgId}")
    public ResponseEntity<?> getCountAssetBySuperCategory(@PathVariable("orgId") UUID orgId) {
        return BodyResponse.getBodyResponse(superCategoryService.getCountAssetBySuperCategory(orgId));
    }
}
