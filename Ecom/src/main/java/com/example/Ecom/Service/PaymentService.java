package com.example.Ecom.Service;

import com.example.Ecom.Model.PaymentOrder;
import com.example.Ecom.Repo.PaymentRepo;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private EmailService emailService;

    public Order createOrder(PaymentOrder orderDetails) throws RazorpayException {
        // Validate input
        if (orderDetails.getAmount() == null || orderDetails.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        
        if (orderDetails.getName() == null || orderDetails.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        
        if (orderDetails.getEmail() == null || orderDetails.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email is required");
        }

        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", orderDetails.getAmount() * 100); // convert to paise
            orderRequest.put("currency", orderDetails.getCurrency() != null ? orderDetails.getCurrency() : "INR");
            orderRequest.put("receipt", "txn_" + UUID.randomUUID().toString().substring(0, 8));

            Order razorpayOrder = client.orders.create(orderRequest);

            // Update order details with Razorpay response
            orderDetails.setOrderId(razorpayOrder.get("id"));
            orderDetails.setStatus("CREATED");
            orderDetails.setCreatedAt(LocalDateTime.now());
            orderDetails.setUpdatedAt(LocalDateTime.now());

            // Save to database
            paymentRepo.save(orderDetails);

            return razorpayOrder;
        } catch (RazorpayException e) {
            throw new RazorpayException("Failed to create Razorpay order: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while creating order: " + e.getMessage());
        }
    }

    public void updateOrderStatus(String paymentId, String orderId, String status) {
        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment ID is required");
        }
        
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required");
        }
        
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }

        PaymentOrder order = paymentRepo.findByOrderId(orderId);
        if (order != null) {
            order.setPaymentId(paymentId);
            order.setStatus(status.toUpperCase());
            order.setUpdatedAt(LocalDateTime.now());
            
            paymentRepo.save(order);

            // Send email notification for successful payment
            if ("SUCCESS".equalsIgnoreCase(order.getStatus()) || "PAID".equalsIgnoreCase(order.getStatus())) {
                try {
                    emailService.sendEmail(order.getEmail(), order.getName(), order.getProductName(), order.getAmount());
                } catch (Exception e) {
                    // Log the error but don't fail the payment update
                    System.err.println("Failed to send email notification: " + e.getMessage());
                }
            }
        } else {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
    }
    
    public PaymentOrder getOrderByOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required");
        }
        return paymentRepo.findByOrderId(orderId);
    }
    
    public PaymentOrder getOrderByPaymentId(String paymentId) {
        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment ID is required");
        }
        return paymentRepo.findByPaymentId(paymentId);
    }
}