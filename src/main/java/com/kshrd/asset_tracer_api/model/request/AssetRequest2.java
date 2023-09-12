package com.kshrd.asset_tracer_api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequest2 {
    private String labelCode;
    private String serialCode;
    private String status;
    private String owner;
    private UUID roomId;
    private UUID organizationId;
}
