package com.example.demo.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "CREDIT_LIMIT")
    private Double creditLimit;

    @Column(name = "USED_CREDIT_LIMIT")
    private Double usedCreditLimit;
}