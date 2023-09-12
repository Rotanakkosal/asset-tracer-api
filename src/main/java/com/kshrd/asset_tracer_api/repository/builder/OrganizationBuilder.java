package com.kshrd.asset_tracer_api.repository.builder;

import com.kshrd.asset_tracer_api.exception.UnauthorizedExceptionHandler;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.security.core.context.SecurityContextHolder;

import java.awt.*;
import java.util.UUID;

public class OrganizationBuilder {

    public String selectOrganizationSql(UUID userId, String search, String sort) {
        return new SQL(){{
            SELECT("""
                    o.*, r.name role_name, od.is_active, od.is_member, od.status, (select count(*)from organization_detail od where od.organization_id = o.id and od.status not in ('is_invited', 'is_rejected', 'is_removed', 'is_requested')) total_user
                    """);
            FROM("organization o");
            INNER_JOIN("organization_detail od on o.id = od.organization_id");
            INNER_JOIN("user_acc ua on ua.id = od.user_id");
            INNER_JOIN("role r on od.role_id = r.id");
            WHERE("ua.id = #{userId}");
            WHERE("o.deleted_at IS NULL");
            WHERE("od.status not in ('is_invited', 'is_rejected', 'is_removed')");
            if(search != null) {
                WHERE("o.name  ilike concat('%', #{search},'%')");
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

    public String selectAllInvitationFormOrganization(Integer page, Integer size, String search, String sort, UUID getCurrentUserId) {
        return new SQL(){{
            SELECT("o.id, o.name organization_name, o.logo, ua.name username, ua.image user_image, ua.email, od.is_active, od.is_member, od.status, r.name roleName, od.created_at created_at");
            FROM("organization o");
            INNER_JOIN("organization_detail od on o.id = od.organization_id");
            INNER_JOIN("role r on od.role_id = r.id");
            INNER_JOIN("user_acc ua on ua.id = o.created_by");
            WHERE("od.is_active = false");
            WHERE("od.is_member = false");
            WHERE("od.status = 'is_invited' ");
            WHERE("od.user_id = #{getCurrentUserId}");

            if(search != null && search != "") {
                AND();
                WHERE("o.name  ilike #{search} || '%'");
            }

            if(sort != null && sort.equals("name")){
                ORDER_BY("o.name asc");
            }
            else if(sort != null && sort.equals("owner")){
                ORDER_BY("ua.name asc");
            }
            else if(sort != null && sort.equals("createdAt")){
                ORDER_BY("o.created_at asc");
            }
            else {
                ORDER_BY("o.created_at desc");
            }
            OFFSET("#{size} * (#{page}-1)");
            LIMIT("#{size}");
        }}.toString();
    }

    public String selectCountInvitationFormOrganization(String search, UUID getCurrentUserId) {
        return new SQL(){{
            SELECT("count(o.*)");
            FROM("organization o");
            INNER_JOIN("organization_detail od on o.id = od.organization_id");
            INNER_JOIN("role r on od.role_id = r.id");
            INNER_JOIN("user_acc ua on ua.id = o.created_by");
            WHERE("od.is_active = false");
            WHERE("od.is_member = false");
            WHERE("od.status = 'is_invited' ");
            WHERE("od.user_id = #{getCurrentUserId}");
            if(search != null && search != "") {
                AND();
                WHERE("o.name  ilike #{search} || '%'");
            }
        }}.toString();
    }

    public  String  getAllUsersNotificationSql(UUID orgId, Integer page, Integer size, String search, String sort) {
        return new SQL(){{
            SELECT("*");
            FROM("user_acc ua");
            INNER_JOIN("organization_detail od on ua.id = od.user_id");
            WHERE("od.organization_id = #{orgId}");
            WHERE("od.status = 'is_requested'");
            if(search != null && search != "") {
                AND();
                WHERE("ua.name  ilike #{search} || '%'");
            }

            if(sort != null && sort.equals("name")){
                ORDER_BY("ua.name asc");
            }
            else if(sort != null && sort.equals("createdAt")) {
                ORDER_BY("od.created_at asc");
            }
            else {
                ORDER_BY("od.created_at DESC");
            }

            OFFSET("#{size} * (#{page}-1)");
            LIMIT("#{size}");

        }}.toString();
    }

    public  String  getCountAllUsersNotificationSql(UUID orgId, String search) {
        return new SQL(){{
            SELECT("count(*)");
            FROM("user_acc ua");
            INNER_JOIN("organization_detail od on ua.id = od.user_id");
            WHERE("od.organization_id = #{orgId}");
            WHERE("od.status = 'is_requested'");
            if(search != null && search != "") {
                AND();
                WHERE("ua.name  ilike #{search} || '%'");
            }

        }}.toString();
    }
}
