package com.example.Ecom.Repo;

import com.example.Ecom.Model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentOrder, Long> {

    PaymentOrder findByOrderId(String orderId);
    
    PaymentOrder findByPaymentId(String paymentId);
    
    List<PaymentOrder> findByEmail(String email);
    
    List<PaymentOrder> findByStatus(String status);
}

