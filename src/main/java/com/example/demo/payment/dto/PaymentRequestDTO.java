package com.example.demo.payment.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@Getter
@Setter
public class PaymentRequestDTO {
    private Long loanId;
    private Double amount;
    private LocalDate paymentDate;
}
