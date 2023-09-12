package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.dto.NormalCategoryDTO;
import com.kshrd.asset_tracer_api.model.dto.NormalCategoryNameDTO;
import com.kshrd.asset_tracer_api.model.request.NormalCategoryRequest;

import java.util.List;
import java.util.UUID;

public interface NormalCategoryService {
    List<NormalCategoryDTO> getAllNormalCategories(UUID orgId, Integer page, Integer size, String name, String sort);
    List<NormalCategoryNameDTO> getAllNormalCategoryName(UUID orgId);
    NormalCategoryDTO getNormalCategoryById(UUID normalCategoryId);
    UUID addNormalCategory(NormalCategoryRequest normalCategoryRequest);
    UUID updateNormalCategory(UUID normalCategoryId, NormalCategoryRequest normalCategoryRequest);
    UUID deleteNormalCategory(UUID normalCategoryId, UUID organizationId);
}
