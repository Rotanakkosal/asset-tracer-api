package com.kshrd.asset_tracer_api.model.request;

import com.kshrd.asset_tracer_api.model.entity.ItemDetail;
import com.kshrd.asset_tracer_api.model.entity.Organization;
import com.kshrd.asset_tracer_api.model.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequest {
    private UUID itemDetailId;
    private String labelCode;
    private String serialCode;
    private String status;
    private String owner;
    private UUID roomId;
    private UUID organizationId;
}
