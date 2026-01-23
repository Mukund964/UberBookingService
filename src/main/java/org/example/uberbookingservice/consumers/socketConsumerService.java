package org.example.uberbookingservice.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class socketConsumerService {

    @KafkaListener(topics = "sample-topic")
    public void listen(String message) {
        System.out.println("Received Message in service : " + message);
    }

}
