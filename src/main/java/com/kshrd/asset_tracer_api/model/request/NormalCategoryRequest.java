package com.kshrd.asset_tracer_api.model.request;

import com.kshrd.asset_tracer_api.model.entity.SuperCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NormalCategoryRequest {
    private String name;
    private String icon;
    private UUID superCategoryId;
    private UUID organizationId;
}
