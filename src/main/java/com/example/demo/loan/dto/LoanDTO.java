package com.example.demo.loan.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LoanDTO {
    private Long id;
    private Long customerId;
    private Double loanAmount;
    private Integer numberOfInstallment;
    private Timestamp createDate;
    private Boolean isPaid;
}
