package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.EmailDetail;
import com.project.JewelryMS.model.Order.ProductResponse;
import com.project.JewelryMS.model.OrderDetail.OrderDetailDTO;
import com.project.JewelryMS.model.OrderDetail.OrderDetailGuarantee;
import com.project.JewelryMS.model.OrderDetail.OrderDetailResponse;
import com.project.JewelryMS.repository.OrderDetailRepository;
import com.project.JewelryMS.service.Order.OrderDetailService;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EmailService {
    @Autowired
    HtmlFormatterService htmlFormatterService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private ImageService imageService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    ProductSellService productSellService;
    @Autowired
    OrderDetailService orderDetailService;
    private static final String EMAIL_PATTERN ="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String UPCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER = "0123456789";
    private static final Random RANDOM = new Random();
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    @Autowired
    MailSender mailSender;

    public String sendTempPassword(Account account){
        String emailAddressTo = account.getEmail();
        String userName = "John Doe";
        String temporaryPassword = generateTempEmail(12);
        String companyName = "JewelryManagementSystem";


        String resetPasswordEmail =
                "Dear " + account.getAccountName() + ",<br>" +
                        "We have received a request to reset the password for your account. Please find your new temporary password below:<br>" +
                        "Temporary Password: " + temporaryPassword + "<br>" +
                        "Use this temporary password to log in and set a new, secure password.<br>" +
                        "If you did not request a password reset, please disregard this email. Your account will remain secure.";

        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(emailAddressTo);
        emailDetail.setSubject("Subject: Password Reset Request");
        emailDetail.setMsgBody(resetPasswordEmail);

        sendMailTemplate(emailDetail);
        return temporaryPassword;

    }public void sendMailTemplate(EmailDetail emailDetail) {
        try {
            Context context = new Context();

            context.setVariable("name", emailDetail.getRecipient());
            context.setVariable("content", emailDetail.getMsgBody());
            String text = templateEngine.process("emailtemplate", context);

            // Creating a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            // Setting up necessary details
            mimeMessageHelper.setFrom("jewelryms44@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());

            // Send the email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public String generateTempEmail(int length){
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length/3; i++) {
            int index = RANDOM.nextInt(UPCASE.length());
            randomString.append(UPCASE.charAt(index));
        }
        for (int i = length/3; i < length*2/3; i++) {
            int index = RANDOM.nextInt(LOWERCASE.length());
            randomString.append(LOWERCASE.charAt(index));
        }
        for (int i = length*2/3; i < length; i++) {
            int index = RANDOM.nextInt(NUMBER.length());
            randomString.append(NUMBER.charAt(index));
        }
        return randomString.toString();
    }

    public void sendMailWithEmbeddedImage(EmailDetail emailDetail, BufferedImage bufferedImage, List<ProductResponse> productResponseList) {
        try {
            Context context = new Context();

            context.setVariable("name", emailDetail.getRecipient());
            context.setVariable("content", emailDetail.getMsgBody());
            context.setVariable("orderTable", htmlFormatterService.DefaultOrderTable(productResponseList));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            String cid = UUID.randomUUID().toString();
            context.setVariable("cid", cid);
            // Encode the image data as a Base64 string imageService.imgToBase64String(bufferedImage, "png")
            /*String imageString = imageService.imgToBase64String(bufferedImage, "png");

            context.setVariable("image", "data:image/png;base64," + imageString);
*/
            String text = templateEngine.process("orderEmail", context);

            // Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            // Setting up necessary details
            mimeMessageHelper.setFrom("jewelryms44@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            mimeMessageHelper.addInline(cid, new ByteArrayResource(imageBytes), "image/png");
            // Send the email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendConfirmEmail(Long orderID, EmailDetail emailDetail) {
        try {
            // Fetch order details for the given orderID
            List<OrderDetailDTO> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderID).stream()
                    .filter(orderDetail -> orderDetail.getOrderId().equals(orderID)).toList();

            // Prepare the HTML content
            Context context = new Context();
            context.setVariable("name", emailDetail.getRecipient());
            context.setVariable("content", emailDetail.getMsgBody());
            context.setVariable("orderTable", htmlFormatterService.createOrderDetailTableConfirm(orderDetails));

            // Process the email template
            String text = templateEngine.process("orderEmail", context);

            // Creating a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            // Setting up necessary details
            mimeMessageHelper.setFrom("jewelryms44@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());

            // Send the email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
    }

    private static ProductResponse getProductResponse(ProductResponse item, ProductSell product) {
        ProductResponse response = new ProductResponse();

        response.setQuantity(item.getQuantity());
        response.setProductID(product.getProductID());
        response.setName(product.getPName());
        response.setCarat(product.getCarat());
        response.setChi(product.getChi());
        response.setCost(product.getCost());
        response.setDescription(product.getPDescription());
        response.setGemstoneType(product.getGemstoneType());
        response.setImage(product.getImage());
        response.setManufacturer(product.getManufacturer());
        response.setManufactureCost(product.getManufactureCost());
        response.setStatus(product.isPStatus());
        response.setCategory_id(product.getProductID());
        return response;
    }

    public void sendRefundConfirmationEmail(Long orderDetailId, String recipientEmail, float refundAmount) {
        try {
            // Fetch order detail information
            OrderDetailDTO orderDetail = orderDetailRepository.findOrderDetailDTOById(orderDetailId);

            Context context = new Context();
            context.setVariable("name", recipientEmail);
            context.setVariable("content", "Your refund has been processed successfully.");
            context.setVariable("refundTable", htmlFormatterService.createRefundDetailTable(orderDetail, refundAmount));

            String text = templateEngine.process("refundEmail", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom("jewelryms44@gmail.com");
            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setSubject("Refund Confirmation for Order Detail #" + orderDetailId);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
    }
    public boolean validEmail(String email){
        if (email == null){
            return false;
        } else {
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }

}
