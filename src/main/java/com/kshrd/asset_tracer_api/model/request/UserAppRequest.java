package com.kshrd.asset_tracer_api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAppRequest {
    private String name;
    private String email;
    private String password;
}
