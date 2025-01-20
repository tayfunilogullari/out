package com.example.demo.db.repo;

import com.example.demo.db.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Long> {
    Collection<Loan> findByCustomerId(Long customerId);
}
