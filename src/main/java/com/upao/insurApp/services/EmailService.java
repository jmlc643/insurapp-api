package com.upao.insurApp.services;

import com.upao.insurApp.dto.email.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class EmailService {

    @Autowired private JavaMailSender mailSender;
    @Autowired private SpringTemplateEngine templateEngine;

    public Mail createMail(String to, String subject, Map<String, Object> model, String from) {
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setModel(model);
        return mail;
    }

    public void sendEmail(Mail mail, String templateName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        // Configurar el contexto de Thymeleaf con los datos del modelo
        Context context = new Context();
        context.setVariables(mail.getModel());

        // Procesar la plantilla usando Thymeleaf
        String html = templateEngine.process(templateName, context);
        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());

        // Si necesitas adjuntar un archivo
        //helper.addAttachment("MyTestFile.txt", new ByteArrayResource("test".getBytes()));
        //InputStreamSource imageSource = new ClassPathResource("static/assets/logo.png");
        //helper.addInline("logo", imageSource, "image/png");

        mailSender.send(message);
    }
}