package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.request.OrganizationDetailRequest;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.service.OrganizationDetailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/organization-detail")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationDetailController {

    private final OrganizationDetailService organizationDetailServiceImp;
    private final OrganizationDetailRepository organizationDetailRepository;

    @GetMapping("/{userId}/{organizationId}")
    public ResponseEntity<?> getOrganizationDetail(@PathVariable UUID userId, @PathVariable UUID organizationId) {
        return BodyResponse.getBodyResponse(organizationDetailServiceImp.getOrganizationDetail(userId, organizationId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllOrganizationsDetailByUserId(@PathVariable("userId") UUID userId) {
        return BodyResponse.getBodyResponse(organizationDetailServiceImp.getAllOrganizationsDetailByUserId(userId));
    }

    @GetMapping("get-all-request-users/{orgId}")
    public ResponseEntity<?> getAllRequestUsers(@PathVariable("orgId") UUID organizationId){
        return BodyResponse.getBodyResponse(organizationDetailServiceImp.getAllRequestUsers(organizationId),
                organizationDetailRepository.getCountData());
    }
    @GetMapping("get-all-joined-users/{orgId}")
    public ResponseEntity<?> getAllUsers(@PathVariable("orgId") UUID organizationId){
        return BodyResponse.getBodyResponse(organizationDetailServiceImp.getAllJoinedUsers(organizationId),
                organizationDetailRepository.getCountData());
    }
}
