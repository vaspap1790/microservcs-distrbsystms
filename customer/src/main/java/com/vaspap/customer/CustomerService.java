package com.vaspap.customer;

import com.vaspap.amqp.RabbitMQMessageProducer;
import com.vaspap.clients.fraud.FraudCheckResponse;
import com.vaspap.clients.fraud.FraudClient;
import com.vaspap.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

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

        NotificationRequest notificationRequest = new NotificationRequest(customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome!", customer.getFirstName()));
        rabbitMQMessageProducer.publish(notificationRequest,
                "internal.exchange", "internal.notification.routing-key");
    }
}
