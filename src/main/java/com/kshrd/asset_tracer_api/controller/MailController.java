package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.entity.Mail;
import com.kshrd.asset_tracer_api.service.MailSenderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MailController {
//    private final MailSenderService mailService;
//
//    @PostMapping("/send_mail")
//    public String sendMail(@RequestBody Mail mail) throws MessagingException {
//        return "success";
//    }
}
