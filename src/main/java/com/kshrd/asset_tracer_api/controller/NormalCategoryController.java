package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.request.NormalCategoryRequest;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.repository.NormalCategoryRepository;
import com.kshrd.asset_tracer_api.service.NormalCategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("api/v1/normal-categories")
public class NormalCategoryController {
    private final NormalCategoryService normalCategoryService;
    private final NormalCategoryRepository normalCategoryRepository;

    @GetMapping("/get-all/{orgId}")
    public ResponseEntity<?> getAllNormalCategories(@PathVariable UUID orgId,
                                                    @RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer size,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String sort) {
        Integer countData = normalCategoryRepository.getCountAllNormalCategories(orgId, name, sort);
        return BodyResponse.getBodyResponse(normalCategoryService.getAllNormalCategories(orgId, page, size, name, sort), countData);
    }

    @GetMapping("/name/{orgId}")
    public ResponseEntity<?> getAllNormalCategoriesByOrg(@PathVariable("orgId") UUID orgId) {
        return BodyResponse.getBodyResponse(normalCategoryService.getAllNormalCategoryName(orgId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNormalCategoryById(@PathVariable("id") UUID normalCategoryId) {
        return BodyResponse.getBodyResponse(normalCategoryService.getNormalCategoryById(normalCategoryId));
    }


    @PostMapping
    public ResponseEntity<?> addNormalCategory(@RequestBody NormalCategoryRequest normalCategoryRequest) {
        UUID getId = normalCategoryService.addNormalCategory(normalCategoryRequest);
        return BodyResponse.getBodyResponse(normalCategoryService.getNormalCategoryById(getId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateNormalCategory(@PathVariable("id") UUID normalCategoryId, @RequestBody NormalCategoryRequest normalCategoryRequest) {
        normalCategoryService.updateNormalCategory(normalCategoryId, normalCategoryRequest);
        return BodyResponse.getBodyResponse(normalCategoryService.getNormalCategoryById(normalCategoryId));
    }

    @DeleteMapping("/{normalCategoryId}/{organizationId}")
    public ResponseEntity<?> deleteNormalCategory(@PathVariable UUID normalCategoryId,
                                                  @PathVariable UUID organizationId) {
        return BodyResponse.getBodyResponse(normalCategoryService.deleteNormalCategory(normalCategoryId, organizationId));
    }
}
