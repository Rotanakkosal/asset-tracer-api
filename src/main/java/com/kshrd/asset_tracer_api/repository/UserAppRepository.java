package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.config.UuidTypeHandler;
import com.kshrd.asset_tracer_api.model.dto.UserAppDTO;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.request.UserAppRequest;
import com.kshrd.asset_tracer_api.model.request.UserAppUpdateRequest;
import com.kshrd.asset_tracer_api.model.request.UserNewPasswordRequest;
import com.kshrd.asset_tracer_api.model.request.UserPasswordRequest;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface UserAppRepository {

    @Results( id = "userMap",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(column = "created_at", property = "createdAt"),
                    @Result(column = "is_enabled", property = "isEnabled"),
                    @Result(column = "is_account_non_locked", property = "isAccountNonLocked"),
                    @Result(column = "is_account_non_expired", property = "isAccountNonExpired"),
            })
    @Select("""
            select * from user_acc where email = #{userEmail}
            """)
    UserApp getUserByEmail(String userEmail);

    @Select("""
            insert into user_acc(name, email, password)
            values(#{req.name}, #{req.email}, #{req.password})
            returning *;
            """)
    @ResultMap("userMap")
    UserApp insertUser(@Param("req") UserAppRequest userAppRequest);

    @Select("""
            insert into user_acc(name, email, password, is_enabled, is_account_non_locked, is_account_non_expired)
            values(#{req.name}, #{req.email}, #{req.password}, true, true, true)
            returning *;
            """)
    UserApp insertUserWithGoogle(@Param("req") UserAppRequest userAppRequest);

    @Select("""
            select email from user_acc where email = #{userEmail}
            """)
    String getEmail(String userEmail);

    @Select("""
                select * from user_acc
                where id = #{userId}
            """)
    @ResultMap("userMap")
    UserApp getUserByID(@Param("userId") UUID userId);

    @Update("""
            update user_acc set is_enabled = true, is_account_non_locked = true, is_account_non_expired = true
            where id = #{userId}
            """)
    void updateUserSetting(UUID userId);

    @Select("""
            update user_acc set password = #{req.newPassword}
            where id = #{userId}
            returning id
            """)
    UUID changePassword(UUID userId, @Param("req") UserPasswordRequest userPasswordRequest);

    @Select("""
            update user_acc set password = #{req.newPassword}
            where id = #{userId}
            returning id
            """)
    UUID changePasswordAfterForgot(UUID userId,@Param("req") UserNewPasswordRequest newPassword);

    @Select("""
            select ua.id id, ua.name name, ua.email email, ua.gender gender, ua.phone phone,ua.address address,
            ua.is_enabled is_enabled, ua.is_account_non_locked is_account_non_locked,
            ua.is_account_non_expired is_account_non_expired, ua.image image
            from user_acc ua
            inner join organization_detail od
            on ua.id = od.user_id
            where od.organization_id = #{organizationId}
            and od.status not in ('is_requested', 'is_invited')
            limit 3
            """)
    @ResultMap("userMap")
    List<UserApp> getAllUsersByOrganizationId(UUID organizationId);

    @Select("""
            select ua.id id, ua.name name, ua.email email, ua.gender gender, ua.phone phone,ua.address address,
            ua.is_enabled is_enabled, ua.is_account_non_locked is_account_non_locked,
            ua.is_account_non_expired is_account_non_expired, ua.image image
            from user_acc ua
            inner join organization_detail od
            on ua.id = od.user_id
            where od.organization_id = #{orgId}
            and od.is_member = false
            and od.is_active = false
            and od.status = 'is_invited'
            limit 3
            """)
    @ResultMap("userMap")
    List<UserAppDTO> getUsersByOrgId(UUID orgId);

    @Select("""
            update user_acc
            set name = #{req.name}, gender = #{req.gender},
            phone = #{req.phone}, address = #{req.address},
            image = #{req.image}
            where id = #{userId}
            returning *
            """)
    @ResultMap("userMap")
    UserApp updateUser(@Param("req")UserAppUpdateRequest userAppUpdateRequest, UUID userId);

    @Select("""
            select * from user_acc ua
            inner join organization_detail od
            on ua.id = od.user_id
            where organization_id = #{orgId}
            order by created_at desc
            """)
    List<UserApp> getAllUsersByOrgId(UUID orgId, String search, String sort);

    @Select("""
                select ua.* from user_acc ua
                inner join organization_detail od
                on  ua.id = od.user_id
                where od.organization_id = #{orgId}
                and is_active = false
            """)
    List<UserApp> getAllRequestUserByOrgId(UUID id);

    @Select("""
            select * from user_acc
            where deleted_at is null 
            and email ilike #{search} || '%'
            and email <> (select email from user_acc where id = #{getCurrentUserId})
            """)
    List<UserApp> searchUsers(String search, UUID getCurrentUserId);
}
