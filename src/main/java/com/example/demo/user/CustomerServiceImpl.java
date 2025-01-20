package com.example.demo.user;

import com.example.demo.db.Customer;
import com.example.demo.db.repo.CustomerRepo;
import com.example.demo.user.dto.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements  CustomerService{

    @Autowired
    private CustomerRepo  customerRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CustomerDTO> getCustomerList() {

        List<Customer> customers = customerRepo.findAll();
        List<CustomerDTO> dtoList = customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .toList();

        return dtoList;
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepo.getReferenceById(id);
        CustomerDTO dto = modelMapper.map(customer, CustomerDTO.class);
        return dto;
    }

    @Override
    public CustomerDTO getCustomerByUserId(Long id) {
        //TODO: null check
        Customer customer = customerRepo.findByUserId(id);
        if (customer != null) {
            CustomerDTO dto = modelMapper.map(customer, CustomerDTO.class);
            return dto;
        }
        return null;
    }

    @Override
    public Boolean createCustomer(CustomerDTO customerDto) {
        //TODO: user null check
        Customer customer = modelMapper.map(customerDto, Customer.class);
        customerRepo.save(customer);

        return true;
    }

    @Override
    public Boolean updateCustomer(CustomerDTO customerDto) {
        //TODO: user null check
        Customer customer = customerRepo.getReferenceById(customerDto.getId());
        modelMapper.map(customerDto, customer);
        customerRepo.save(customer);

        return true;
    }
}
