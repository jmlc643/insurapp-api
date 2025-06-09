package com.upao.insurApp.units;

import com.upao.insurApp.dto.email.Mail;
import com.upao.insurApp.exceptions.ConfigMailException;
import com.upao.insurApp.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock private JavaMailSender mailSender;
    @Mock private SpringTemplateEngine templateEngine;
    @InjectMocks private EmailService emailService;

    private String to;
    private String from;
    private String subject;
    private Map<String, Object> model;

    @BeforeEach
    void setUp() {
        to = "jluyoc1@upao.edu.pe";
        from = "example@example.com";
        subject = "Asunto";
        model = new HashMap<>();
        model.put("key", "value");
    }

    @Test
    void testCreateEmail() {
        // When
        Mail result = emailService.createMail(to, subject, model, from);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSubject()).isEqualTo("Asunto");
        assertThat(result.getFrom()).isEqualTo("example@example.com");
        assertThat(result.getTo()).isEqualTo("jluyoc1@upao.edu.pe");
    }

    @Test
    void testSendEmail() {
        // Given
        MimeMessage mimeMessage = mock(MimeMessage.class);

        Mail mail = emailService.createMail(to, subject, model, from);
        String templateName = "test-template";

        // When
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html><body>Test</body></html>");

        emailService.sendEmail(mail, templateName);

        // Then
        verify(mailSender).createMimeMessage();
        verify(templateEngine).process(eq(templateName), any(Context.class));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void testSendEmailWhenHaveAnException() {
        // Given
        MimeMessage mimeMessage = mock(MimeMessage.class);
        Mail mail = emailService.createMail(to, subject, model, from);
        String templateName = "test-template";

        // When
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html><body>Test</body></html>");
        doThrow(new RuntimeException(new MessagingException())).when(mailSender).send(mimeMessage);

        ConfigMailException ex = assertThrows(ConfigMailException.class, () -> emailService.sendEmail(mail, templateName));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Problemas al configurar el correo");

        verify(mailSender, times(1)).createMimeMessage();
        verify(templateEngine, times(1)).process(eq(templateName), any(Context.class));
        verify(mailSender, times(1)).send(mimeMessage);
    }
}