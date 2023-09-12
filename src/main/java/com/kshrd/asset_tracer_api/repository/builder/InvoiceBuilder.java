package com.kshrd.asset_tracer_api.repository.builder;

import org.apache.ibatis.jdbc.SQL;

import java.util.UUID;

public class InvoiceBuilder {
    public String selectInvoiceSql(UUID orgId, Integer page, Integer size, String invoiceCode, String purchaseBy, String supplier, String sort){
        return new SQL(){
            {
                SELECT("*");
                FROM("invoice");
                WHERE("organization_id = #{orgId}");

                if(invoiceCode != null && invoiceCode != ""){
                    WHERE("invoice_code ilike #{invoiceCode} || '%' ");
                    OR();
                    WHERE("purchase_by ilike #{invoiceCode} || '%' ");
                    OR();
                    WHERE("supplier ilike #{invoiceCode} || '%' ");
                }

                if(purchaseBy != null && purchaseBy != ""){
                    AND();
                    WHERE("purchase_by ilike #{purchaseBy} || '%' ");
                }
                if(supplier != null && supplier != ""){
                    AND();
                    WHERE("supplier ilike #{supplier} || '%' ");
                }

                if(sort != null && sort.equals("invoiceCode")){
                    ORDER_BY("invoice_code asc");
                }
                else if(sort != null && sort.equals("purchaseBy")){
                    ORDER_BY("purchase_by asc");
                }
                else if(sort != null && sort.equals("supplier")){
                    ORDER_BY("supplier asc");
                }
                else if(sort != null && sort.equals("purchaseDate")){
                    ORDER_BY("purchase_date asc");
                }
                else {
                    ORDER_BY("created_at DESC");
                }

                OFFSET("#{size} * (#{page}-1)");
                LIMIT("#{size}");
            }
        }.toString();
    }

    public String selectCountInvoiceSql(UUID orgId, String invoiceCode, String purchaseBy, String supplier){
        return new SQL(){
            {
                SELECT("count(*)");
                FROM("invoice");
                WHERE("organization_id = #{orgId}");
                if(invoiceCode != null && invoiceCode != ""){
                    AND();
                    WHERE("""
                        invoice_code ilike concat(#{invoiceCode},'%')
                        or purchase_by ilike concat(#{invoiceCode},'%')
                        or supplier ilike concat(#{invoiceCode},'%')
                        """);
                }
                if(purchaseBy != null && purchaseBy != ""){
                    AND();
                    WHERE("purchase_by ilike concat(#{purchaseBy},'%')");
                }
                if(supplier != null && supplier != ""){
                    AND();
                    WHERE("supplier ilike concat(#{supplier},'%')");
                }
            }
        }.toString();
    }
}
