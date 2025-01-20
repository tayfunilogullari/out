package com.example.demo.loan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanResponseDTO {
    private LoanRequestDTO requestDTO;
    private Boolean isAccepted;
    private Integer errorType;
    private Double amountOfOneInstallment;
}
