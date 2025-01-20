package com.example.demo.user;

import com.example.demo.user.dto.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface CustomerService {

    List<CustomerDTO> getCustomerList();

    CustomerDTO getCustomerById(Long id);

    CustomerDTO getCustomerByUserId(Long id);

    @Transactional
    Boolean createCustomer(CustomerDTO customerDto);

    @Transactional
    Boolean updateCustomer(CustomerDTO customerDto);

}
