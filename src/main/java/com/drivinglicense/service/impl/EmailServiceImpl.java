package com.drivinglicense.service.impl;

import com.drivinglicense.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Override
    public void sendUserCredentials(String toEmail, String fullName, String rawPassword, String role) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Votre compte DLMS a été créé");
        message.setText(
                "Bonjour " + fullName + ",\n\n" +
                        "Un compte vous a été créé sur le système DLMS (Driving License Management System).\n\n" +
                        "Voici vos identifiants de connexion :\n" +
                        "Email : " + toEmail + "\n" +
                        "Mot de passe : " + rawPassword + "\n" +
                        "Rôle : " + role + "\n\n" +
                        "Merci de vous connecter et de changer votre mot de passe dès que possible.\n\n" +
                        "Cordialement,\nL'équipe DLMS"
        );
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send credentials email to {}: {}", toEmail, e.getMessage());
        }
    }
}