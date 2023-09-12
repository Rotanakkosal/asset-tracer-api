package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.request.*;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.service.AuthService;
import com.kshrd.asset_tracer_api.service.EmailVerificationService;
import com.kshrd.asset_tracer_api.service.serviceImp.UserAppServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {
    private final UserAppServiceImp userAppServiceImp;
    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/login")
//    @Operation(summary = "Login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        return BodyResponse.getBodyResponse(authService.authenticate(authRequest));
    }

    @PostMapping("/register")
//    @Operation(summary = "Register")
    public ResponseEntity<?> register(@RequestBody UserAppRequest userAppRequest) throws MessagingException {
        return BodyResponse.getBodyResponse(userAppServiceImp.register(userAppRequest));
    }

    @PostMapping("/register-with-google")
//    @Operation(summary = "Register with google")
    public ResponseEntity<?> registerWithGoogle(@RequestBody UserAppRequest userAppRequest)
            throws MessagingException {
        return BodyResponse.getBodyResponse(userAppServiceImp.registerWithGoogle(userAppRequest));
    }

    @GetMapping("/register/verify")
//    @Operation(summary = "Verify code")
    public ResponseEntity<?> verifyEmail(@RequestParam("code") String code) {
        return BodyResponse.getBodyResponse(emailVerificationService.getEmailVerificationByCode(code));
    }

    @GetMapping("/{id}")
//    @Operation(summary = "Get user by ID")
    public ResponseEntity<?> getUserById(@PathVariable("id") UUID userID) {
        return BodyResponse.getBodyResponse(userAppServiceImp.getUserById(userID));
    }

    @GetMapping("/forgot-password")
//    @Operation(summary = "User forgot password")
    public ResponseEntity<?> forgotPassword(@RequestParam String userEmail) throws MessagingException {
        return BodyResponse.getBodyResponse(userAppServiceImp.forgotPassword(userEmail));
    }

    @GetMapping("/reset-password")
//    @Operation(summary = "User reset password")
    public ResponseEntity<?> resetPassword(@RequestParam("id") UUID userId, @RequestParam("code") String code) {
        return BodyResponse.getBodyResponse(userAppServiceImp.resetPassword(userId, code));
    }

    @PostMapping("/set-password/{id}/{code}")
    public ResponseEntity<?> changPasswordAfterForgot(@PathVariable("id") UUID userId, @PathVariable("code") String code, @RequestBody UserNewPasswordRequest newPassword) {
        UUID id = userAppServiceImp.changPasswordAfterForgot(userId, code, newPassword);
        return BodyResponse.getBodyResponse(userAppServiceImp.getUserById(id));
    }

    @PutMapping("/change-password/{id}")
    @Operation(summary = "Change user password")
    public ResponseEntity<?> changePassword(@PathVariable("id") UUID userId, @RequestBody UserPasswordRequest userPasswordRequest) {
        UUID id = userAppServiceImp.changePassword(userId, userPasswordRequest);
        return BodyResponse.getBodyResponse(userAppServiceImp.getUserById(id));
    }

    @PutMapping("/{id}/user")
    @Operation(summary = "Update user")
    public ResponseEntity<?> updateUser(@RequestBody UserAppUpdateRequest userAppUpdateRequest, @PathVariable("id") UUID userId) {
        return BodyResponse.getBodyResponse(userAppServiceImp.updateUser(userAppUpdateRequest, userId));
    }

//    @GetMapping("/{id}/created-by")
//    public ResponseEntity<?> getCreatedBy(@PathVariable("id") UUID userId){
//        return BodyResponse.getBodyResponse(userAppServiceImp.getCreateByUserId(userId));
//    }

}
