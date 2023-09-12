package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.entity.EmailVerification;

import java.util.UUID;

public interface EmailVerificationService {
    EmailVerification getEmailVerificationByUserId(UUID userId);
    EmailVerification getEmailVerificationByCode(String code);
//    EmailVerification getEmailVerifyByGoogle(String code);
}
