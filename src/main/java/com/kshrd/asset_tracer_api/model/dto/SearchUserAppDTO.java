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
public class SearchUserAppDTO {
    private UUID id;
    private String name;
    private String email;
    private String image;
}
