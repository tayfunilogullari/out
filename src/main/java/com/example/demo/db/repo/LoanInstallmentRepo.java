package com.example.demo.db.repo;

import com.example.demo.db.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LoanInstallmentRepo extends JpaRepository<LoanInstallment, Long> {
    Collection<LoanInstallment> findByLoanId(Long loanId);

    List<LoanInstallment> findByLoanIdAndIsPaidOrderByDueDateAsc(Long loanId, Boolean isPaid);


}
