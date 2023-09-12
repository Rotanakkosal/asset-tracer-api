package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.request.ItemDetailRequest;
import com.kshrd.asset_tracer_api.model.request.ItemDetailUpdateRequest;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.repository.ItemDetailRepository;
import com.kshrd.asset_tracer_api.service.ItemDetailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/item-details")
public class ItemDetailController {
    private final ItemDetailService itemDetailService;
    private final ItemDetailRepository itemDetailRepository;
    @GetMapping("/{id}")
    public ResponseEntity<?> getItemDetailById(@PathVariable("id") UUID itemDetailId){
        return BodyResponse.getBodyResponse(itemDetailService.getItemDetailById(itemDetailId));
    }

    @PostMapping
    public ResponseEntity<?> addItemDetail(@RequestBody ItemDetailRequest itemDetailRequest){
        UUID getItemId = itemDetailService.addItemDetail(itemDetailRequest);
        return BodyResponse.getBodyResponse(itemDetailService.getItemDetailById(getItemId));
    }

    @GetMapping("/by-organization/{orgId}")
    public ResponseEntity<?> getAllItemsDetail(    @PathVariable("orgId") UUID orgId,
                                                   @RequestParam (required = false) Integer page,
                                                   @RequestParam (required = false) Integer size,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String normalCategoryName,
                                                   @RequestParam(required = false) String sort){

        Integer countData = itemDetailRepository.getCountAllItemsDetail(orgId,name,normalCategoryName);
        return BodyResponse.getBodyResponse(itemDetailService.getAllItemsDetail(orgId, page, size, name, normalCategoryName, sort), countData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItemDetail(@RequestBody ItemDetailUpdateRequest itemDetailUpdateRequest,
                                              @PathVariable("id") UUID itemDetailId){
        itemDetailService.updateItemDetail(itemDetailUpdateRequest, itemDetailId);
        return BodyResponse.getBodyResponse(itemDetailService.getItemDetailById(itemDetailId));
    }


    @DeleteMapping("/{itemId}/{organizationId}")
    public ResponseEntity<?> deleteItemDetailById(@PathVariable UUID itemId, @PathVariable UUID organizationId){
        return BodyResponse.getBodyResponse(itemDetailService.deleteItemById(itemId, organizationId));
    }

    @GetMapping("/get-all/{orgId}")
    public ResponseEntity<?> getAllItemDetailByOrganizationId(@PathVariable("orgId") UUID orgId) {
        return BodyResponse.getBodyResponse(itemDetailService.getAllItemDetailByOrganizationId(orgId));
    }

    @GetMapping("/get-by-id/{itemDetailId}/{orgId}")
    public ResponseEntity<?> getItemById(@PathVariable UUID itemDetailId, @PathVariable UUID orgId) {
        return BodyResponse.getBodyResponse(itemDetailService.getItemById(itemDetailId, orgId));
    }
}
