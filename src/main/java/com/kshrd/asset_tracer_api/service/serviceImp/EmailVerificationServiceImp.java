package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.model.entity.EmailVerification;
import com.kshrd.asset_tracer_api.repository.EmailVerificationRepository;
import com.kshrd.asset_tracer_api.repository.UserAppRepository;
import com.kshrd.asset_tracer_api.service.EmailVerificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailVerificationServiceImp implements EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final UserAppRepository userAppRepository;
    @Override
    public EmailVerification getEmailVerificationByUserId(UUID userId) {
        return emailVerificationRepository.getEmailVerificationByUserId(userId);
    }

    @Override
    public EmailVerification getEmailVerificationByCode(String code) {
        EmailVerification getEmailVerification =  emailVerificationRepository.getEmailVerificationByCode(code);
        if(!getEmailVerification.getIsVerified()) {
            emailVerificationRepository.updateIsVerified(code);
            userAppRepository.updateUserSetting(getEmailVerification.getUserId());
        }
        return emailVerificationRepository.getEmailVerificationByCode(code);
    }

//    @Override
//    public EmailVerification getEmailVerifyByGoogle(String code) {
//        return null;
////        return emailVerificationRepository.insertGoogleVerify();
//    }


}
