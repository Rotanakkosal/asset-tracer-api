package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.config.UuidTypeHandler;
import com.kshrd.asset_tracer_api.model.entity.Invoice;
import com.kshrd.asset_tracer_api.model.request.InvoiceRequest;
import com.kshrd.asset_tracer_api.model.request.InvoiceRequest2;
import com.kshrd.asset_tracer_api.model.request.ItemDetailRequest;
import com.kshrd.asset_tracer_api.repository.builder.InvoiceBuilder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface InvoiceRepository {

    @Select("""
            select * from invoice
            where deleted_at is null order by purchase_date desc
            """)
    @Results(
            id = "invoiceMapper",
            value = {
                    @Result(property = "id", column = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(property = "organizationId", column = "organization_id"),
                    @Result(property = "invoiceCode", column = "invoice_code"),
                    @Result(property = "purchaseBy", column = "purchase_by"),
                    @Result(property = "purchaseDate", column = "purchase_date"),
                    @Result(property = "createdAt", column = "created_at"),
                    @Result(property = "createdBy", column = "created_by"),
                    @Result(property = "updatedAt", column = "updated_ay"),
                    @Result(property = "updatedBy", column = "updated_by"),
                    @Result(property = "organizationId", column = "organization_id"),
                    @Result(property = "itemDetails", column = "id",
                            many = @Many(select = "com.kshrd.asset_tracer_api.repository.ItemDetailRepository.getItemDetailByInvoiceId")
                    )
            }
    )
    List<Invoice> getAllInvoices();

    @SelectProvider(value = InvoiceBuilder.class, method = "selectInvoiceSql")
    @ResultMap("invoiceMapper")
    List<Invoice> getAllInvoice(UUID orgId, Integer page, Integer size, String invoiceCode, String purchaseBy, String supplier, String sort);

    @SelectProvider(value = InvoiceBuilder.class, method = "selectCountInvoiceSql")
    String getCountAllInvoices(UUID orgId, String invoiceCode, String purchaseBy, String supplier);


    @Select("""
            select * from invoice where id = #{invoiceId} and deleted_at is null
            """)
    @ResultMap("invoiceMapper")
    Invoice getInvoiceById( UUID invoiceId);

    @Select("""
            INSERT INTO invoice(invoice_code, purchase_by, purchase_date, image, supplier, organization_id, created_by)
            VALUES(#{req.invoiceCode}, #{req.purchaseBy}, #{req.purchaseDate}, #{req.image}, #{req.supplier}, #{req.organizationId}, #{getCurrentUser})
            RETURNING id
            """)
    UUID addInvoice(@Param("req") InvoiceRequest invoiceRequest, UUID getCurrentUser);

    @Select("""
            insert into item_detail(invoice_id,name, model, qty, unit_price, discount, category_id, image, organization_id, created_by)
            values(#{invoiceId}, #{req.name}, #{req.model}, #{req.qty}, #{req.unitPrice}, #{req.discount}, #{req.normalCategoryId}, #{req.image}, #{req.organizationId}, #{getCurrentUser})
            returning id;
            """)
    UUID addItemDetail(@Param("req") ItemDetailRequest itemDetailRequest, UUID invoiceId, UUID getCurrentUser);

    @Select("""
            update invoice
            set invoice_code = #{req.invoiceCode}, purchase_by = #{req.purchaseBy},
                purchase_date = #{req.purchaseDate},supplier = #{req.supplier}, image = #{req.image},
                updated_at = CURRENT_TIMESTAMP
            where id = #{invoiceId}
            returning id;
            """)
    UUID updateInvoice(@Param("req") InvoiceRequest2 invoiceRequest2, UUID invoiceId);

    @Select("""
            update invoice
            set deleted_at = CURRENT_TIMESTAMP
            where id = #{invoiceId}
            """)
    UUID deleteInvoiceById(UUID invoiceId);

    @Select("""
            select count(*) from invoice where deleted_at is null
            """)
    Integer getCountData();


    @Select("""
            update invoice
            set invoice_code = #{req.invoiceCode}, purchase_by = #{req.purchaseBy},
                purchase_date = #{req.purchaseDate}, supplier = #{req.supplier}, image = #{req.image},
                updated_at = CURRENT_TIMESTAMP
            where id = #{invoiceId}
            returning id;
            """)
    UUID updateInvoiceWithItem(UUID invoiceId, @Param("req") InvoiceRequest invoiceRequest);

    @Delete("""
            delete from item_detail
            where invoice_id = #{invoiceId}
            """)
    void deleteInvoiceItem(UUID invoiceId);


    @Select("""
            select * from invoice
            where organization_id = #{orgId}
            and deleted_at is null
            order by created_at desc offset #{size} * (#{page}-1) limit #{size}
            """)
    @ResultMap("invoiceMapper")
    List<Invoice> getLastThreeInvoices(UUID orgId, Integer page, Integer size);

    @Select("""
            select count(*) from invoice
            where organization_id = #{orgId}
            and deleted_at is null;
            """)
    Integer getAllCountInvoiceByOrgId(UUID orgId);
}
