package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.exception.NotFoundExceptionHandler;
import com.kshrd.asset_tracer_api.model.entity.Role;
import com.kshrd.asset_tracer_api.model.request.RoleRequest;
import com.kshrd.asset_tracer_api.repository.RoleRepository;
import com.kshrd.asset_tracer_api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {

        List<Role> roles = roleRepository.getAllRoles();

        if(roles.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return roles;
    }

    @Override
    public List<Role> getAllRoleByFunc(Integer page, Integer size, String name) {

        List<Role> roles = roleRepository.getAllRolesByFunc(page, size, name);

        if(roles.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return roles;
    }


    @Override
    public Role getRoleById(UUID roleId) {
        if(roleRepository.getRoleById(roleId) == null) {
            throw new NotFoundExceptionHandler("You're searching role id is not found!");
        }
        return roleRepository.getRoleById(roleId);
    }

    @Override
    public UUID updateRole(RoleRequest roleRequest, UUID roleId) {
        if(roleRepository.getRoleById(roleId) == null){
            throw new NotFoundExceptionHandler("You're updating id is not found!");
        }
        return roleRepository.updateRole(roleRequest, roleId);
    }

    @Override
    public UUID addRole(RoleRequest roleRequest) {
        return roleRepository.addRole(roleRequest);
    }
}
