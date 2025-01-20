package com.example.demo.db;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;


@Entity
@Table(name = "LOAN")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="CUSTOMER_ID", nullable=false, referencedColumnName = "id")
    private Customer customer;

    @Column(name = "LOAN_AMOUNT")
    private Double loanAmount;

    @Column(name = "NUMBER_OF_INSTALLMENT")
    private Integer numberOfInstallment;

    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @Column(name = "IS_PAID")
    private Boolean isPaid;
}
