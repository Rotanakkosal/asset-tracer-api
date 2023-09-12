package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.request.RoleRequest;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/all")
    @Operation(summary = "Get all roles")
    public ResponseEntity<?> getAllRoles() {
        return BodyResponse.getBodyResponse(roleService.getAllRoles());
    }

    @GetMapping("")
    @Operation(summary = "Get all roles with pagination and search")
    public ResponseEntity<?> getAllRolesByFunc(@RequestParam (required = false) Integer page,
                                               @RequestParam (required = false) Integer size,
                                               @RequestParam(required = false) String name) {
        return BodyResponse.getBodyResponse(roleService.getAllRoleByFunc(page, size, name));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by id")
    public ResponseEntity<?> getRoleById(@PathVariable("id") UUID roleId) {
        return BodyResponse.getBodyResponse(roleService.getRoleById(roleId));
    }
    @PostMapping("")
    public ResponseEntity<?> addRole(@RequestBody RoleRequest roleRequest){
        UUID getId = roleService.addRole(roleRequest);
        return BodyResponse.getBodyResponse(roleService.getRoleById(getId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@RequestBody  RoleRequest roleRequest, @PathVariable("id") UUID roleId){
        roleService.updateRole(roleRequest, roleId);
        return BodyResponse.getBodyResponse(roleService.getRoleById(roleId));
    }
}
