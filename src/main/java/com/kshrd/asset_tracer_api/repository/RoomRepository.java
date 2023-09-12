package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.config.UuidTypeHandler;
import com.kshrd.asset_tracer_api.model.entity.Room;
import com.kshrd.asset_tracer_api.model.request.RoomRequest;
import com.kshrd.asset_tracer_api.repository.builder.RoomBuilder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface RoomRepository {
    @Results(id = "roomMapper",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(column = "created_at", property = "createdAt"),
                    @Result(column = "created_by", property = "createdBy"),
                    @Result(column = "updated_at", property = "updatedAt"),
                    @Result(column = "updated_by", property = "updatedBy"),
                    @Result(column = "deleted_at", property = "deletedAt"),
                    @Result(column = "deleted_by", property = "deletedBy"),
                    @Result(column = "organization_id", property = "organizationId"),
                    @Result(column = "created_by_username", property = "createdByUsername"),
                    @Result(column = "organization_name", property = "organizationName")
            })
    @SelectProvider(value = RoomBuilder.class, method = "selectRoomSql")
    List<Room> getAllRooms(UUID orgId, Integer page, Integer size, String search, String sort);

    @SelectProvider(value = RoomBuilder.class, method = "selectCountAllRooms")
    Integer getCountAllRooms(UUID orgId, String search);

    @Select("""
            select r.*, ua.name created_by_username
            from room r
            inner join user_acc ua on ua.id = r.created_by
            where r.id = #{id} and r.deleted_at is null
            """)
    @ResultMap("roomMapper")
    Room getRoomById(@Param("id") UUID roomId);

    @Select("""
            insert into room(name, type, floor, description, image, organization_id, created_by)
            values(#{req.name}, #{req.type}, #{req.floor},
            #{req.description}, #{req.image}, #{req.organizationId}, #{getCurrentUser})
            returning id
            """)
    UUID addRoom(@Param("req") RoomRequest roomRequest, UUID getCurrentUser);

    @Select("""
            update room set name = #{req.name}, type = #{req.type}, floor = #{req.floor},
            description = #{req.description}, image = #{req.image}, 
            updated_at = CURRENT_TIMESTAMP, updated_by = #{getCurrentUserId}
            where id = #{roomId} returning id
            """)
    UUID updateRoom(@Param("req") RoomRequest roomRequest, UUID roomId, UUID getCurrentUserId);

    @Select("""
            update room
            set deleted_at = CURRENT_TIMESTAMP, deleted_by = #{getCurrentUserId}
            where id = #{id} returning id;
            """)
    UUID deleteRoom(@Param("id") UUID roomId, UUID getCurrentUserId);

    @Select("""
            select r.*, ua.name created_by_username
            from room r
            inner join user_acc ua on ua.id = r.created_by
            where r.deleted_at is null and r.organization_id = #{orgId}
            order by r.created_at asc
            offset (#{size} * (#{page}-1)) limit #{size}
            """)
    @ResultMap("roomMapper")
    List<Room> getAllRoomByCreateDate(UUID orgId, Integer page, Integer size, String search, String prop);

    @Select("""
            select r.*, ua.name created_by_username
            from room r
            inner join user_acc ua on ua.id = r.created_by
            where (r.floor ilike '%' || #{search} ||'%')
            and r.deleted_at is null and r.organization_id = #{orgId}
            order by r.created_at desc
            offset (#{size} * (#{page}-1)) limit #{size}
            """)
    @ResultMap("roomMapper")
    List<Room> getAllRoomByFloor(UUID orgId, Integer page, Integer size, String search, String prop);

    @Select("""
            select r.* from room r
            inner join organization o on o.id = r.organization_id
            where (r.name ilike '%' || #{search} ||'%')
            and r.deleted_at is null and r.organization_id = #{orgId}
            order by r.created_at desc
            offset (#{size} * (#{page}-1)) limit #{size}
            """)
    @ResultMap("roomMapper")
    List<Room> getAllRoomByName(UUID orgId, Integer page, Integer size, String search, String prop);

    @Select("""
            select id, name from room
            where deleted_at is null
            and organization_id = #{orgId}
            """)
    List<Room> getAllRoomByOrganizationId(UUID organizationId);

    @Select("""
            select count(*) from room
            where deleted_at is null
            and organization_id = #{orgId}
            """)
    Integer getCountData(UUID orgId);

    @Select("""
            select count(*) from room
            where deleted_at is null
            and organization_id = #{orgId}
            """)
    Integer getCountRooms(UUID orgId);
}
