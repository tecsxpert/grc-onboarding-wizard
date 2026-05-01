package com.internship.tool.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.IContext;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    // ✅ FIX: inject fromEmail manually
    @BeforeEach
    void setup() throws Exception {
        java.lang.reflect.Field field = EmailService.class.getDeclaredField("fromEmail");
        field.setAccessible(true);
        field.set(emailService, "test@gmail.com");
    }

    @Test
    void test_nullTemplateVariables() {
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any()))
                .thenReturn("<html>");

        assertDoesNotThrow(() ->
                emailService.sendEmail("test@gmail.com", "sub", "template", null)
        );
    }
    @Test
    void test_exception() {
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () ->
                emailService.sendEmail("test@gmail.com", "sub", "template", Map.of())
        );
    }
    @Test
    void test_templateProcessingFailure() {
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("fail"));

        assertThrows(RuntimeException.class, () ->
                emailService.sendEmail("test@gmail.com", "sub", "template", Map.of())
        );
    }
}