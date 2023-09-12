package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.dto.ItemDTO;
import com.kshrd.asset_tracer_api.model.dto.ItemDetailAllDTO;
import com.kshrd.asset_tracer_api.model.dto.ItemDetailDTO;
import com.kshrd.asset_tracer_api.model.request.ItemDetailRequest;
import com.kshrd.asset_tracer_api.model.request.ItemDetailUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ItemDetailService {

    ItemDetailDTO getItemDetailById(UUID itemDetailId);
    UUID addItemDetail(ItemDetailRequest itemDetailRequest);
    UUID updateItemDetail(ItemDetailUpdateRequest itemDetailUpdateRequest, UUID itemDetailId);
    List<ItemDetailDTO> getAllItemsDetail(UUID orgId, Integer page, Integer size, String name, String normalCategoryName, String sort);
    UUID deleteItemById(UUID itemId, UUID organizationId);
    List<ItemDetailAllDTO> getAllItemDetailByOrganizationId(UUID orgId);
    ItemDTO getItemById(UUID itemDetailId, UUID orgId);
}
