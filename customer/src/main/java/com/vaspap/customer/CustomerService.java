package com.vaspap.customer;

import org.springframework.stereotype.Service;

@Service
public record CustomerService(CustomerRepository customerRepository) {
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .withFirstName(request.firstName())
                .withLastName(request.lastName())
                .withEmail(request.email())
                .build();
        //TODO: check if email is valid
        //TODO: check if email is taken
        customerRepository.save(customer);
    }
}
