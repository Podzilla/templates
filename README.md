# Project Coding Standards and Dependencies Guide

## General Coding Standards

### 1. File and Folder Structure
- Use a consistent project structure, e.g.,:
  ```
  src/
    main/
      java/com/example/project/
        config/
        controller/
        service/
        repository/
        dto/ ( Optional )
        model/
        util/
    test/
  ```
### 2. Naming Conventions
- **Classes**: Use PascalCase (e.g., `OrderService`, `CustomerRepository`).
- **Variables**: Use camelCase (e.g., `orderId`, `customerName`).
- **Constants**: Use UPPER_SNAKE_CASE (e.g., `DEFAULT_RETRY_COUNT`).
- **Methods**: Use camelCase for method names (e.g., `processOrder()`, `validateInput()`).
- **Database Columns**: Use snake_case (e.g., `user_id`, `created_at`).
- **REST Endpoints**: Use kebab-case for paths (e.g., `/api/orders/{id}/assign`).

---

## Spring Boot Standards

### 3. Configuration
- Use `application.yml` for hierarchical and readable configuration.
- Externalize sensitive data (e.g., database credentials) using environment variables or tools like **Spring Cloud Config** or **Vault**.
- Avoid hardcoding values; use `@Value` or `@ConfigurationProperties`.

### 4. Annotations
- Use `@RestController` for REST APIs.
- Annotate services with `@Service` and repositories with `@Repository`.
- Avoid placing `@Autowired` on fields; prefer constructor-based injection for better testability.
- Use `@Transactional` for methods modifying database state.

### 5. Error Handling ( Optional )
- Implement centralized exception handling with `@ControllerAdvice`.
- Use custom exception classes for domain-specific errors (e.g., `OrderNotFoundException`).
- Return meaningful HTTP status codes (e.g., `400` for validation errors, `404` for not found).

---

## Database Standards

### 6. PostgreSQL
- Normalize data structures; avoid redundancy.
- Use migrations with **Liquibase** or **Flyway** for schema management.
- Always use prepared statements to prevent SQL injection.
- Index frequently queried columns but avoid over-indexing.
- Use UUIDs for primary keys in distributed systems.

### 7. MongoDB
- Use meaningful field names (e.g., `customerId` instead of `cid`).
- Avoid deeply nested documents for high-performance queries.
- Use capped collections or TTL indexes for logs or ephemeral data.

---

## Messaging Standards (RabbitMQ)

### 8. Queue Naming
- Use descriptive names (e.g., `order_processing_queue`, `payment_notification_queue`).
- Include versioning in queue names if evolving message schemas (e.g., `order_queue_v2`).

### 9. Message Payload
- Always use a structured format like JSON or Avro for messages.
- Include metadata like `messageId` and `timestamp` for traceability.

### 10. Error Handling
- Use Dead Letter Queues (DLQs) for failed messages.
- Avoid infinite retries; configure a reasonable retry limit and backoff policy.
- Might need custom retry logic using Spring Retry.

---

## REST API Standards

### 11. Endpoints
- Use consistent HTTP methods:
  - `GET`: Fetch resources.
  - `POST`: Create resources.
  - `PUT`: Update resources.
  - `DELETE`: Remove resources.
- Example:
  ```
  GET /api/orders/{id}
  POST /api/orders
  PUT /api/orders/{id}
  DELETE /api/orders/{id}
  ```

### 12. Response Structure
- Always return a structured JSON response:
  ```json
  {
    "status": "success",
    "data": { ... },
    "error": null
  }
  ```
- Use pagination for list endpoints (e.g., `GET /orders?page=1&size=10`).

### 13. Validation
- Use `@Valid` and validation annotations (e.g., `@NotNull`, `@Email`) in DTOs.

---

## Logging Standards ( Optional )

### 14. Log Levels ( Optional )
- `DEBUG`: Detailed diagnostic information.
- `INFO`: High-level application flow (e.g., "Order processed successfully").
- `WARN`: Non-critical issues (e.g., "Stock running low").
- `ERROR`: Critical issues requiring immediate attention.

### 15. Tools ( Optional )
- Use **SLF4J** with **Logback** for consistent logging.
- Avoid logging sensitive information (e.g., passwords, PII).

### 16. Logback for Logging ( Optional )
Logback is the default logging framework in Spring Boot, offering high performance and flexibility. It integrates seamlessly with SLF4J for a consistent API. 
- **Benefits**:
  - Customizable log formats.
  - Dynamic log level adjustments.
  - Support for appending logs to files, console, or external systems (e.g., ELK).
- **Configuration**: Use a `logback-spring.xml` file to customize logging behavior.

Example logback configuration:
```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="CONSOLE" />
    </root>
</configuration>
```

---

## Testing Standards

### 17. Unit Testing
- Use **JUnit 5** and **Mockito** for unit tests.
- Write tests for services and repositories with at least 80% coverage.

### 18. Integration Testing
- Use **Spring Boot Test** for testing the application context.

### 19. API Testing
- Use tools like **Postman** or **REST-assured** for validating REST APIs.

---

## Kubernetes Standards

### 20. Config Maps and Secrets
- Store non-sensitive data in ConfigMaps and sensitive data in Secrets.

### 21. Resource Management
- Define CPU and memory limits for pods to prevent resource starvation.

### 22. Health Checks
- Implement liveness and readiness probes for all services.

---

## Redis Standards

### 23. Caching
- Use appropriate expiration policies to prevent stale data.

---

## Code Quality and Collaboration

### 24. Code Reviews
- All code changes must be peer-reviewed before merging.
- Focus on readability, maintainability, and adherence to these standards.

### 25. CI/CD
- Automate builds, tests, and deployments with tools like **GitHub Actions**, **Jenkins**, or **GitLab CI/CD**.

### 26. Documentation
- Use **Swagger/OpenAPI** for documenting APIs.
- Write clear comments for complex logic and use Javadoc for public methods.

---

## Required Spring Dependencies

- **Spring RabbitMQ**: For RabbitMQ messaging.
- **Spring Web**: For building REST APIs.
- **Spring Data MongoDB**: For MongoDB integration.
- **Spring Data JPA**: For PostgreSQL and ORM.
- **Spring Cloud Kubernetes**: For Kubernetes integration.
- **Spring Cache**: For Redis caching.
- **Lombok**: For reducing boilerplate code (e.g., getters/setters).
- **Spring WebSocket**: For real-time communication.
- **Spring Validation**: For request payload validation.
- **Spring DevTools**: For auto-reloading during development.
- **Spring Boot Test**: For testing.
- **Spring Boot Admin and Actuator**: For application health, logging, and monitoring.
- **Liquibase or Flyway**: For database schema versioning and migrations.
- **Spring Retry**: For custom RabbitMQ retries.
- **Logback**: For flexible and high-performance logging.( Optional )
- **Swagger/OpenAPI**: For API documentation.( Optional )
