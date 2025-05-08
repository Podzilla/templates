# templates

# message queue

## How to use

1.  Copy the files `RabbitMQConfig.java`, `RabbitMqDeclarator.java`, and `RabbitMqPublisher.java` into a folder within your service. Ensure they are in the same Java package.

2.  Add the following dependency to your `pom.xml`. For more detailed steps, refer to the library's README: [https://github.com/Podzilla/mq-utils-lib/blob/main/README.md](https://github.com/Podzilla/mq-utils-lib/blob/main/README.md)

    ```xml
    <dependency>
        <groupId>com.github.Podzilla</groupId>
        <artifactId>mq-utils-lib</artifactId>
        <version>main-SNAPSHOT</version>
    </dependency>
    ```

3.  In the `RabbitMQConfig.java` file, add your event definitions to the `PRODUCE_EVENTS` and `CONSUME_EVENTS` sets.

4.  It is mandatory to have `spring.application.name` defined in your `application.properties` file.

5.  To listen to a specific event, use the `@RabbitListener` annotation as shown below:

    ```java
    @RabbitListener(
        queues = "#{ T(com.podzilla.mq.utils.RabbitMqNaming).getQueueName(T(com.podzilla.mq.EventsConstants).ORDER_PLACED, T(com.example.demo.RabbitMQConfig).getServiceName()) }"
    )
    public void listenToOrderPlaced(YourEventType eventPayload) {
        // Your message processing logic here
    }
    ```

    **Note:** Change `com.example.demo` to the actual package name where you placed the `RabbitMQConfig.java` file.
