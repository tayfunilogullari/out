package com.example.demo.loan;

import com.example.demo.loan.dto.LoanDTO;
import com.example.demo.loan.dto.LoanInstallmentDTO;
import com.example.demo.loan.dto.LoanRequestDTO;
import com.example.demo.loan.dto.LoanResponseDTO;
import com.example.demo.payment.dto.PaymentRequestDTO;
import com.example.demo.payment.dto.PaymentResponseDTO;
import com.example.demo.payment.PaymentService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/loan")
public class LoanController {

    private final LoanService loanService;
    private final PaymentService paymentService;

    @GetMapping (value = "/hello")
    @ResponseBody
    public String healthCheck() {
        return "Hello";
    }

    @PostMapping(value = "/createLoan")
    @ResponseBody
    public LoanResponseDTO createLoan(@RequestBody LoanRequestDTO loanDto) {
        return loanService.createLoan(loanDto);
    }

    @GetMapping(value = "/list")
    @ResponseBody
    public List<LoanDTO> loanList(@RequestBody LoanRequestDTO loanDto) {
        return loanService.getLoanList(loanDto);
    }


    @GetMapping(value = "/installments/{id}")
    @ResponseBody
    public List<LoanInstallmentDTO> loanInstallmentList(@PathVariable("id") Long loanId) {
        return loanService.getLoanInstallmentList(loanId);
    }

    @PostMapping("/pay/{id}/{amount}")
    public PaymentResponseDTO payLoan(@RequestBody PaymentRequestDTO requestDto)
    {
        if (requestDto.getPaymentDate() == null) {
            requestDto.setPaymentDate(LocalDate.now());
        }
        return paymentService.payLoan(requestDto);
    }

}
