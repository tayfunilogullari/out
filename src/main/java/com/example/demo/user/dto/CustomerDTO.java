package com.example.demo.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {

    private Long Id;
    private UserDTO user;
    private String name;
    private String surname;
    private Double creditLimit;
    private Double usedCreditLimit;
}
