package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.config.UuidTypeHandler;
import com.kshrd.asset_tracer_api.model.entity.Role;
import com.kshrd.asset_tracer_api.model.request.RoleRequest;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.UUID;

@Mapper
public interface RoleRepository {

    @Select("""
            SELECT * FROM role order by name
            """)
    @Result(property = "id", column = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class)
    List<Role> getAllRoles();

    @Select("""
            select * from get_all_roles_func(#{page}, #{size}, #{name})
            """)
    @Result(property = "id", column = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class)
    List<Role> getAllRolesByFunc(Integer page, Integer size, String name);

    @Select("""
            SELECT * FROM role WHERE id = #{id}
            """)
    @Result(property = "id", column = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class)
    Role getRoleById(@Param("id") UUID roleId);


    @Select("""
            UPDATE role SET name = #{req.name} WHERE id = #{roleId}
            """)
    UUID updateRole(@Param("req") RoleRequest roleRequest, UUID roleId);

    @Select("""
            INSERT INTO role(name) VALUES(#{req.name}) RETURNING id
            """)
    UUID addRole(@Param("req") RoleRequest roleRequest);

    @Select("""
            select r.id, r.name from role r
            inner join organization_detail od
            on r.id = od.role_id
            where od.organization_id = #{organizationId}
            and od.userId = #{userId}
            """)
    Role getRoleByUserAndOrganization(UUID userId, UUID organizationId);
}
