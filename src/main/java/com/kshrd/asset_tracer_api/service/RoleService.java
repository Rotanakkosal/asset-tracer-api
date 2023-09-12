package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.entity.Role;
import com.kshrd.asset_tracer_api.model.request.RoleRequest;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    List<Role> getAllRoles();
    List<Role> getAllRoleByFunc(Integer page, Integer size, String name);
    Role getRoleById(UUID roleId);
    UUID updateRole(RoleRequest roleRequest, UUID roleId);
    UUID addRole(RoleRequest roleRequest);
}
