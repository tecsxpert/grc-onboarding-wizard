package com.internship.tool.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // ✅ Constructor Injection (Best Practice)
    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {

        try {
            // ✅ Prepare Thymeleaf context
            Context context = new Context();
            context.setVariables(variables);

            // ✅ Process HTML template
            String htmlContent = templateEngine.process(templateName, context);

            // ✅ Create email message
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // ✅ Set sender (from application.yml)
            helper.setFrom(fromEmail);

            // ✅ Send email
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }

}
