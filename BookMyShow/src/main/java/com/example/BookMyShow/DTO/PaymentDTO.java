package com.example.BookMyShow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long id;
    private String transactionId;
    private Double amount;
    private LocalDateTime paymentTime;
    private String paymentMethod;
    private String status;
}
