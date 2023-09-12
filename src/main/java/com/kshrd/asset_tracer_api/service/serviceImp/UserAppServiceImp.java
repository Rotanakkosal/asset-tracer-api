package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.config.JwtUtil;
import com.kshrd.asset_tracer_api.exception.*;
import com.kshrd.asset_tracer_api.model.dto.SearchUserAppDTO;
import com.kshrd.asset_tracer_api.model.dto.UserAppCreatedByDTO;
import com.kshrd.asset_tracer_api.model.dto.UserAppDTO;
import com.kshrd.asset_tracer_api.model.dto.UserAppUpdateDTO;
import com.kshrd.asset_tracer_api.model.entity.OrganizationDetail;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.entity.UserResetPassword;
import com.kshrd.asset_tracer_api.model.mapper.UserAppMapper;
import com.kshrd.asset_tracer_api.model.mapper.UserAppUpdateMapper;
import com.kshrd.asset_tracer_api.model.request.*;
import com.kshrd.asset_tracer_api.model.response.AuthResponse;
import com.kshrd.asset_tracer_api.repository.EmailVerificationRepository;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.repository.UserAppRepository;
import com.kshrd.asset_tracer_api.repository.UserResetPasswordRepository;
import com.kshrd.asset_tracer_api.service.MailSenderService;
import com.kshrd.asset_tracer_api.service.UserAppService;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAppServiceImp implements UserDetailsService, UserAppService {
    private UserApp userApp = new UserApp();
    private UserAppDTO userAppDTO = new UserAppDTO();
    private UserAppMapper userAppMapper;
    private UserAppUpdateMapper userAppUpdateMapper;
    private final UserAppRepository userAppRepository;
    private final OrganizationDetailRepository organizationDetailRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserResetPasswordRepository userResetPasswordRepository;
    private final MailSenderService mailSenderService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private UUID getCurrentUser() {

        Object getContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(getContext.equals("anonymousUser")) {
            throw new UnauthorizedExceptionHandler("Unauthorized User");
        }

        UserApp user = (UserApp) getContext;
        return user.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserApp userApp = userAppRepository.getUserByEmail(email);

        if (userApp == null) {
            throw new NotFoundExceptionHandler("User not found");
        } else if (email.isEmpty()) {
            throw new FieldEmptyExceptionHandler("Email field is empty");
        } else if (email.isBlank()) {
            throw new FieldBlankExceptionHandler("Email field is blank");
        } else if (!email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$")) {
            throw new NotValidValueExceptionHandler("Email not valid");
        }

        return userApp;
    }

    @Override
    public UserAppDTO register(UserAppRequest userAppRequest) throws MessagingException {

        String email = userAppRepository.getEmail(userAppRequest.getEmail());

        if (userAppRequest.getEmail().equalsIgnoreCase(email)) {
            throw new UserDuplicateExceptionHandler("User already registered");
        } else {
            validateUserAppRequest(userAppRequest);
        }
//        else if(!userAppRequest.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
//            throw new NotValidValueExceptionHandler("Password minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character");
//        }

        userAppRequest.setPassword(passwordEncoder.encode(userAppRequest.getPassword()));
        userApp = userAppRepository.insertUser(userAppRequest);
        userAppDTO = userAppMapper.INSTANCE.toUserAppDto(userApp);

        String code = UUID.randomUUID().toString();
        emailVerificationRepository.insertEmailVerification(userApp.getId(), code);
        mailSenderService.sendMail(userAppRequest, code);

        return userAppDTO;
    }

    private void validateUserAppRequest(UserAppRequest userAppRequest) {
        if (userAppRequest.getEmail().isEmpty()) {
            throw new FieldEmptyExceptionHandler("Email field is empty");
        } else if (userAppRequest.getEmail().isBlank()) {
            throw new FieldBlankExceptionHandler("Email field is blank");
        } else if (userAppRequest.getPassword().isEmpty()) {
            throw new FieldEmptyExceptionHandler("Password field is empty");
        } else if (userAppRequest.getPassword().isBlank()) {
            throw new FieldBlankExceptionHandler("Password field is blank");
        } else if (!userAppRequest.getEmail().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$")) {
            throw new NotValidValueExceptionHandler("Email not valid");
        }
    }

    @Override
    public AuthResponse registerWithGoogle(UserAppRequest userAppRequest) throws MessagingException {

        String email1 = userAppRepository.getEmail(userAppRequest.getEmail()); // boolean

        if (!userAppRequest.getEmail().equalsIgnoreCase(email1)) {

            String code = UUID.randomUUID().toString();
            userAppRequest.setPassword(passwordEncoder.encode(userAppRequest.getPassword()));   // get password then +
            userApp = userAppRepository.insertUserWithGoogle(userAppRequest);

            userAppDTO = userAppMapper.toUserAppDto(userApp);
            emailVerificationRepository.insertEmailVerify(userApp.getId(), code);  // add prefix code here?
            mailSenderService.sendSuccessMail(userAppRequest);

        } else validateUserAppRequest(userAppRequest);

        var user = userAppRepository.getUserByEmail(userAppRequest.getEmail());
        var jwtToken = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .token(jwtToken)
                .build();
    }

    @Override
    public UserAppDTO getUserById(UUID userId) {
        userApp = userAppRepository.getUserByID(userId);

        if (userApp == null) {
            throw new NotFoundExceptionHandler("User not found");
        }

        return UserAppMapper.INSTANCE.toUserAppDto(userApp);
    }

    @Override
    public UUID changePassword(UUID userId, UserPasswordRequest userPasswordRequest) {

        userApp = userAppRepository.getUserByID(userId);
        System.out.println(userApp);
        if (userAppRepository.getUserByID(userId) == null) {
            throw new NotFoundExceptionHandler("User not found");
        }
        else if (!passwordEncoder.matches(userPasswordRequest.getOldPassword(), userApp.getPassword())) {
            throw new PasswordNotMatchExceptionHandler("Old password not correct");
        }

        userPasswordRequest.setNewPassword(passwordEncoder.encode(userPasswordRequest.getNewPassword()));
        return userAppRepository.changePassword(userId, userPasswordRequest);
    }

    @Override
    public String forgotPassword(String userEmail) throws MessagingException {

        UserApp user = userAppRepository.getUserByEmail(userEmail);

        if (user == null) {
            throw new NotFoundExceptionHandler("User not found");
        }

        String code = UUID.randomUUID().toString();
        UserResetPassword data = userResetPasswordRepository.addUserResetPassword(user.getId(), code);

        if (data == null) {
            throw new NotFoundExceptionHandler("User reset password not found");
        }

        mailSenderService.sendMailForgotPassword(user.getId(), code, userEmail);

        return userEmail;
    }

    @Override
    public UserResetPassword resetPassword(UUID userId, String code) {

        UserResetPassword data = userResetPasswordRepository.getUserResetPassword(userId, code);

        if (data == null) {
            throw new NotFoundExceptionHandler("User reset password not found");
        }

        return data;
    }

    @Override
    public UUID changPasswordAfterForgot(UUID userId, String code, UserNewPasswordRequest newPassword) {

        UserResetPassword data = userResetPasswordRepository.getUserResetPassword(userId, code);

        if (data == null) {
            throw new NotFoundExceptionHandler("User reset password not found");
        }

        newPassword.setNewPassword(passwordEncoder.encode(newPassword.getNewPassword()));

        return userAppRepository.changePasswordAfterForgot(userId, newPassword);
    }

    @Override
    public UserAppUpdateDTO updateUser(UserAppUpdateRequest userAppUpdateRequest, UUID userId) {

        userApp = userAppRepository.getUserByID(userId);

        if(userApp == null) {
            throw new NotFoundExceptionHandler("User not found");
        }

        userApp = userAppRepository.updateUser(userAppUpdateRequest, userId);

        return userAppUpdateMapper.INSTANCE.toUserAppUpdateDto(userApp);
    }

    @Override
    public List<UserAppDTO> getAllJoinedUserByOrgId(UUID orgId) {
        return null;
    }

    @Override
    public List<SearchUserAppDTO> getAllUsersFromSearching(String search) {

        List<UserApp> userApps = userAppRepository.searchUsers(search, getCurrentUser());

        if(userApps.isEmpty()) {
            throw new NotFoundExceptionHandler("Data not found");
        }
        return userAppMapper.INSTANCE.toSearchUserAppDtos(userApps);
    }

    @Override
    public UUID inviteUser(InviteUserRequest inviteUserRequest) {

        Boolean userId = organizationDetailRepository.isUserWasRejected(inviteUserRequest.getUserId(), inviteUserRequest.getOrganizationId());
        Boolean isUserOwner = organizationDetailRepository.checkUserByStatus(inviteUserRequest.getUserId(), inviteUserRequest.getOrganizationId(), "is_owner");
        Boolean isUserWasRemoved = organizationDetailRepository.checkUserByStatus(inviteUserRequest.getUserId(), inviteUserRequest.getOrganizationId(), "is_removed");
        if(userId || isUserOwner || isUserWasRemoved) {
            return organizationDetailRepository.inviteUserWasRejected(inviteUserRequest.getUserId(), inviteUserRequest.getOrganizationId(), getCurrentUser());
        }
        else if(organizationDetailRepository.getExistUserInOrganization(inviteUserRequest.getUserId(), inviteUserRequest.getOrganizationId()) != null){
            throw new UserDuplicateExceptionHandler("User already exist");
        }

        return organizationDetailRepository.inviteUser(inviteUserRequest, getCurrentUser());
    }
}
