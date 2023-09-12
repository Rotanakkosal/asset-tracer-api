package com.kshrd.asset_tracer_api.repository.builder;

import org.apache.ibatis.jdbc.SQL;

import java.util.UUID;

public class UserAppBuilder {
    public String selectUserSql(UUID orgId, String search, String sort, UUID getCurrentUserId) {
        return new SQL(){{
            SELECT("""
                    ua.id id, ua.name name, ua.gender gender, ua.email email, ua.phone phone, ua.address address,
                    ua.image image, ua.created_at created_at, r.name
                    """);
            FROM("user_acc ua");
            INNER_JOIN("organization_detail od on ua.id = od.user_id");
            INNER_JOIN("role r on od.role_id = r.id");
            WHERE("od.organization_id = #{orgId}");
            WHERE("od.user_id <> #{getCurrentUserId}");
            WHERE("od.is_active = true");
            WHERE("od.is_member = true");

            if(search != null && search != "") {
                WHERE("ua.name  ilike #{search} || '%'");
            }

            if(sort != null && sort.equals("asc")){
                ORDER_BY("ua.name asc");
            }
            else if(sort != null && sort.equals("desc")){
                ORDER_BY("ua.name desc");
            }else {
                ORDER_BY("ua.created_at desc");
            }

        }}.toString();
    }

    public String selectAllRequestUsersByUserSql(String search, String sort, UUID getCurrentUserId) {
        return new SQL(){{
            SELECT("distinct o.*");
            FROM("organization o");
            INNER_JOIN("organization_detail od on o.id = od.organization_id");
            WHERE("od.is_active = false");
            WHERE("od.is_member =false");
            WHERE("od.status = 'is_requested'");
            WHERE("o.deleted_at is null");
            WHERE("""
                    od.organization_id in (
                        select distinct organization_id from organization_detail od where od.status = 'is_owner' and od.user_id = #{getCurrentUserId}
                    )
                    """);
            if(search != null) {
                AND();
                WHERE("o.name  ilike concat(#{search},'%')");
            }
            if(sort != null && sort.equals("asc")){
                ORDER_BY("o.name asc");
            }
            else if(sort != null && sort.equals("desc")){
                ORDER_BY("o.name desc");
            }else {
                ORDER_BY("o.created_at desc");
            }
        }}.toString();
    }
}
