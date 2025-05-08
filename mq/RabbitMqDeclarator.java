// package com.example.demo;

import com.podzilla.mq.EventMetadata;
import com.podzilla.mq.utils.RabbitMqNaming;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RabbitMqDeclarator {

    @Autowired
    private AmqpAdmin amqpAdmin;

    private final String serviceName = RabbitMQConfig.getServiceName();


    /**
     * This method is called after the bean is created and dependencies (like amqpAdmin)
     * are injected. We perform all RabbitMQ declarations here.
     */
    @PostConstruct
    public void declareRabbitMqResources() {
        System.out.println("--- Explicitly declaring RabbitMQ resources for service: " + serviceName + " ---");

        // Access the event lists from RabbitMQConfig
        List<EventMetadata> produceEvents = RabbitMQConfig.PRODUCE_EVENTS;
        List<EventMetadata> consumeEvents = RabbitMQConfig.CONSUME_EVENTS;
        String currentServiceName = this.serviceName; // Use the injected service name

        // 1. Declare Exchanges
        Set<String> exchangeNamesToDeclare = Stream.concat(produceEvents.stream(), consumeEvents.stream())
                .map(event -> event.exchange)
                .collect(Collectors.toSet()); // Use Set for uniqueness

        exchangeNamesToDeclare.forEach(exchangeName -> {
            System.out.println("Declaring exchange: " + exchangeName);
            amqpAdmin.declareExchange(new TopicExchange(exchangeName, true, false)); // Example: durable=true, autoDelete=false
        });
        System.out.println("--- All exchanges declared ---");


        // 2. Declare Queues
        // Declare queues for consumed events (using the naming convention)
        List<Queue> queuesToDeclare = consumeEvents.stream()
                .map(event -> {
                    // Get the queue name using your naming utility and service name
                    String queueName = RabbitMqNaming.getQueueName(event.exchange, event.name, currentServiceName); // Use EventName_ServiceName format
                    System.out.println("Declaring queue: " + queueName);
                    // Create Queue object (ensure properties match what listeners expect)
                    return new Queue(queueName, true, false, false); // Example: durable=true, exclusive=false, autoDelete=false
                })
                .collect(Collectors.toList());

        queuesToDeclare.forEach(queue -> amqpAdmin.declareQueue(queue)); // declareQueue is idempotent
        System.out.println("--- All queues declared ---");


        // 3. Declare Bindings
        // Declare bindings for consumed events (linking queues to exchanges)
        consumeEvents.forEach(event -> {
            // Get the queue name using your naming utility and service name
            String queueName = RabbitMqNaming.getQueueName(event.exchange, event.name, currentServiceName);
            String exchangeName = event.exchange;
            String routingKey = event.key;

            System.out.println("Declaring binding: queue='" + queueName + "', exchange='" + exchangeName + "', key='" + routingKey + "'");

            Binding binding = BindingBuilder.bind(new Queue(queueName, true, false, false))
                    .to(new TopicExchange(exchangeName, true, false))
                    .with(routingKey);


            amqpAdmin.declareBinding(binding);
        });
        System.out.println("--- All bindings declared ---");


        System.out.println("--- RabbitMQ resource explicit declaration complete ---");
    }
}