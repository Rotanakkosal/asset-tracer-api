package com.kshrd.asset_tracer_api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAppUpdateRequest {
    private String name;
    private String gender;
    private String phone;
    private String address;
    private String image;
}
