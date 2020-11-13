package com.derkovich.springdocuments.service.events;

import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import com.derkovich.springdocuments.service.dto.User;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private UserDetailsServiceImpl service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        System.out.println("EVENT CREATE");
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event){
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        System.out.println("EVENT START");
        service.createVerificationToken(user, token);

        String recipientAddress = user.getUsername();
        String subject = "Registration confirmation";
        String confirmationUrl = "/api/auth/registrationConfirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Confirm email" + "\r\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}
