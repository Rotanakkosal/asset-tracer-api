package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.CountAssetByStatusDTO;
import com.kshrd.asset_tracer_api.model.dto.SuperCategoryDTO;
import com.kshrd.asset_tracer_api.model.dto.SuperCategoryNameDTO;
import com.kshrd.asset_tracer_api.model.entity.SuperCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SuperCategoryMapper {
    SuperCategoryMapper INSTANCE = Mappers.getMapper(SuperCategoryMapper.class);
    SuperCategoryDTO toSuperCategoryDto(SuperCategory superCategory);
    List<SuperCategoryDTO> toSuperCategoryDto(List<SuperCategory> superCategories);
    List<SuperCategoryNameDTO> toSuperCategoryNameDtos(List<SuperCategory> superCategories);
    List<CountAssetByStatusDTO> toCountAssetByStatusDtos(List<SuperCategory> superCategories);
}
