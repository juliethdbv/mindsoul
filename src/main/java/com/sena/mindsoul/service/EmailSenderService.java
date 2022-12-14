package com.sena.mindsoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmailForgotPassword(String toEmail){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("juliethdb.027@gmail.com");
        message.setTo(toEmail);
        message.setText("Solicitaste el cambio de contraseña de click en el siguiente enlace http://localhost:8080/register/restablecerClave/"+toEmail);
        message.setSubject("Cambio de clave Mind&Soul");

        mailSender.send(message);
    }
}
