package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.InvoiceDTO;
import com.kshrd.asset_tracer_api.model.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);
    InvoiceDTO toInvoiceDTO(Invoice invoice);
    List<InvoiceDTO> toInvoiceDTO(List<Invoice> invoices);
}
