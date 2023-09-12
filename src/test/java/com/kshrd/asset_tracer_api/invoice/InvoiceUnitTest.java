//package com.kshrd.asset_tracer_api.invoice;
//
//import com.kshrd.asset_tracer_api.model.entity.ItemDetail;
//import com.kshrd.asset_tracer_api.repository.InvoiceRepository;
//import com.kshrd.asset_tracer_api.repository.ItemDetailRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.UUID;
//
//@SpringBootTest
//@Slf4j
//public class InvoiceUnitTest {
//
//    @Autowired
//    private InvoiceRepository invoiceRepository;
//    @Autowired
//    private ItemDetailRepository itemDetailRepository;
//
//
//    @Test
//    void shouldGetAllItemDetailByInvoiceId(){
//        List<ItemDetail> res = itemDetailRepository.getAllItemDetailByInvoiceId(UUID.fromString("f3ee1b39-28e9-4096-918a-5921e0db3abf"));
//        log.info(res.toString());
//    }
//
//    @Test
//    void shouldGetAllItemDetailById(){
//        var res = itemDetailRepository.getItemDetailById(UUID.fromString("5c6b1da3-6f87-4b80-98e5-cfdce9b03756"));
//        log.info(res.toString());
////        res.setInvoiceId(null);
//        Assertions.assertNotNull(res.getInvoiceId());
//    }
//
//    @Test
//    void shouldGetInvoiceById(){
//        var res = invoiceRepository.getInvoiceById(UUID.fromString("f3ee1b39-28e9-4096-918a-5921e0db3abf"));
//        log.info(res.getItemDetails().toString());
//        Assertions.assertNotNull(res.getItemDetails().get(0));
//    }
//
//}
