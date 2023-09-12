package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.ItemDTO;
import com.kshrd.asset_tracer_api.model.dto.ItemDetailAllDTO;
import com.kshrd.asset_tracer_api.model.dto.ItemDetailDTO;
import com.kshrd.asset_tracer_api.model.entity.ItemDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemDetailMapper {
    ItemDetailMapper INSTANCE = Mappers.getMapper(ItemDetailMapper.class);
    ItemDetailDTO toItemDetailDto (ItemDetail itemDetail);
    List<ItemDetailDTO> toItemDetailDtos (List<ItemDetail> itemDetails);
    List<ItemDetailAllDTO> toItemDetailAllDtos (List<ItemDetail> itemDetails);
    ItemDTO toItemDto(ItemDetail itemDetail);
}
