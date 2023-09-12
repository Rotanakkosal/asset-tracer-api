package com.kshrd.asset_tracer_api.repository.builder;

import org.apache.ibatis.jdbc.SQL;

import java.util.UUID;

public class NormalCategoryBuilder {
    public String selectNormalCategorySql(UUID orgId, Integer page, Integer size, String name, String sort) {
        return new SQL(){{
            SELECT("n.*, s.name super_category_name, s.icon super_category_icon, (select count(*) from item_detail where category_id = n.id and deleted_at is null) usage");
            FROM("normal_category n");
            INNER_JOIN("super_category s on n.super_category_id = s.id");
            WHERE("n.organization_id = #{orgId}");
            WHERE("n.deleted_at is null");
            if(name != null && name != "") {
                AND();
                WHERE("n.name ilike concat(#{name},'%') or s.name ilike concat(#{name},'%')");
            }

            if(sort != null && sort.equals("name")) {
                ORDER_BY("n.name ASC");
            }
            else if(sort != null && sort.equals("createdAt")) {
                ORDER_BY("n.created_at ASC");
            }
            else{
                ORDER_BY("n.created_at DESC");
            }

            OFFSET("#{size} * (#{page}-1)");
            LIMIT("#{size}");
        }}.toString();
    }

    public String selectCountNormalCategory(UUID orgId, String name, String superCategoryName){
        return new SQL(){
            {
                SELECT("count(*)");
                FROM("normal_category n");
                INNER_JOIN("super_category s on n.super_category_id = s.id");
                WHERE("n.organization_id = #{orgId}");
                WHERE("n.deleted_at is null");
                if(name != null && name != "") {
                    AND();
                    WHERE("n.name ilike concat(#{name},'%') or s.name ilike concat(#{name},'%')");
                }
            }
        }.toString();
    }
}
