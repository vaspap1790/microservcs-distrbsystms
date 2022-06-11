package com.vaspap.customer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository, RestTemplate restTemplate) {
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .withFirstName(request.firstName())
                .withLastName(request.lastName())
                .withEmail(request.email())
                .build();
        //TODO: check if email is valid
        //TODO: check if email is taken
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject("http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class, customer.getId());
        if(fraudCheckResponse != null && fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("Fraudster");
        }
        //TODO: send notification
    }
}
