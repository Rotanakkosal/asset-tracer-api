package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.config.UuidTypeHandler;
import com.kshrd.asset_tracer_api.model.dto.AssetDTO;
import com.kshrd.asset_tracer_api.model.entity.Asset;
import com.kshrd.asset_tracer_api.model.request.AssetRequest;
import com.kshrd.asset_tracer_api.model.request.AssetRequest2;
import com.kshrd.asset_tracer_api.repository.builder.AssetBuilder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface AssetRepository {

    @Results(
            id = "assetMapper",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(column = "label_code", property = "labelCode"),
                    @Result(column = "serial_code", property = "serialCode"),
                    @Result(column = "asset_name", property = "assetName"),
                    @Result(column = "room_id", property = "roomId"),
                    @Result(column = "room_name", property = "roomName"),
                    @Result(column = "room_image", property = "roomImage"),
                    @Result(column = "created_at", property = "createdAt"),
                    @Result(column = "created_by", property = "createdBy"),
                    @Result(column = "organization_id", property = "organizationId"),
                    @Result(column = "item_detail_id", property = "itemDetail",
                            one = @One(select = "com.kshrd.asset_tracer_api.repository.ItemDetailRepository.getItemDetailById")
                    )
            }
    )

    @SelectProvider(value = AssetBuilder.class, method = "selectAssetSql")
    List<Asset> getAllAssets(UUID orgId, Integer page, Integer size, String assetName, String status, String roomName, String normalCategoryName, String sort);

    @SelectProvider(value = AssetBuilder.class, method = "selectCountAssets")
    Integer getCountAssets(UUID orgId, String assetName, String status, String roomName, String normalCategoryName);

    @Select("""
            select a.id, a.image, a.label_code, a.serial_code, a.status, a.owner, a.type,
                a.item_detail_id, a.created_at, a.created_by, r.id room_id,
                r.name room_name, r.image room_image, a.id, a.organization_id, a.description
            from asset a
            left join room r on r.id = a.room_id
            where a.id = #{assetId} and a.deleted_at is null
            """)
    @ResultMap("assetMapper")
    Asset getAssetById(@Param("assetId") UUID assetId);

    @Select("""
            insert into asset(item_detail_id, label_code, serial_code, status, owner, room_id, organization_id, created_by)
            values(#{req.itemDetailId}, #{req.labelCode}, #{req.serialCode},#{req.status} ,#{req.owner},
            #{req.roomId}, #{req.organizationId}, #{getCurrentUser})
            returning id
            """)
    UUID addAsset(@Param("req") AssetRequest assetRequest, UUID getCurrentUser);

    @Select("""
            update asset set label_code = #{req.labelCode}, serial_code = #{req.serialCode}, room_id = #{req.roomId},
            status = #{req.status}, owner = #{req.owner}, updated_at = CURRENT_TIMESTAMP, updated_by = #{getCurrentUserId}
            where id = #{assetId}
            returning id
            """)
    UUID updateAsset(@Param("req") AssetRequest2 assetRequest2, UUID assetId, UUID getCurrentUserId);

    @Select("""
            update asset
            set deleted_at = CURRENT_TIMESTAMP
            where id = #{assetId}
            returning id
            """)
    UUID deleteAsset(UUID assetId);

    @Select("""
            select count(*) from asset where deleted_at is null and organization_id = #{orgId}
            """)
    Integer getCountData(UUID orgId);


    @Select("""
            select a.*, r.name room_name, r.image room_image, i.name name
            from asset a
                     left join room r on r.id = a.room_id
                     inner join item_detail i on i.id = a.item_detail_id
                     inner join normal_category nc on i.category_id = nc.id
                     inner join super_category sc on nc.super_category_id = sc.id
            where a.organization_id = #{orgId}
            and a.deleted_at is null
            """)
    @ResultMap("assetMapper")
    List<Asset> getAllAssetsByOrgId(UUID orgId);

    @Select("""
            select a.*, r.name room_name, r.image room_image, i.name name
            from asset a
                     left join room r on r.id = a.room_id
                     inner join item_detail i on i.id = a.item_detail_id
                     inner join normal_category nc on i.category_id = nc.id
                     inner join super_category sc on nc.super_category_id = sc.id
            where a.organization_id = #{orgId} and a.deleted_at is null
            order by a.created_at desc offset #{size} * (#{page}-1) limit #{size}
            """)
    @ResultMap("assetMapper")
    List<Asset> getLastThreeAssets(UUID orgId, Integer page, Integer size);

    @SelectProvider(value = AssetBuilder.class, method = "selectCountAllAsset")
    Integer getCountAssetByStatus(UUID orgId, String status);

    @Select("""
            select sum(qty) from item_detail
            where organization_id = #{orgId}
            and deleted_at is null
            """)
    Integer getCountAllAssets(UUID orgId);

    @Select("""
          
            select sum(it.qty) from super_category sc
            inner join normal_category nc on sc.id = nc.super_category_id
            inner join item_detail it on it.category_id  = nc.id
            where sc.deleted_at is null and sc.id = #{superId}
            and sc.organization_id = #{orgId}
            """)
    Integer getAllAssetBySuperCategory(UUID orgId, UUID superId);

    @Select("""
            select sum(qty) from item_detail id
            inner join normal_category nc on id.category_id = nc.id
            where id.deleted_at is null
              and id.organization_id = #{orgId}
              and id.category_id = #{normalId}
            """)
    Integer getAllAssetByNormalCategory(UUID normalId, UUID orgId);

    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class)
    @Result(column = "label_code", property = "labelCode")
    @Result(column = "serial_code", property = "serialCode")
    @Result(column = "asset_name", property = "assetName")
    @Result(column = "room_name", property = "roomName")
    @Result(column = "room_image", property = "roomImage")
    @Result(column = "created_at", property = "createdAt")
    @Result(column = "created_by", property = "createdBy")
    @Result(column = "organization_id", property = "organizationId")
    @Select("""
            select * from asset where item_detail_id = #{itemDetailId} and deleted_at is null
            """)
    List<Asset> getAllAssetByItemId (UUID itemDetailId);

    @Select("""
            select exists(select * from asset where label_code = #{labelCode} and organization_id = #{orgId})
            """)
    Boolean isDuplicateLabelCode(String labelCode, UUID orgId);

    @Select("""
            select exists(select * from asset where serial_code = #{serialCode} and organization_id = #{orgId})
            """)
    Boolean isDuplicateSerialCode(String serialCode, UUID orgId);

    @Select("""
            update asset set status = #{status}
            where id = #{assetId}
            returning *
            """)
    Asset changeStatus(UUID assetId, String status);
}
