# templates

# message queue 

## How to use 
    1. copy files RabbitMQConfig.java , RabbitMqDeclarator and RabbitMqPublisher in folder mq (copy them in the same package)
    2. add the following to dependecy and see this for detailed steps https://github.com/Podzilla/mq-utils-lib/blob/main/README.md

```xml
       <dependency>
			<groupId>com.github.Podzilla</groupId>
			<artifactId>mq-utils-lib</artifactId>
			<version>main-SNAPSHOT</version>
		</dependency>

```

    3. in RabbitMQConfig use the add the events in PRODUCE_EVENTS and CONSUME_EVENTS
    4. it is a must to have spring.application.name in your application.properties
    5. to listen to an event use this 
        ```
        @RabbitListener(
            queues = "#{ T(com.podzilla.mq.utils.RabbitMqNaming).getQueueName(T(com.podzilla.mq.EventsConstants).ORDER_PLACED, T(com.example.demo.RabbitMQConfig).getServiceName()) }"
    )
        ```
  note change `com.example.demo` to the packaage name where you copied the file at 


