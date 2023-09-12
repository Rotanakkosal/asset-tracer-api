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
public class RoomRequest {
    private String name;
    private String type;
    private String floor;
    private String description;
    private String image;
    private UUID organizationId;
}
