package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.request.UserAppRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String email;

    @Async
    public void sendMail(UserAppRequest userAppRequest, String code) throws MessagingException {
        Context context = new Context();
        context.setVariable("mail", userAppRequest);
        context.setVariable("link", "http://localhost:8080/api/v1/users/register/verify?code=" + code);
        context.setVariable("code", code);

        String process = templateEngine.process("email", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setSubject("Please verify your registration");
        helper.setText(process, true);
        helper.setTo(userAppRequest.getEmail());

        mailSender.send(mimeMessage);
    }
    @Async
    public void sendSuccessMail(UserAppRequest userAppRequest) throws MessagingException{
        Context context = new Context();
        context.getVariable(userAppRequest.getEmail());

        String process = templateEngine.process("success-email", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        helper.setSubject("Register successfully");
        helper.setText(process, true);
        helper.setTo(userAppRequest.getEmail());

        mailSender.send(message);
    }

    public void sendMailForgotPassword(UUID userId, String code, String email) throws MessagingException {
        Context context = new Context();
        context.setVariable("link", "http://localhost:8080/api/v1/users/reset-password/" + userId + "?code=" +  code);
        context.setVariable("userId", userId);
        context.setVariable("code", code);
        context.setVariable("email", email);

        String process = templateEngine.process("resetPassword", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setSubject("Asset Tracer Password Reset");
        helper.setText(process, true);
        helper.setTo(email);
        mailSender.send(mimeMessage);
    }
}
