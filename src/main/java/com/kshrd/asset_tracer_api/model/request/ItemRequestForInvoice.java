package com.kshrd.asset_tracer_api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestForInvoice {
    private String name;
    private String model;
    private String image;
    private Integer qty;
    private Double unitPrice;
    private Integer discount;
    private UUID normalCategoryId;
    private UUID organizationId;
    private List<ItemDetailRequest> itemDetailRequests;
}
