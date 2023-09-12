package com.kshrd.asset_tracer_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountAssetByStatusDTO {
    private UUID id;
    private String name;
    private Integer countAsset;
}
