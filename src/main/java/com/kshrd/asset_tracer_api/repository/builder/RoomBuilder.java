package com.kshrd.asset_tracer_api.repository.builder;

import org.apache.ibatis.jdbc.SQL;

import java.util.UUID;

public class RoomBuilder {
    public String selectRoomSql(UUID orgId, Integer page, Integer size, String search, String sort) {
        return new SQL() {{
            SELECT("r.*, ua.name created_by_username, o.name organization_name, (select count(*) from asset where room_id = r.id) usage");
            FROM("room r");
            INNER_JOIN("user_acc ua on ua.id = r.created_by");
            INNER_JOIN("organization o on r.organization_id = o.id");
            WHERE("r.deleted_at is null");
            WHERE("r.organization_id = #{orgId}");
            if (search != null && search != "") {
                AND();
                WHERE("""
                        r.name ilike concat(#{search},'%')
                        or r.type ilike concat(#{search},'%')
                        or r.floor ilike concat(#{search},'%')
                        """);
            }

            if (sort != null && sort.equals("name")) {
                ORDER_BY("r.name asc");
            } else if (sort != null && sort.equals("type")) {
                ORDER_BY("r.type asc");
            } else if (sort != null && sort.equals("floor")) {
                ORDER_BY("r.floor asc");
            } else {
                ORDER_BY("r.created_at DESC");
            }

            OFFSET("#{size} * (#{page}-1)");
            LIMIT("#{size}");
        }}.toString();
    }

    public String selectCountAllRooms(UUID orgId, String search) {
        return new SQL() {{
            SELECT("count(r.*)");
            FROM("room r");
            WHERE("r.deleted_at is null");
            WHERE("r.organization_id = #{orgId}");
            if (search != null) {
                AND();
                WHERE("""
                        r.name ilike concat(#{search},'%')
                        or r.type ilike concat(#{search},'%')
                        or r.floor ilike concat(#{search},'%')
                        """);
            }
        }}.toString();
    }
}
