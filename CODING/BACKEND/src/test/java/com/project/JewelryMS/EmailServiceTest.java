package com.project.JewelryMS;
import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.model.EmailDetail;
import com.project.JewelryMS.model.Order.ProductResponse;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.HtmlFormatterService;
import com.project.JewelryMS.service.ImageService;
import com.project.JewelryMS.service.Order.OrderDetailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private HtmlFormatterService htmlFormatterService;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private ImageService imageService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private OrderDetailService orderDetailService;

    @InjectMocks
    private EmailService emailService;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void sendMailTemplate() throws MessagingException {
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("hahoang33322@gmail.com");
        emailDetail.setSubject("Test Subject");
        emailDetail.setMsgBody("Test Body");

        Context context = new Context();
        when(templateEngine.process(eq("emailtemplate"), any(Context.class))).thenReturn("Processed Template");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendMailTemplate(emailDetail);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void generateTempEmail() {
        String tempPassword = emailService.generateTempEmail(12);

        assertNotNull(tempPassword);
        assertEquals(12, tempPassword.length());
    }

    @Test
    void sendMailWithEmbeddedImage() throws MessagingException, IOException {
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("hahoang33322@gmail.com");
        emailDetail.setSubject("Test Subject");
        emailDetail.setMsgBody("Test Body");
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        List<ProductResponse> productResponseList = Collections.emptyList();

        Context context = new Context();
        when(templateEngine.process(eq("orderEmail"), any(Context.class))).thenReturn("Processed Template");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendMailWithEmbeddedImage(emailDetail, bufferedImage, productResponseList);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendConfirmEmail() throws MessagingException {
        Long orderID = 1L;
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("hahoang33322@gmail.com");
        emailDetail.setSubject("Test Subject");
        emailDetail.setMsgBody("Test Body");

        when(orderDetailService.getOrderDetailsByOrderId(orderID)).thenReturn(Collections.emptyList());

        Context context = new Context();
        when(templateEngine.process(eq("orderEmail"), any(Context.class))).thenReturn("Processed Template");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendConfirmEmail(orderID, emailDetail);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void validEmail() {
        assertTrue(emailService.validEmail("hahoang33322@gmail.com"));
        assertFalse(emailService.validEmail("invalid-email"));
    }
}
