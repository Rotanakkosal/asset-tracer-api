package com.kshrd.asset_tracer_api.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.asset_tracer_api.model.entity.ItemDetail;
import com.kshrd.asset_tracer_api.model.entity.Organization;
import com.kshrd.asset_tracer_api.model.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDTO {
    private UUID id;
    private String name;
    private String type;
    private String labelCode;
    private String serialCode;
    private String status;
    private String owner;
    private String assetName;
    private UUID roomId;
    private String roomName;
    private String roomImage;
    private ItemDetail itemDetail;
    private Timestamp createdAt;
    private UUID createdBy;
    private UUID organizationId;
    private String description;
}
