package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.config.UuidTypeHandler;
import com.kshrd.asset_tracer_api.model.entity.NotificationByUser;
import com.kshrd.asset_tracer_api.model.entity.Organization;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.request.OrganizationRequest;
import com.kshrd.asset_tracer_api.repository.builder.OrganizationBuilder;
import com.kshrd.asset_tracer_api.repository.builder.UserAppBuilder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface OrganizationRepository {
    @Results(id = "organizationMapper",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(column = "created_at", property = "createdAt"),
                    @Result(column = "created_by", property = "createdBy"),
                    @Result(column = "updated_at", property = "updatedAt"),
                    @Result(column = "updated_by", property = "updatedBy"),
                    @Result(column = "deleted_at", property = "deletedAt"),
                    @Result(column = "deleted_by", property = "deletedBy"),
                    @Result(column = "role_name", property = "roleName"),
                    @Result(column = "is_active", property = "isActive"),
                    @Result(column = "is_member", property = "isMember"),
                    @Result(column = "is_active", property = "isActive"),
                    @Result(column = "total_user", property = "totalUser"),
                    @Result(column = "id", property = "users",
                            many = @Many(select = "com.kshrd.asset_tracer_api.repository.UserAppRepository.getAllUsersByOrganizationId")
                    ),
            })
    @Select("""
            select * from organization where deleted_at is null order by created_at desc
            """)
    List<Organization> getAllOrganizations();

    @Select("""
            select id from organization
            where id = #{orgId}
            and deleted_at is null order by created_at desc
            """)
    @ResultMap("organizationMapper")
    List<Organization> getAllUsersByOrgId(UUID orgId);

    @Results(id = "orgMapper",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(column = "created_at", property = "createdAt"),
                    @Result(column = "created_by", property = "createdBy"),
                    @Result(column = "updated_at", property = "updatedAt"),
                    @Result(column = "updated_by", property = "updatedBy"),
                    @Result(column = "deleted_at", property = "deletedAt"),
                    @Result(column = "deleted_by", property = "deletedBy"),
                    @Result(column = "role_name", property = "roleName"),
                    @Result(column = "id", property = "users",
                            many = @Many(select = "com.kshrd.asset_tracer_api.repository.UserAppRepository.getAllRequestUserByOrgId")
                    ),
            })
