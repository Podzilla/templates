// package com.example.demo;

import com.podzilla.mq.EventMetadata;
import com.podzilla.mq.EventsConstants;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;


@Configuration
public class RabbitMQConfig {
    @Value("${spring.application.name}")
    private String injectedServiceName;

    static final List<EventMetadata> PRODUCE_EVENTS = Arrays.asList(
            EventsConstants.ORDER_PLACED,
            EventsConstants.USER_CREATED
    );

     static final List<EventMetadata> CONSUME_EVENTS = Arrays.asList(
            EventsConstants.USER_CREATED,
            EventsConstants.ORDER_PLACED
    );

    @PostConstruct
    public void setServiceName() {
        RabbitMQConfig.serviceName = this.injectedServiceName;

    }
    static String serviceName;
    public static String getServiceName() {
        return RabbitMQConfig.serviceName;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}