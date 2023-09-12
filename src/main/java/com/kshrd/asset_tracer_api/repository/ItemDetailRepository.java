package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.config.UuidTypeHandler;
import com.kshrd.asset_tracer_api.model.entity.ItemDetail;
import com.kshrd.asset_tracer_api.model.request.ItemDetailRequest;
import com.kshrd.asset_tracer_api.model.request.ItemDetailUpdateRequest;
import com.kshrd.asset_tracer_api.repository.builder.ItemBuilder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ItemDetailRepository {

    @Results(
            id = "invoiceMapper",
            value = {
                    @Result(property = "id", column = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(property = "invoiceCode", column = "invoice_code"),
                    @Result(property = "normalCategoryId", column = "normal_category_id"),
                    @Result(property = "normalCategoryName", column = "normal_category_name"),
                    @Result(property = "normalCategoryIcon", column = "normal_category_icon"),
                    @Result(property = "superCategoryId", column = "super_category_id"),
                    @Result(property = "superCategoryName", column = "super_category_name"),
                    @Result(property = "superCategoryIcon", column = "super_category_icon"),
                    @Result(property = "unitPrice", column = "unit_price"),
                    @Result(property = "createdAt", column = "created_at"),
                    @Result(property = "createdBy", column = "created_by"),
                    @Result(property = "organizationId", column = "organization_id"),
            }
    )
    @Select("""
            select itd.*,
                   i.id,
                   nc.name normal_category_name,
                   nc.icon normal_category_icon,
                   sc.name super_category_name,
                   sc.icon super_category_icon
            from item_detail itd
                     inner join invoice i on i.id = itd.invoice_id
                     inner join normal_category nc on itd.category_id = nc.id
                     inner join super_category sc on nc.super_category_id = sc.id
            where invoice_id = #{invoiceId}
                      """)
    List<ItemDetail> getAllItemDetailByInvoiceId(UUID invoiceId);


    @Select("""
            select itd.*, (select count(*) from asset where itd.id = item_detail_id) usage,
                   nc.id normal_category_id,
                   nc.name normal_category_name,
                   nc.icon normal_category_icon,
                   sc.id super_category_id,
                   sc.name super_category_name,
                   sc.icon super_category_icon
            from item_detail itd
                     inner join invoice i on i.id = itd.invoice_id
                     inner join normal_category nc on itd.category_id = nc.id
                     inner join super_category sc on nc.super_category_id = sc.id
            where itd.invoice_id = #{invoiceId}
            """)
    @ResultMap("invoiceMapper")
    ItemDetail getItemDetailByInvoiceId(@Param("invoiceId") UUID invoiceId);


    @Results(
            id = "itemDetailDtoMapper",
            value = {
                    @Result(property = "id", column = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(property = "invoiceId", column = "invoice_id"),
                    @Result(property = "invoiceCode", column = "invoice_code"),
                    @Result(property = "normalCategoryName", column = "normal_category_name"),
                    @Result(property = "normalCategoryIcon", column = "normal_category_icon"),
                    @Result(property = "normalCategoryId", column = "normal_category_id"),
                    @Result(property = "superCategoryName", column = "super_category_name"),
                    @Result(property = "superCategoryId", column = "super_category_id"),
                    @Result(property = "superCategoryIcon", column = "super_category_icon"),
                    @Result(property = "unitPrice", column = "unit_price"),
                    @Result(property = "organizationId", column = "organization_id"),
                    @Result(property = "createdAt", column = "created_at"),
                    @Result(property = "createdBy", column = "created_by"),
            }
    )
    @Select("""
            select itd.*, i.invoice_code , nc.id normal_category_id,
            nc.name normal_category_name, nc.icon normal_category_icon,
            sc.name super_category_name, sc.icon super_category_icon, sc.id super_category_id
            from item_detail itd
            inner join normal_category nc on itd.category_id = nc.id
            inner join super_category sc on nc.super_category_id = sc.id
            left join invoice i on itd.invoice_id = i.id
            where itd.id = #{itemDetailId} and itd.deleted_at is null
            order by created_at desc
            """)
    ItemDetail getItemDetailById(@Param("itemDetailId") UUID itemDetailId);


    @Results(
            id = "itemMapper",
            value = {
                    @Result(property = "id", column = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(property = "unitPrice", column = "unit_price"),
                    @Result(property = "organizationId", column = "organization_id"),
                    @Result(property = "normalCategoryId", column = "category_id"),
                    @Result(property = "createdAt", column = "created_at"),
                    @Result(property = "createdBy", column = "created_by"),
            }
    )
    @Select("""
            insert into item_detail(name, model, image, qty, unit_price, discount, category_id, description, organization_id, created_by)
            values(#{req.name}, #{req.model}, #{req.image}, #{req.qty}, #{req.unitPrice}, #{req.discount}, #{req.normalCategoryId}, #{req.description}, #{req.organizationId}, #{getCurrentUser})
            returning id
            """)
    UUID addItemDetail(@Param("req") ItemDetailRequest itemDetailRequest, UUID getCurrentUser);


    @Select("""
            update item_detail
            set invoice_id = #{req.invoiceId}, name = #{req.name}, model = #{req.model}, image = #{req.image},
                qty = #{req.qty}, unit_price = #{req.unitPrice}, discount = #{req.discount}, category_id = #{req.normalCategoryId},
                updated_at = CURRENT_TIMESTAMP, updated_by = #{getCurrentUserId}
            where id = #{itemDetailId}
            returning id
            """)
    UUID updateItemDetail(@Param("req") ItemDetailUpdateRequest itemDetailUpdateRequest, UUID itemDetailId, UUID getCurrentUserId);

    @ResultMap("itemDetailDtoMapper")
    @SelectProvider(value = ItemBuilder.class, method = "selectItemSql")
    List<ItemDetail> getAllItemsDetail(UUID orgId, Integer page, Integer size, String name, String normalCategoryName, String sort);

    @SelectProvider(value = ItemBuilder.class, method = "selectCountItemSql")
    Integer  getCountAllItemsDetail(UUID orgId, String name, String normalCategoryName);

    @Select("""
            update item_detail
            set deleted_at = CURRENT_TIMESTAMP, deleted_by = #{getCurrentUserId}
            where id = #{itemId}
            """)
    UUID deleteItemById(UUID itemId, UUID getCurrentUserId);

    @Select("""
            select count(*) from item_detail
            where organization_id = #{orgId}
            and deleted_at is null
            """)
    Integer getCountData(UUID orgId);

    @Select("""
            select i.id, i.name, i.image, i.qty, i.invoice_id invoiceId, invoice.invoice_code invoiceCode,
            (select count(*) from asset where item_detail_id = i.id) countSet,
            (i.qty -(select count(*) from asset where item_detail_id = i.id)) countUnset
            from item_detail i
            left join invoice on i.invoice_id = invoice.id
            where i.deleted_at is null
            and i.qty > (select count(*) from asset where item_detail_id = i.id)
            and i.organization_id = #{orgId}
            """)

    List<ItemDetail> getAllItemDetailByOrganizationId(UUID orgId);

    @Select("""
            select i.id id ,i.*,
            nc.id normal_category_id,
            nc.name normal_category_name,
            nc.icon normal_category_icon,
            sc.id super_category_id,
            sc.name super_category_name,
            sc.icon super_category_icon,
            invoice.id invoice_id,
            invoice.invoice_code
            from item_detail i
            inner join normal_category nc on i.category_id = nc.id
            inner join super_category sc on nc.super_category_id = sc.id
            left outer join invoice on i.invoice_id = invoice.id
            where i.id = #{itemDetailId}
            and i.organization_id = #{orgId}
            """)
    @Result(property = "id", column = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class)
    @Result(property = "invoiceCode", column = "invoice_code")
    @Result(property = "normalCategoryId", column = "normal_category_id")
    @Result(property = "normalCategoryName", column = "normal_category_name")
    @Result(property = "normalCategoryIcon", column = "normal_category_icon")
    @Result(property = "superCategoryId", column = "super_category_id")
    @Result(property = "superCategoryName", column = "super_category_name")
    @Result(property = "superCategoryIcon", column = "super_category_icon")
    @Result(property = "unitPrice", column = "unit_price")
    @Result(property = "createdAt", column = "created_at")
    @Result(property = "createdBy", column = "created_by")
    @Result(property = "organizationId", column = "organization_id")
    @Result(column = "id", property = "assets",
            many = @Many(select = "com.kshrd.asset_tracer_api.repository.AssetRepository.getAllAssetByItemId")
    )
    ItemDetail getItemById(UUID itemDetailId, UUID orgId);
}
