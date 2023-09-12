package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.model.entity.Organization;
import com.kshrd.asset_tracer_api.model.entity.OrganizationDetail;
import com.kshrd.asset_tracer_api.model.request.InviteUserRequest;
import com.kshrd.asset_tracer_api.model.request.OrganizationDetailRequest;
import com.kshrd.asset_tracer_api.model.request.OrganizationRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface OrganizationDetailRepository {

    @Results(
            id = "organizationDetailMapper",
            value = {
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "organizationID", column = "organization_id"),
                    @Result(property = "isActive", column = "is_active"),
                    @Result(property = "createdAt", column = "created_at"),
                    @Result(property = "createdBy", column = "created_by"),
                    @Result(property = "organization", column = "organization_id",
                            one = @One(select = "com.kshrd.asset_tracer_api.repository.OrganizationRepository.getOrganizationById")),
                    @Result(property = "role", column = "role_id",
                            one = @One(select = "com.kshrd.asset_tracer_api.repository.RoleRepository.getRoleById"))})
    @Select("""
            insert into organization_detail(user_id, organization_id, role_id)
            values(#{userId}, #{organizationId}, (select id from role where name = 'ADMIN'))
            returning *
            """)
    OrganizationDetail userAddNewOrganizationDetail(UUID userId, UUID organizationId);

    @Select("""
            INSERT INTO organization(name, code, address, logo)
            VALUES (#{req.organizationName}, #{code}, #{req.organizationAddress}, #{req.organizationLogo})
            RETURNING id
            """)
    UUID addOrganization(@Param("req") OrganizationDetailRequest organizationDetailRequest, String code);

    @Select("""
            select * from organization_detail where deleted_at is null
            """)
    @ResultMap("organizationDetailMapper")
    List<OrganizationDetail> getAllOrganizationsDetailByUserId(@Param("userId") UUID userId);

    @Select("""
            select r.name from organization_detail od
            inner join role r
            on r.id = od.role_id
            where od.user_id = #{userId}
            and od.organization_id = #{orgId}
            and od.deleted_at is null
            """)
    String getExistRoleInOrganization(UUID userId, UUID orgId);

    @Select("""
            select user_id from organization_detail
            where user_id = #{userId}
            and organization_id = #{orgId}
            and deleted_at is null
            """)
    UUID getExistUserInOrganization(UUID userId, UUID orgId);

    @Select("""
            select organization_id from organization_detail
            where user_id = #{userId}
            and organization_id = #{orgId}
            and deleted_at is null
            """)
    UUID getOrganizationById(UUID userId, UUID orgId);

    @Select("""
            select exists(select * from organization_detail where user_id = #{userId} and organization_id = #{organizationId})
            """)
    Boolean isHasOrganizationDetail(UUID userId, UUID organizationId);


    @Select("""
            select od.*
            from organization_detail od
            where od.organization_id = #{orgId}
            and od.is_active = false
            """)
    @ResultMap("organizationDetailMapper")
    List<OrganizationDetail> getAllJoinedUsers(@Param("orgId") UUID organizationId);
    @Select("""
            select od.*
            from organization_detail od
            where od.organization_id = #{orgId}
            and od.is_active = true
            and od.is_deleted_at is null
            """)
    @ResultMap("organizationDetailMapper")
    List<OrganizationDetail> getAllRequestUsers(@Param("orgId") UUID organizationId);

    @Select("""
            select count(*)
            from organization_detail od
            where od.is_active = false
            and od.is_deleted is null                  
            """)
    Integer getCountData();

    @Select("""
            insert into organization_detail(user_id, organization_id, role_id, is_active, is_member, created_by, status)
            values(#{req.userId}, #{req.organizationId}, (select id from role where name = 'USER'), false, false, #{getCurrentUserId}, 'is_invited')
            returning user_id 
            """)
    UUID inviteUser(@Param("req") InviteUserRequest inviteUserRequest, UUID getCurrentUserId);

    @Select("""
            update organization_detail set status = 'is_invited', updated_at = CURRENT_TIMESTAMP, updated_by = #{getCurrentUserId}
            where user_id = #{userId} and organization_id = #{orgId}
            returning user_id 
            """)
    UUID inviteUserWasRejected(UUID userId, UUID orgId, UUID getCurrentUserId);

    @Select("""
            update organization_detail set
            is_active = true, is_member = true, status = 'is_approved'
            where user_id = #{userId} and organization_id = #{orgId}
            returning organization_id
            """)
    UUID approveInvitation(UUID userId, UUID orgId);

    @Select("""
            update organization_detail set
            is_active = false, is_member = false, status = 'is_rejected'
            where user_id = #{userId} and organization_id = #{orgId}
            returning organization_id
            """)
    UUID rejectInvitation(UUID userId, UUID orgId);

    @Select("""
            update organization_detail
            set deleted_at = CURRENT_TIMESTAMP,
            deleted_by = #{getCurrentUserId},
            where user_id = #{userId}
            and organization_id = #{orgId}
            returning user_id
            """)
    UUID deleteUserFromOrganization(UUID userId, UUID orgId, UUID getCurrentUserId);

    @Select("""
            select exists(select * from organization_detail where user_id = #{userId} and organization_id = #{orgId} and status = 'is_rejected')
            """)
    Boolean isUserWasRejected(UUID userId, UUID orgId);

    @Select("""
            select exists(select * from organization_detail where user_id = #{userId} and organization_id = #{orgId} and status = #{status})
            """)
    Boolean checkUserByStatus(UUID userId, UUID orgId, String status);

    @Select("""
            update organization_detail 
            set updated_at = CURRENT_TIMESTAMP,
            updated_by = #{getCurrentUserId},
            is_active = false,
            is_member = false,
            status = 'is_removed'
            where user_id = #{userId}
            and organization_id = #{orgId}
            returning user_id
            """)
    UUID removeMember(UUID userId, UUID orgId, UUID getCurrentUserId);
}