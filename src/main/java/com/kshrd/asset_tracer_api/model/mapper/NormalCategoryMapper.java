package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.NormalCategoryDTO;
import com.kshrd.asset_tracer_api.model.dto.NormalCategoryNameDTO;
import com.kshrd.asset_tracer_api.model.entity.NormalCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NormalCategoryMapper {
    NormalCategoryMapper INSTANCE = Mappers.getMapper(NormalCategoryMapper.class);
    NormalCategoryDTO toNormalCategoryDto (NormalCategory normalCategory);
    List<NormalCategoryDTO> toNormalCategoryDto (List<NormalCategory> normalCategories);
    List<NormalCategoryNameDTO> toNormalCategoryNameDtos (List<NormalCategory> normalCategories);
}
