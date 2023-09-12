package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.AssetDTO;
import com.kshrd.asset_tracer_api.model.dto.ItemDetailDTO;
import com.kshrd.asset_tracer_api.model.entity.Asset;
import com.kshrd.asset_tracer_api.model.entity.ItemDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring")
public interface AssetMapper {
    AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);
    AssetDTO toAssetDto(Asset asset);
    List<AssetDTO> toAssetDtos(List<Asset> assets);
}