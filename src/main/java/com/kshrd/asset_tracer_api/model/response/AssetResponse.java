package com.kshrd.asset_tracer_api.model.response;

import com.kshrd.asset_tracer_api.model.entity.ItemDetail;
import com.kshrd.asset_tracer_api.model.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetResponse {
    private UUID id;
    private String type;
    private String labelCode;
    private String serialCode;
    private String status;
    private String owner;
    private String roomName;
    private String roomImage;
    private ItemDetail itemDetail;
    private Organization organization;
}
