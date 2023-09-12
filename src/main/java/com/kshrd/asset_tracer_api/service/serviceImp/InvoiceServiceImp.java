package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.exception.*;
import com.kshrd.asset_tracer_api.model.dto.InvoiceDTO;
import com.kshrd.asset_tracer_api.model.entity.Invoice;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.mapper.InvoiceMapper;
import com.kshrd.asset_tracer_api.model.request.InvoiceRequest;
import com.kshrd.asset_tracer_api.model.request.InvoiceRequest2;
import com.kshrd.asset_tracer_api.model.request.ItemDetailRequest;
import com.kshrd.asset_tracer_api.repository.InvoiceRepository;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImp implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private Invoice invoice = new Invoice();
    private InvoiceDTO invoiceDTO = new InvoiceDTO();
    private final OrganizationDetailRepository organizationDetailRepository;
    private UUID getCurrentUser() {

        Object getContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(getContext.equals("anonymousUser")) {
            throw new UnauthorizedExceptionHandler("Unauthorized User");
        }

        UserApp user = (UserApp) getContext;
        return user.getId();
    }

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        List<Invoice> invoices = invoiceRepository.getAllInvoices();
        List<InvoiceDTO> invoiceDTOS;

        if (invoices.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        invoiceDTOS = invoiceMapper.INSTANCE.toInvoiceDTO(invoices);
        return invoiceDTOS;
    }


    @Override
    public InvoiceDTO getInvoiceById(UUID invoiceId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        invoice = invoiceRepository.getInvoiceById(invoiceId);
        if ( invoice == null) {
            throw new NotFoundExceptionHandler("Invoice not found");
        }
        invoiceDTO = invoiceMapper.INSTANCE.toInvoiceDTO(invoice);

        return invoiceDTO;
    }

    @Override
    public UUID addInvoice(InvoiceRequest invoiceRequest) {

        if(invoiceRequest.getInvoiceCode().isEmpty()) {
            throw new FieldBlankExceptionHandler("Field invoice code cannot be empty");
        }
        else if(invoiceRequest.getInvoiceCode().isEmpty()) {
            throw new EmptyDataExceptionHandler("Field invoice code cannot be blank");
        }

        var organizationId = invoiceRequest.getOrganizationId();

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(!roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }

        UUID invoiceId = invoiceRepository.addInvoice(invoiceRequest, getCurrentUser());

        for (ItemDetailRequest item: invoiceRequest.getItemDetailRequests()) {
            invoiceRepository.addItemDetail(item, invoiceId, getCurrentUser());
        }
        return invoiceId;
    }


    @Override
    public UUID updateInvoice(InvoiceRequest2 invoiceRequest2, UUID invoiceId) {

        var organizationId = invoiceRequest2.getOrganizationId();

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(!roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }
        else if(invoiceRepository.getInvoiceById(invoiceId) == null){
            throw new NotFoundExceptionHandler("You're updating id is not found.");
        }
        return invoiceRepository.updateInvoice(invoiceRequest2, invoiceId);
    }

    @Override
    public UUID updateInvoiceWithItem(UUID invoiceId, InvoiceRequest invoiceRequest) {
        invoice = invoiceRepository.getInvoiceById(invoiceId);

        if(invoice == null) {
            throw new NotFoundExceptionHandler("Invoice not found");
        }

        UUID id = invoiceRepository.updateInvoiceWithItem(invoiceId, invoiceRequest);

        invoiceRepository.deleteInvoiceItem(id);

        for (ItemDetailRequest item : invoiceRequest.getItemDetailRequests()) {
            invoiceRepository.addItemDetail(item, invoiceId, getCurrentUser());
        }

        return id;
    }

    @Override
    public List<InvoiceDTO> getAllInvoice(UUID orgId, Integer page, Integer size, String invoiceCode, String purchaseBy, String supplier, String sort) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        List<Invoice> invoices = invoiceRepository.getAllInvoice(orgId, page, size, invoiceCode, purchaseBy, supplier, sort);

        if(invoices.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return invoiceMapper.INSTANCE.toInvoiceDTO(invoices);
    }

    @Override
    public UUID deleteInvoiceById(UUID invoiceId, UUID organizationId) {
        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if(!roleName.equals("ADMIN")){
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }else if(invoiceRepository.getInvoiceById(invoiceId) == null){
            throw new NotFoundExceptionHandler("You're deleting id is not found");
        }
        return invoiceRepository.deleteInvoiceById(invoiceId);
    }



    @Override
    public List<InvoiceDTO> getLastThreeInvoices(UUID orgId, Integer page, Integer size) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        List<Invoice> invoices = invoiceRepository.getLastThreeInvoices(orgId, page, size);
        List<InvoiceDTO> invoiceDTOS;

        invoiceDTOS = invoiceMapper.toInvoiceDTO(invoices);
        return invoiceDTOS;
    }

    @Override
    public Integer getAllInvoiceByOrgId(UUID orgId) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        return invoiceRepository.getAllCountInvoiceByOrgId(orgId);
    }
}
