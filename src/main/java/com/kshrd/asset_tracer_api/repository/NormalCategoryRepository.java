package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.config.UuidTypeHandler;
import com.kshrd.asset_tracer_api.model.dto.NormalCategoryDTO;
import com.kshrd.asset_tracer_api.model.entity.NormalCategory;
import com.kshrd.asset_tracer_api.model.request.NormalCategoryRequest;
import com.kshrd.asset_tracer_api.repository.builder.NormalCategoryBuilder;
import com.kshrd.asset_tracer_api.repository.builder.SuperCategoryBuilder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface NormalCategoryRepository {
    @Results(
            id = "normalCategoryMapper",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(column = "name", property = "name"),
                    @Result(column = "icon", property = "icon"),
                    @Result(column = "created_at", property = "createdAt"),
                    @Result(column = "created_by", property = "createdBy"),
                    @Result(column = "updated_at", property = "updatedAt"),
                    @Result(column = "updated_by", property = "updatedBy"),
                    @Result(column = "deleted_at", property = "deletedAt"),
                    @Result(column = "deleted_by", property = "deletedBy"),
                    @Result(column = "super_category_id", property = "superCategoryId"),
                    @Result(column = "super_category_name", property = "superCategoryName"),
                    @Result(column = "super_category_icon", property = "superCategoryIcon"),
                    @Result(column = "organization_id", property = "organizationId"),
            }
    )
    @SelectProvider(value = NormalCategoryBuilder.class, method = "selectNormalCategorySql")
    List<NormalCategory> getAllNormalCategories(UUID orgId, Integer page, Integer size, String name, String sort);

    @SelectProvider(value = NormalCategoryBuilder.class, method = "selectCountNormalCategory")
    Integer getCountAllNormalCategories(UUID orgId, String name, String superCategoryName);

    @Select("""
            select * from normal_category where deleted_at is null and organization_id = #{orgId}
            """)
    List<NormalCategory> getAllNormalCategoriesByOrg(UUID orgId);

    @ResultMap("normalCategoryMapper")
    @Select("""
            select n.*, s.name super_category_name
            , s.icon super_category_icon, super_category_id
             from normal_category n
            inner join super_category s on n.super_category_id = s.id
            where n.id = #{normalCategoryId} and n.deleted_at is null
            """)
    NormalCategory getNormalCategoryById(UUID normalCategoryId);

    @Select("""
            insert into normal_category(name, icon,created_by, super_category_id, organization_id)
            values(#{req.name}, #{req.icon}, #{getCurrentUser}, #{req.superCategoryId}, #{req.organizationId})
            returning id
            """)
    UUID addNormalCategory(@Param("req") NormalCategoryRequest normalCategoryRequest, UUID getCurrentUser);

    @Select("""
            UPDATE normal_category
            SET name = #{req.name}, icon = #{req.icon}, super_category_id = #{req.superCategoryId}, updated_at = CURRENT_TIMESTAMP, updated_by = #{getCurrentUserId}
            WHERE id = #{normalCategoryId} returning id
            """)
    UUID updateNormalCategory(@Param("req") NormalCategoryRequest normalCategoryRequest, UUID normalCategoryId, UUID getCurrentUserId);

    @Select("""
            update normal_category
            set deleted_at = CURRENT_TIMESTAMP,
            deleted_by = #{getCurrentUserId}
            where id = #{normalCategoryId}
            returning id
            """)
    UUID deleteNormalCategory(UUID normalCategoryId, UUID getCurrentUserId);

    @Select("""
            select count(*) from normal_category
            where deleted_at is null
            """)
    Integer getCountData();

    @Select("""
            select exists(select * from normal_category where name = #{name} and deleted_at is null and organization_id = #{orgId})
            """)
    Boolean isExistCategoryName(String name, UUID orgId);
}
