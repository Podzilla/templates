// package com.example.demo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import com.podzilla.mq.EventMetadata;


@Component
public class RabbitMqPublisher {

    private final RabbitTemplate rabbitTemplate;

    public  RabbitMqPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Validates the provided EventMetadata object.
     * Prints error messages to System.err if the metadata is invalid.
     *
     * @param event The EventMetadata to validate.
     * @return true if the EventMetadata is valid, false otherwise.
     */
    boolean isValid(EventMetadata event) {
        if (event == null) {
            // Using System.err as requested
            System.err.println("Validation failed: Outbound event EventMetadata can not be null.");
            return  false;
        }
        String eventName = event.name != null ? event.name : "Unnamed Event";

        if (event.exchange == null || event.exchange.trim().isEmpty()) {
            System.err.println("Validation failed: Outbound event '" + eventName + "' has no exchange configured.");
            return  false;
        }

        if (event.key == null || event.key.trim().isEmpty()) {
            System.err.println("Validation failed: Outbound event '" + eventName + "' has no routing key configured.");
            return  false;
        }

        return  true;
    }

    /**
     * Sends a message for a specific configured event.
     *
     * @param event The EventMetadata object containing configuration for the event.
     * @param payload The message payload (will be converted by the MessageConverter).
     */
    public void send(EventMetadata event, Object payload) {
        if(!isValid(event)) {
            return;
        }

        String exchange = event.exchange;
        String routingKey = event.key;
        String eventName = event.name;

        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, payload);

        } catch (Exception e) {
            System.err.println("Error sending message for event '" + eventName + "' to exchange '" + exchange + "' with routing key '" + routingKey + "': " + e.getMessage());
            e.printStackTrace(System.err);
            //TODO: how to handle the failure (rethrow, retry, etc.)
        }
    }
}