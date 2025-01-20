package com.example.demo.loan;

import com.example.demo.loan.dto.LoanDTO;
import com.example.demo.loan.dto.LoanInstallmentDTO;
import com.example.demo.loan.dto.LoanRequestDTO;
import com.example.demo.loan.dto.LoanResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface LoanService {

    LoanDTO getById(Long id);

    List<LoanDTO> getByCustomer(Long id);

    @Transactional
    Boolean makePaid(Long id);

    @Transactional
    LoanResponseDTO createLoan(LoanRequestDTO requestDto);

    List<LoanDTO> getLoanList(LoanRequestDTO requestDto);

    List<LoanInstallmentDTO> getLoanInstallmentList(Long loanId);
}
