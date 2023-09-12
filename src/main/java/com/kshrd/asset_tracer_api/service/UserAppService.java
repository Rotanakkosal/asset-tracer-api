package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.dto.SearchUserAppDTO;
import com.kshrd.asset_tracer_api.model.dto.UserAppCreatedByDTO;
import com.kshrd.asset_tracer_api.model.dto.UserAppDTO;
import com.kshrd.asset_tracer_api.model.dto.UserAppUpdateDTO;
import com.kshrd.asset_tracer_api.model.entity.UserResetPassword;
import com.kshrd.asset_tracer_api.model.request.*;
import com.kshrd.asset_tracer_api.model.response.AuthResponse;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.UUID;

public interface UserAppService {
    UserAppDTO register(UserAppRequest userAppRequest) throws MessagingException;
    AuthResponse registerWithGoogle(UserAppRequest userAppRequest) throws MessagingException;
    UserAppDTO getUserById(UUID userId);
    UUID changePassword(UUID userId, UserPasswordRequest userPasswordRequest);
    String forgotPassword(String userEmail) throws MessagingException;
    UserResetPassword resetPassword(UUID userId, String code);
    UUID changPasswordAfterForgot(UUID userId, String code, UserNewPasswordRequest newPassword);
    UserAppUpdateDTO updateUser(UserAppUpdateRequest userAppUpdateRequest, UUID userId);
    List<UserAppDTO> getAllJoinedUserByOrgId (UUID orgId);
    List<SearchUserAppDTO> getAllUsersFromSearching(String search);
    UUID inviteUser(InviteUserRequest inviteUserRequest);
}
