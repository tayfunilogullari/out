package com.example.demo.db;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "LOAN_INSTALLMENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="LOAN_ID", nullable=false, referencedColumnName = "id")
    private Loan loan;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "PAID_AMOUNT")
    private Double paidAmount;

    @Column(name = "DUE_DATE")
    private Timestamp dueDate;

    @Column(name = "PAYMENT_DATE")
    private Timestamp paymentDate;

    @Column(name = "IS_PAID")
    private Boolean isPaid;
}
