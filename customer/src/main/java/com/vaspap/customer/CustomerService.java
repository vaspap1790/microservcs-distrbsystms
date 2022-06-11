package com.vaspap.customer;

import com.vaspap.clients.fraud.FraudCheckResponse;
import com.vaspap.clients.fraud.FraudClient;
import org.springframework.stereotype.Service;

@Service
public record CustomerService(CustomerRepository customerRepository, FraudClient fraudClient) {
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .withFirstName(request.firstName())
                .withLastName(request.lastName())
                .withEmail(request.email())
                .build();
        //TODO: check if email is valid
        //TODO: check if email is taken
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
        if(fraudCheckResponse != null && fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("Fraudster");
        }
        //TODO: send notification
    }
}
