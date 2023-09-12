package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.request.InviteUserRequest;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.service.UserAppService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
public class UserAppController {
    private final UserAppService userAppService;

    @GetMapping("/search-users")
    public ResponseEntity<?> searchUsers(@RequestParam("search") String search) {
        return BodyResponse.getBodyResponse(userAppService.getAllUsersFromSearching(search));
    }

    @PutMapping("/invite-user")
    public ResponseEntity<?> inviteUser(@RequestBody InviteUserRequest inviteUserRequest) {
        UUID userId = userAppService.inviteUser(inviteUserRequest);
        return BodyResponse.getBodyResponse(userAppService.getUserById(userId));
    }
 }
