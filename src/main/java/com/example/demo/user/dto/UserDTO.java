package com.example.demo.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String name;
    private String surname;
    private Double creditLimit;
    private Double usedCreditLimit;
}
