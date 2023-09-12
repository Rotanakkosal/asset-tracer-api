package com.kshrd.asset_tracer_api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mail {
    private String toEmail;
    private String senderName;
    private String subject;
    private String body;
}
