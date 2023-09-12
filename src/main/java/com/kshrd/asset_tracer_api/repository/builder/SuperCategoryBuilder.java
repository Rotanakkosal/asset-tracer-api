package com.kshrd.asset_tracer_api.repository.builder;

import org.apache.ibatis.jdbc.SQL;

import java.util.UUID;

public class SuperCategoryBuilder {
    public String selectSuperCategorySql(UUID orgId, Integer page, Integer size, String name, String sort) {
        return new SQL(){{
            SELECT("sc.*, (select count(*) from normal_category where super_category_id = sc.id and deleted_at is null) usage");
            FROM("super_category sc");
            WHERE("sc.deleted_at IS NULL");
            WHERE("sc.organization_id = #{orgId}");
            if(name != null && !name.isBlank() && !name.isEmpty()) {
                WHERE("name ilike #{name} || '%'");
            }
            if(sort !=null && sort.equals("name")){
                ORDER_BY("sc.name asc");
            }
            else if(sort !=null && sort.equals("createdAt")){
                ORDER_BY("sc.created_at asc");
            }
            else {
                ORDER_BY("sc.created_at desc");
            }
            OFFSET("#{size} * (#{page}-1)");
            LIMIT("#{size}");
        }}.toString();
    }

    public String selectCountAllSuperCategory(UUID orgId, String name){
        return new SQL(){
            {
                SELECT("count(*)");
                FROM("super_category");
                if(name != null && !name.isBlank() && !name.isEmpty()){
                    WHERE("name ilike #{name} || '%'");
                }
                WHERE("organization_id = #{orgId}");
                WHERE("deleted_at IS NULL");

            }
        }.toString();
    }
}


