package com.vaspap.notification;

import com.vaspap.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest notificationRequest){
        notificationRepository.save(
                Notification.builder()
                        .withToCustomerId(notificationRequest.toCustomerId())
                        .withToCustomerEmail(notificationRequest.toCustomerEmail())
                        .withSender("Vaspap")
                        .withMessage(notificationRequest.message())
                        .withSentAt(LocalDateTime.now())
                        .build()
        );
    }
}
