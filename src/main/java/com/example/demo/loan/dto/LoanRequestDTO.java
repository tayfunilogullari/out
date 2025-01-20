package com.example.demo.loan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRequestDTO {
    private Long customerId;
    private Double loanAmount;
    private Integer numberOfInstallment;
    private Double interestRate;
}