//    @Select("""
//            select distinct o.* from organization o
//            inner join organization_detail od on o.id = od.organization_id
//            where od.is_active = false
//            and od.is_member =false
//            and od.status = 'is_requested'
//            and od.organization_id in (
//                    select distinct organization_id from organization_detail od where od.status = 'is_owner' and od.user_id = #{getCurrentUserId}
//                )
//            and o.deleted_at is null
//            order by o.created_at desc
//            """)
    @SelectProvider(type = UserAppBuilder.class, method = "selectAllRequestUsersByUserSql")
    List<Organization> getAllRequestUsersByUserId(String search, String sort, UUID getCurrentUserId);

    @ResultMap("organizationMapper")
    @Select("""
            select * from organization where deleted_at is null order by created_at desc offset #{size} * (#{page}-1) limit #{size}
            """)
    List<Organization> getAllOrganizationWithFilter(Integer page, Integer size);


    @ResultMap("organizationMapper")
    @Select("""
            select id, name, code, address, logo, created_at
            from organization
            where id = #{organizationId} and deleted_at is null
            """)
    Organization getOrganizationById(UUID organizationId);

    @Select("""
             select o.id, o.name organization_name, od.is_active, od.is_member, od.status, r.name from organization o
             inner join organization_detail od on od.organization_id = o.id
             inner join role r on od.role_id = r.id
             where od.user_id = #{userId} and od.organization_id = #{orgId}
            """)
    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class)
    @Result(column = "is_active", property = "isActive")
    @Result(column = "is_member", property = "isMember")
    @Result(column = "created_at", property = "createdAt")
    @Result(column = "organization_name", property = "organizationName")
    @Result(column = "username", property = "owner")
    @Result(column = "user_image", property = "userImage")
    @Result(column = "logo", property = "logo")
    Organization getOrganizationByOrgIdWithUserId(UUID userId, UUID orgId);

    @Select("""
            SELECT id FROM organization
            WHERE code = #{code} AND deleted_at IS NULL
            """)
    Organization getOrganizationByCode(String code);


    @Select("""
            INSERT INTO organization(name, code, address, logo, created_by)
            VALUES (#{request.name}, #{code}, #{request.address}, #{request.logo}, #{getCurrentUserId})
            RETURNING id
            """)
    UUID addOrganization(@Param("request") OrganizationRequest organizationRequest, String code, UUID getCurrentUserId);

    @Select("""
            insert into organization_detail(user_id, organization_id, is_active, role_id, created_by, status)
            values(#{userId}, #{organizationId}, true, (select id from role where name = 'ADMIN'), #{getCurrentUserId}, 'is_owner')
            returning organization_id
            """)
    UUID addOrganizationDetail(UUID userId, UUID organizationId, UUID getCurrentUserId);

    @Select("""
            insert into organization_detail(user_id, organization_id, is_active, role_id, created_by, status)
            values(#{userId}, #{organizationId}, false, (select id from role where name = 'USER'), #{getCurrentUserId}, 'is_requested')
            returning organization_id
            """)
    UUID joinOrganization(UUID userId, UUID organizationId, UUID getCurrentUserId);

    @Select("""
            update organization_detail set status = 'is_requested'
            where user_id = #{userId} and organization_id = #{organizationId}
            returning organization_id
            """)
    UUID joinOrganizationWhenUserWasRejected(UUID userId, UUID organizationId);

    @Select("""
            UPDATE organization
            SET name = #{request.name} , address = #{request.address} ,
            logo = #{request.logo} , updated_at = CURRENT_TIMESTAMP
            WHERE id = #{organizationId}
            """)
    UUID updateOrganization(@Param("request") OrganizationRequest organizationRequest, UUID organizationId);


    @Update("""
            UPDATE organization
            SET deleted_at = CURRENT_TIMESTAMP
            WHERE id = #{organizationId}
            """)
    boolean deleteOrganization(UUID organizationId);

    @SelectProvider(type = OrganizationBuilder.class, method = "selectOrganizationSql")
    @ResultMap("organizationMapper")
    List<Organization> getAllOrganizationsByByUserId(@Param("userId") UUID userId, @Param("search") String search, @Param("sort") String sort, UUID getCurrentUserId);

    @Select("""
            select o.id, o.name, od.is_active, od.is_member, od.status, r.name from organization o 
            inner join organization_detail od on od.organization_id = o.id
            inner join role r on od.role_id = r.id
            where od.user_id = #{user} and od.organization_id = #{orgId}
            """)
    Organization getOrganizationByUserId(UUID userId, UUID orgId);

    @Select("""
            update organization_detail set is_active = true, is_member = true, status = 'is_approved'
            where user_id = #{userId} and organization_id = #{organizationId}
            returning user_id
            """)
    UUID approveUserRequestJoin(UUID userId, UUID organizationId);

    @Select("""
            update organization_detail set is_active = false, is_member = false, status = 'is_rejected',
            updated_at = CURRENT_TIMESTAMP, updated_by = #{getCurrentUserId}
            where user_id = #{userId} and organization_id = #{organizationId}
            returning user_id
            """)
    UUID rejectUserRequestJoin(UUID userId, UUID organizationId, UUID getCurrentUserId);

    @Results(id = "userMap",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(column = "created_at", property = "createdAt"),
            })
    @SelectProvider(type = UserAppBuilder.class, method = "selectUserSql")
    List<UserApp> getAllJoinedUsersByOrgId(UUID orgId, String search, String sort, UUID getCurrentUserId);

    @Select("""
            """)
    List<Organization> getAllUserJoined(UUID orgId, String search, String sort, UUID currentUser);

    @Select("""
            select count(*) from user_acc ua
            inner join organization_detail od
            inner join role r on od.role_id = r.id
            on ua.id = od.user_id
            where od.organization_id = #{orgId}
            and od.is_active = true
            """)
    Integer getCountAllUserJoined(UUID orgId);

    @Select("""
            select ua.id id, ua.name name, ua.gender gender, ua.email email, ua.phone phone, ua.address address,
            ua.image image, ua.created_at created_at, r.name
            from user_acc ua
            inner join organization_detail od
            inner join role r on od.role_id = r.id
            on ua.id = od.user_id
            where od.organization_id = #{orgId}
            and od.user_id <> #{currentUser}
            and od.is_active = false
            and od.is_member = false
            order by ua.created_at desc
            offset #{size} * (#{page}-1) limit #{size}
            """)
    @ResultMap("userMap")
    List<UserApp> getAllNotificationRequest(UUID orgId,Integer page, Integer size, UUID currentUser);

    @Select("""
            select count(ua.*)
            from user_acc ua
            inner join organization_detail od
            inner join role r on od.role_id = r.id
            on ua.id = od.user_id
            where od.organization_id = #{orgId}
            and od.user_id <> #{currentUser}
            and od.is_active = false
            and od.is_member = false
            """)
    Integer getCountUserRequest(UUID orgId);

    @SelectProvider(value = OrganizationBuilder.class, method = "selectAllInvitationFormOrganization")
    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class)
    @Result(column = "is_active", property = "isActive")
    @Result(column = "is_member", property = "isMember")
    @Result(column = "created_at", property = "createdAt")
    @Result(column = "organization_name", property = "organizationName")
    @Result(column = "username", property = "owner")
    @Result(column = "user_image", property = "userImage")
    @Result(column = "logo", property = "logo")
    List<Organization> getAllInvitationFromOrganization(Integer page, Integer size, String search, String sort, UUID getCurrentUserId);

    @SelectProvider(value = OrganizationBuilder.class, method = "selectCountInvitationFormOrganization")
    Integer getCountInvitationFromOrganization(String search, UUID getCurrentUserId);

    @Result(column = "created_at", property = "createdAt")
    @SelectProvider(value = OrganizationBuilder.class, method = "getAllUsersNotificationSql")
    List<UserApp> getAllNotificationsByOrgId(UUID orgId, Integer page, Integer size, String search, String sort);

    @SelectProvider(value = OrganizationBuilder.class, method = "getCountAllUsersNotificationSql")
    Integer getCountAllNotificationsByOrgId(UUID orgId, String search);

    @Select("""
            select o.*, od.is_active, od.is_member, od.status, r.name role_name, ua.image userImage, ua.name userName,
            (select count(*)from organization_detail od where od.organization_id = o.id and od.status not in ('is_invited', 'is_rejected', 'is_removed', 'is_requested')) total_user
            from organization o
            inner join organization_detail od 
            on od.organization_id = o.id
            inner join user_acc ua 
            on od.user_id = ua.id
            inner join role r
            on od.role_id = r.id
            where od.is_active = true
            and od.is_member = false
            and od.status = 'is_owner'
            and od.user_id = #{getCurrentUserId}
            """)
    @ResultMap("organizationMapper")
    List<Organization> getAllOrganizationsByOwner(UUID getCurrentUserId);

    @Select("""
            select o.id orgId, o.name orgName, o.logo orgLogo, od.status, od.created_at createdAt, od.updated_at updatedAt,
            (select distinct email from user_acc where id = (select user_id from organization_detail where status = 'is_owner' and organization_id = od.organization_id)) email
            from organization o
            inner join organization_detail od on od.organization_id = o.id
            where od.status in ('is_approved', 'is_rejected', 'is_invited')
            and od.user_id = #{getCurrentUserId}
            """)
    List<NotificationByUser> getAllNotificationByUser(UUID getCurrentUserId);
}
