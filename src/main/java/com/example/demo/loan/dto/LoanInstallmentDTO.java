package com.example.demo.loan.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LoanInstallmentDTO {
    private Long id;
    private Double amount;
    private Double paidAmount;
    private Timestamp dueDate;
    private Timestamp paymentDate;
    private Boolean isPaid;
}
