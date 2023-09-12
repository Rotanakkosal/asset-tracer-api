package com.kshrd.asset_tracer_api.repository.builder;

import com.kshrd.asset_tracer_api.model.request.ItemDetailRequest;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public class ItemBuilder {

    public String selectItemSql(UUID orgId, Integer page, Integer size, String name, String normalCategoryName, String sort){
        return new SQL(){
            {
                SELECT("""
                        itd.*, i.invoice_code, nc.name normal_category_name, nc.icon normal_category_icon, nc.id normal_category_id,
                        (select count(*) from asset where item_detail_id = itd.id) countSet,
                        (itd.qty -(select count(*) from asset where item_detail_id = itd.id)) countUnset
                        """);
                FROM("item_detail itd");
                INNER_JOIN("normal_category nc on itd.category_id = nc.id");
                LEFT_OUTER_JOIN("invoice i on i.id = itd.invoice_id");
                WHERE("itd.organization_id = #{orgId}");
                WHERE("itd.deleted_at is null");

                if(name != null && name != ""){
                    AND();
                    WHERE("itd.name ilike #{name} || '%' ");
                }

                if(normalCategoryName != null && normalCategoryName != ""){
                    AND();
                    WHERE("nc.name ilike #{normalCategoryName} || '%' ");
                }

                if(sort != null && sort.equals("name")){
                    ORDER_BY("itd.name asc");
                }
                else if(sort != null && sort.equals("qty")){
                    ORDER_BY("itd.qty asc");
                }
                else if(sort != null && sort.equals("unitPrice")){
                    ORDER_BY("itd.unit_price asc");
                }
                else if(sort != null && sort.equals("createdAt")){
                    ORDER_BY("itd.created_at asc");
                }
                else {
                    ORDER_BY("itd.created_at DESC");
                }

                OFFSET("#{size} * (#{page}-1)");
                LIMIT("#{size}");
            }
        }.toString();
    }

    public String selectCountItemSql(UUID orgId, String name, String normalCategoryName) {
        return new SQL(){
            {
                SELECT("count(itd.*)");
                FROM("item_detail itd");
                INNER_JOIN("normal_category nc on itd.category_id = nc.id");
                if(name != null && name != ""){
                    AND();
                    WHERE("itd.name ilike #{name} || '%' ");
                }
                if(normalCategoryName != null && normalCategoryName != ""){
                    AND();
                    WHERE("nc.name ilike #{normalCategoryName} || '%' ");
                }
                WHERE("itd.organization_id = #{orgId}");
                WHERE("itd.deleted_at is null");
            }
        }.toString();
    }
}