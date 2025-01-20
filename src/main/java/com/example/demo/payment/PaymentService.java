package com.example.demo.payment;

import com.example.demo.payment.dto.PaymentRequestDTO;
import com.example.demo.payment.dto.PaymentResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public interface PaymentService {

    @Transactional
    PaymentResponseDTO payLoan(PaymentRequestDTO requestDto);
}
