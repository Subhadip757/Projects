package com.example.Ecom.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String name, String productName, Double amount) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toEmail);
            mailMessage.setSubject("Payment Successful - " + productName);
            mailMessage.setText("Dear " + name + ",\n\n" +
                    "Thank you for your purchase!\n\n" +
                    "Product: " + productName + "\n" +
                    "Amount: ₹" + amount + "\n\n" +
                    "Your order has been confirmed and payment has been processed successfully.\n" +
                    "We will send you tracking information once your order is shipped.\n\n" +
                    "Thank you for shopping with us!\n\n" +
                    "Best regards,\n" +
                    "E-commerce Team"
            );

            javaMailSender.send(mailMessage);
            System.out.println("✅ Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Failed to send email notification", e);
        }
    }
}