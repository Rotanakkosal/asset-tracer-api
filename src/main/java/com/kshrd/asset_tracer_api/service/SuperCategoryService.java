package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.dto.CountAssetByStatusDTO;
import com.kshrd.asset_tracer_api.model.dto.SuperCategoryDTO;
import com.kshrd.asset_tracer_api.model.dto.SuperCategoryNameDTO;
import com.kshrd.asset_tracer_api.model.entity.SuperCategory;
import com.kshrd.asset_tracer_api.model.request.SuperCategoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface SuperCategoryService {
    List<SuperCategoryDTO> getAllSuperCategories(UUID orgId, Integer page, Integer size, String name, String sort);
    List<SuperCategoryNameDTO> getAllSuperCategoryNames(UUID orgId);
    SuperCategoryDTO getSuperCategoryById(UUID superCategoryId);
    UUID addSuperCategory(SuperCategoryRequest superCategoryRequest);
    UUID updateSuperCategory(SuperCategoryRequest superCategoryRequest, UUID superCategoryId);
    UUID deleteSuperCategory(UUID superCategoryId, UUID orgId);
    List<SuperCategoryNameDTO> getAllAssetInSuperCategory(UUID orgId);
    List<CountAssetByStatusDTO> getCountAssetBySuperCategory(UUID orgId);
}
