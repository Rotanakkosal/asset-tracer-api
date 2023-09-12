package com.kshrd.asset_tracer_api.repository.builder;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.core.annotation.Order;

import java.util.UUID;

public class AssetBuilder {
    public String selectAssetSql(UUID orgId, Integer page, Integer size, String assetName, String status, String roomName, String normalCategoryName, String sort){
        return new SQL(){
            {
                SELECT("a.*, r.name room_name, r.image room_image,  i.name asset_name");
                FROM("asset a");
                INNER_JOIN("item_detail i on i.id = a.item_detail_id");
                LEFT_OUTER_JOIN("room r on r.id = a.room_id");
                INNER_JOIN("normal_category nc on i.category_id = nc.id");

                if(assetName != null && assetName != ""){
                    AND();
                    WHERE("i.name ilike #{assetName} || '%' ");
                }
                if(status != null && status != ""){
                    AND();
                    WHERE("a.status ilike #{status} || '%' ");
                }
                if(roomName != null && roomName != ""){
                    AND();
                    WHERE("r.name ilike #{roomName} || '%' ");
                }
                if(normalCategoryName != null && normalCategoryName != ""){
                    AND();
                    WHERE("nc.name ilike #{normalCategoryName} || '%' ");
                }

                WHERE("a.deleted_at is null");
                WHERE("a.organization_id = #{orgId}");
                ORDER_BY("a.created_at DESC");
                OFFSET("#{size} * (#{page}-1)");
                LIMIT("#{size}");
            }
        }.toString();
    }

    public String selectCountAssets(UUID orgId, String assetName, String status, String roomName, String normalCategoryName){
        return new SQL(){
            {
                SELECT ("count(a.*)");
                FROM ("asset a");
                INNER_JOIN("item_detail i on i.id = a.item_detail_id");
                LEFT_OUTER_JOIN("room r on r.id = a.room_id");
                INNER_JOIN("normal_category nc on i.category_id = nc.id");

                if(assetName != null && assetName != ""){
                    AND();
                    WHERE("i.name ilike #{assetName} || '%' ");
                }
                if(status != null && status != ""){
                    AND();
                    WHERE("a.status ilike #{status} || '%' ");
                }
                if(roomName != null&& roomName != "" ){
                    AND();
                    WHERE("r.name ilike #{roomName} || '%' ");
                }
                if(normalCategoryName != null && normalCategoryName != ""){
                    AND();
                    WHERE("nc.name ilike #{normalCategoryName} || '%' ");
                }

                WHERE("a.deleted_at IS NULL");
                WHERE("a.organization_id = #{orgId}");
            }
        }.toString();
    }

    public String selectCountAllAsset(UUID orgId,String status){
        return new SQL(){
            {
                SELECT("count(id.*)");
                FROM("asset a");
                INNER_JOIN("item_detail id on id.id = a.item_detail_id");
                if(status != null && status != ""){
                    AND();
                    WHERE("a.status ilike #{status} || '%' ");
                }
                WHERE("a.deleted_at IS NULL");
                WHERE("a.organization_id = #{orgId}");
            }
        }.toString();
    }
}