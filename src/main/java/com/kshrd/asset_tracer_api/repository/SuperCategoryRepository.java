package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.config.UuidTypeHandler;
import com.kshrd.asset_tracer_api.model.entity.SuperCategory;
import com.kshrd.asset_tracer_api.model.request.SuperCategoryRequest;
import com.kshrd.asset_tracer_api.repository.builder.SuperCategoryBuilder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface SuperCategoryRepository {
    @Results(id = "superCategoryMapper",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(column = "count_asset", property = "countAsset"),
                    @Result(column = "created_at", property = "createdAt"),
                    @Result(column = "created_by", property = "createdBy"),
                    @Result(column = "updated_at", property = "updatedAt"),
                    @Result(column = "updated_by", property = "updatedBy"),
                    @Result(column = "deleted_at", property = "deletedAt"),
                    @Result(column = "deleted_by", property = "deletedBy"),
                    @Result(column = "organization_id", property = "organizationId")
            })
    @SelectProvider(type = SuperCategoryBuilder.class, method = "selectSuperCategorySql")
    List<SuperCategory> getAllSuperCategories(UUID orgId, Integer page, Integer size, String name, String sort);

    @SelectProvider(type = SuperCategoryBuilder.class, method = "selectCountAllSuperCategory")
    String getCountAllSuperCategories(UUID orgId,String name);

    @ResultMap("superCategoryMapper")
    @Select("""
            SELECT * FROM super_category
            WHERE deleted_at IS NULL and organization_id = #{orgId}
            ORDER BY name
            """)
    List<SuperCategory> getAllSuperCategoryNames(UUID orgId);

    @ResultMap("superCategoryMapper")
    @Select("""
            SELECT * FROM super_category
            WHERE id = #{id} AND super_category.deleted_at IS NULL
            """)
    SuperCategory getSuperCategoryById(@Param("id") UUID superCategoryId);

    @Select("""
            INSERT INTO super_category(name, icon, created_by, organization_id)
            VALUES (#{request.name}, #{request.icon}, #{getCurrentUserId}, #{organizationId})
            RETURNING id
            """)
    UUID addSuperCategory(@Param("request") SuperCategoryRequest superCategoryRequest, UUID organizationId, UUID getCurrentUserId);

    @Select("""
            UPDATE super_category
            SET name = #{request.name}, icon = #{request.icon}, updated_at = CURRENT_TIMESTAMP, updated_by = #{getCurrentById}
            WHERE id = #{id}
            RETURNING id
            """)
    UUID updateSuperCategory(@Param("request") SuperCategoryRequest superCategoryRequest, UUID id, UUID getCurrentById);

    @Select("""
            update super_category
            set deleted_at = CURRENT_TIMESTAMP,
            deleted_by = #{getCurrentById}
            where id = #{superCategoryId}
            returning id
            """)
    UUID deleteSuperCategory(UUID superCategoryId, UUID getCurrentById);

    @Select("""
            select exists(select * from super_category where name = #{name} and deleted_at is null and organization_id = #{orgId})
            """)
    Boolean isExistCategoryName(String name, UUID orgId);

    @Select("""
            select count(*) from super_category
            where deleted_at is null
            """)
    Integer getCountData();


    @Results(id = "superMapper",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(column = "created_at", property = "createdAt"),
                    @Result(column = "created_by", property = "createdBy"),
                    @Result(column = "updated_at", property = "updatedAt"),
                    @Result(column = "updated_by", property = "updatedBy"),
                    @Result(column = "deleted_at", property = "deletedAt"),
                    @Result(column = "deleted_by", property = "deletedBy"),
                    @Result(column = "organization_id", property = "organizationId"),
                    @Result(column = "qty", property = "countAsset",
                        one = @One(select = "com.kshrd.asset_tracer_api.repository.AssetRepository.getAllAssetBySuperCategory")
                    )
            })

//    @Select("""
//            select sc.organization_id count_asset
//            from super_category sc
//            inner join organization o on o.id = sc.organization_id
//            where sc.deleted_at is null
//            and sc.organization_id = #{orgId}
//            """)
    @Select("""
            select sc.id, sc.name,
                   (select case when sum(qty) is null then '0' else sum(qty) end total from item_detail id
                       inner join normal_category nc
                           on nc.id = id.category_id where nc.super_category_id = sc.id)
                        
                   from super_category sc where sc.organization_id = #{orgId}
            """)
    List<SuperCategory> getAllAssetInSuperCategory(UUID orgId);


    @Select("""
            select sc.id id, sc.name name,
               (
                    select case when sum(qty) is null then '0' else sum(qty) end total from item_detail id
                    inner join normal_category nc on nc.id = id.category_id
                    where nc.super_category_id = sc.id and id.deleted_at is null
                ) count_asset
            from super_category sc where sc.organization_id = #{orgId}
            """)
    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class)
    @Result(column = "name", property = "name")
    @Result(column = "count_asset", property = "countAsset")
    List<SuperCategory> getCountAssetBySuperCategory(UUID orgId);
}
