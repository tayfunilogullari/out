package com.example.demo.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {
    private Long loanId;
    private Integer countOfPaidInstallments;
    private Double totalSpendingAmount;
    private Boolean isLoanClosed;
}
