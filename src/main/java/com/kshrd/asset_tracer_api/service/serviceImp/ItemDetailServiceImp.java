package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.exception.ForbiddenExceptionHandler;
import com.kshrd.asset_tracer_api.exception.NotFoundExceptionHandler;
import com.kshrd.asset_tracer_api.exception.UnauthorizedExceptionHandler;
import com.kshrd.asset_tracer_api.model.dto.ItemDTO;
import com.kshrd.asset_tracer_api.model.dto.ItemDetailAllDTO;
import com.kshrd.asset_tracer_api.model.dto.ItemDetailDTO;
import com.kshrd.asset_tracer_api.model.entity.ItemDetail;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.mapper.ItemDetailMapper;
import com.kshrd.asset_tracer_api.model.request.ItemDetailRequest;
import com.kshrd.asset_tracer_api.model.request.ItemDetailUpdateRequest;
import com.kshrd.asset_tracer_api.repository.ItemDetailRepository;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.service.ItemDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ItemDetailServiceImp implements ItemDetailService {
    private final ItemDetailRepository itemDetailRepository;
    private final ItemDetailMapper itemDetailMapper;
    private final OrganizationDetailRepository organizationDetailRepository;
    private UUID getCurrentUser() {

        Object getContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(getContext.equals("anonymousUser")) {
            throw new UnauthorizedExceptionHandler("Unauthorized User");
        }

        UserApp user = (UserApp) getContext;
        return user.getId();
    }

    @Override
    public ItemDetailDTO getItemDetailById(UUID itemDetailId) {

        if(getCurrentUser() == null){
            throw new UnauthorizedExceptionHandler("Access Denied, Please log in");
        }

        ItemDetail itemDetail = itemDetailRepository.getItemDetailById(itemDetailId);

        if (itemDetail == null){
            throw new NotFoundExceptionHandler("Item not found");
        }

        return itemDetailMapper.toItemDetailDto(itemDetail);
    }

    @Override
    public UUID addItemDetail(ItemDetailRequest itemDetailRequest) {
        var organizationId = itemDetailRequest.getOrganizationId();

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(!roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }
        return itemDetailRepository.addItemDetail(itemDetailRequest, getCurrentUser());
    }

    @Override
    public UUID updateItemDetail(ItemDetailUpdateRequest itemDetailUpdateRequest, UUID itemDetailId) {

        var organizationId = itemDetailUpdateRequest.getOrganizationId();
        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(roleName == null || !roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }
        else if(itemDetailRepository.getItemDetailById(itemDetailId) == null){
            throw new NotFoundExceptionHandler("Item not found");
        }

        return itemDetailRepository.updateItemDetail(itemDetailUpdateRequest, itemDetailId, getCurrentUser());
    }

    @Override
    public List<ItemDetailDTO> getAllItemsDetail(UUID orgId, Integer page, Integer size, String name, String normalCategoryName, String sort) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        List<ItemDetail> itemDetails = itemDetailRepository.getAllItemsDetail(orgId, page, size, name, normalCategoryName, sort);

        if(itemDetails.isEmpty()){
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return itemDetailMapper.INSTANCE.toItemDetailDtos(itemDetails);
    }

    @Override
    public UUID deleteItemById(UUID itemId, UUID organizationId) {
        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(roleName == null || !roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }

        ItemDetail itemDetail = itemDetailRepository.getItemDetailById(itemId);

        if(itemDetail == null){
            throw new NotFoundExceptionHandler("Item Not Found");
        }

        return itemDetailRepository.deleteItemById(itemId, getCurrentUser());
    }

    @Override
    public List<ItemDetailAllDTO> getAllItemDetailByOrganizationId(UUID orgId) {
        List<ItemDetail> itemDetails = itemDetailRepository.getAllItemDetailByOrganizationId(orgId);

        if(itemDetails.isEmpty()) {
            throw new NotFoundExceptionHandler("Data not found");
        }

        return itemDetailMapper.INSTANCE.toItemDetailAllDtos(itemDetails);
    }

    @Override
    public ItemDTO getItemById(UUID itemDetailId, UUID orgId) {

        ItemDetail itemDetail = itemDetailRepository.getItemById(itemDetailId, orgId);

        if(itemDetail == null) {
            throw new NotFoundExceptionHandler("Item not found");
        }

        return itemDetailMapper.INSTANCE.toItemDto(itemDetail);
    }
}
