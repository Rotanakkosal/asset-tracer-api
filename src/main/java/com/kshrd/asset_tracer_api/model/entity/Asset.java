package com.kshrd.asset_tracer_api.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.asset_tracer_api.model.dto.ItemDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Asset {
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
    private UUID itemDetailId;
    private UUID organizationId;
    private Timestamp createdAt;
    private UUID createdBy;
    private Timestamp updatedAt;
    private UUID updatedBy;
    private Timestamp deletedAt;
    private UUID deletedBy;
    private UUID invoiceId;
    private String description;
}
