package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.exception.UnauthorizedExceptionHandler;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.request.OrganizationJoinRequest;
import com.kshrd.asset_tracer_api.model.request.OrganizationRequest;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.repository.OrganizationRepository;
import com.kshrd.asset_tracer_api.service.OrganizationDetailService;
import com.kshrd.asset_tracer_api.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/organizations")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationController {
    private final OrganizationService organizationServiceImp;
    private final OrganizationRepository organizationRepository;
    private final OrganizationDetailService organizationDetailService;

    private UUID getCurrentUser() {
        Object getContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(getContext.equals("anonymousUser")) {
            throw new UnauthorizedExceptionHandler("Unauthorized User");
        }
        UserApp user = (UserApp) getContext;
        return user.getId();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrganizations() {
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllOrganizations());
    }

    @GetMapping("")
    @Operation(summary = "Get all organizations with pagination")
    public ResponseEntity<?> getAllOrganizationsByFunc(@RequestParam(required = false) Integer page,
                                                       @RequestParam(required = false) Integer size) {
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllOrganizations(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrganizationById(@PathVariable("id") UUID organizationId) {
        return BodyResponse.getBodyResponse(organizationServiceImp.getOrganizationById(organizationId));
    }

    @PostMapping("")
    public ResponseEntity<?> addOrganization(@RequestBody OrganizationRequest organizationRequest) {
        UUID getId = organizationServiceImp.addOrganization(organizationRequest);
        return BodyResponse.getBodyResponse(organizationServiceImp.getOrganizationById(getId));
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinOrganization(@RequestBody OrganizationJoinRequest organizationJoinRequest) {
        UUID organizationId = organizationServiceImp.joinOrganization(organizationJoinRequest);
        return BodyResponse.getBodyResponse(organizationServiceImp.getOrganizationById(organizationId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrganization(@RequestBody OrganizationRequest organizationRequest,
                                                @PathVariable("id") UUID organizationId) {
        organizationServiceImp.updateOrganization(organizationRequest, organizationId);
        return BodyResponse.getBodyResponse(organizationServiceImp.getOrganizationById(organizationId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable("id") UUID organizationId) {
        organizationServiceImp.deleteOrganization(organizationId);
        return BodyResponse.getBodyResponse(organizationId);
    }

    @GetMapping("/{userId}/organization")
    @Operation(summary = "Get all organization by user id")
    public ResponseEntity<?> getAllOrganizationByUserId(@PathVariable("userId") UUID userId,
                                                        @RequestParam(required = false) String search,
                                                        @RequestParam(required = false) String sort) {
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllOrganizationsByUserId(userId, search, sort));
    }

    @GetMapping("/filter/{orgId}")
    @Operation(summary = "Filter")
    public ResponseEntity<?> getAllUserJoinedByOrganizationId(
            @PathVariable("orgId") UUID orgId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort) {
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllUserJoined(orgId, search, sort));
    }

    @PutMapping("/approve-user")
    public ResponseEntity<?> approveUserRequestJoin(@RequestParam("userId") UUID userId, @RequestParam("organizationId") UUID organizationId) {
        return BodyResponse.getBodyResponse(organizationServiceImp.approveUserRequestJoin(userId, organizationId));
    }

    @PutMapping("/reject-user")
    public ResponseEntity<?> rejectUserRequestJoin(@RequestParam("userId") UUID userId, @RequestParam("organizationId") UUID organizationId) {
        return BodyResponse.getBodyResponse(organizationServiceImp.rejectUserRequestJoin(userId, organizationId));
    }

    @PutMapping("/approve-invitation/{orgId}")
    public ResponseEntity<?> approveInvitation(@PathVariable UUID orgId) {
        return BodyResponse.getBodyResponse(organizationServiceImp.approveInvitation(orgId));
    }

    @PutMapping("/reject-invitation/{orgId}")
    public ResponseEntity<?> rejectInvitation(@PathVariable UUID orgId) {
        return BodyResponse.getBodyResponse(organizationServiceImp.rejectInvitation(orgId));
    }

    @PutMapping("/delete-user")
    public ResponseEntity<?> deleteUserFromOrganization(@RequestParam("userId") UUID userId, @RequestParam("organizationId") UUID organizationId) {
        return BodyResponse.getBodyResponse(organizationDetailService.deleteUserFromOrganization(userId, organizationId));
    }

    @GetMapping("/get-all-users/{orgId}")
    public ResponseEntity<?> getAllUsersByOrgId(@PathVariable("orgId") UUID orgId) {
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllUsersByOrgId(orgId));
    }

    @GetMapping("/get-all-request-users")
    public ResponseEntity<?> getAllRequestUsersByUser(@RequestParam(required = false) String search, @RequestParam(required = false) String sort) {
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllRequestUsersByUserId(search, sort));
    }

    @GetMapping("/get-all-joined-users/{orgId}")
    public ResponseEntity<?> getAllJoinedUsersByOrgId(@PathVariable("orgId") UUID orgId,
                                                      @RequestParam(required = false) String search,
                                                      @RequestParam(required = false) String sort) {
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllJoinedUsersByOrgId(orgId, search, sort));
    }

    @GetMapping("/get-all-invitation-from-organization")
    public ResponseEntity<?> getAllInvitationFromOrganization(@RequestParam(required = false) Integer page,
                                                              @RequestParam(required = false) Integer size,
                                                              @RequestParam(required = false) String search,
                                                              @RequestParam(required = false) String sort) {

        Integer countData = organizationRepository.getCountInvitationFromOrganization(search, getCurrentUser());
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllInvitationFromOrganization(page, size, search, sort), countData);
    }

    @GetMapping("/get-all-organizations-by-owner")
    public ResponseEntity<?> getAllOrganizationsByOwner() {
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllOrganizationsByOwner());
    }

    @GetMapping("/get-all-notifications-by-organization/{orgId}")
    public ResponseEntity<?> getAllNotificationByOrg(@PathVariable UUID orgId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String search, @RequestParam(required = false) String sort) {
        Integer countData = organizationRepository.getCountAllNotificationsByOrgId(orgId,search);
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllNotificationsByOrgId(orgId, page, size, search, sort), countData);
    }

    @GetMapping("/get-all-notifications-by-user")
    public ResponseEntity<?> getAllNotificationByUser() {
        return BodyResponse.getBodyResponse(organizationServiceImp.getAllNotificationByUser());
    }

    @PutMapping("/remove-member/{userId}/{orgId}")
    public ResponseEntity<?> removeMember(@PathVariable UUID userId, @PathVariable UUID orgId) {
        return BodyResponse.getBodyResponse(organizationServiceImp.removeMember(userId, orgId));
    }
}
