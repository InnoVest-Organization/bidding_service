# Bidding Service API

## Overview

The **Bidding Service API** allows investors to place bids on inventions. It includes functionality for submitting bids, viewing bids, and managing the bidding process.

This service is built using **Spring Boot** with JPA for database interaction and Swagger for API documentation.

## Features

- **Place Bid**: Allows an investor to place a bid on an invention.
- **Get Invention Bids**: Retrieves all bids for a specific invention.
- **Swagger UI**: Auto-generated API documentation accessible through Swagger UI.

## Technologies

- Java 17
- Spring Boot 3
- Spring Data JPA
- MySQL
- Swagger/OpenAPI

## Setup

Follow these steps to set up and run the service locally.

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/bidding-service.git
cd bidding-service
```

### 2. Install Dependencies

Make sure you have **Maven** installed. Run the following command to install dependencies:

```bash
mvn clean install
```

### 3. Configure Database

Create a database in MySQL and add the necessary credentials in the **`application.properties`** file.

Example configuration:

```properties
# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/inno_vest_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080

# Swagger Configuration (Optional)
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
```

### 4. Run the Application

To start the application, run the following command:

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**.

### 5. Access Swagger UI

Once the application is running, you can access the Swagger UI to interact with the API:

[http://localhost:5004/swagger-ui.html](http://localhost:5004/swagger-ui.html)

This will show all available API endpoints, their descriptions, and allow you to try them out directly in your browser.

## Endpoints

### 1. **POST /api/bids**

Place a bid for an invention.

**Request Body:**

```json
{
    "inventionId": 1,
    "investorId": 123,
    "bidAmount": 10000,
    "equity": 5
}
```

**Response Body:**

```json
{
    "orderId": 1,
    "inventionId": 1,
    "investorId": 123,
    "bidAmount": 10000,
    "equity": 5,
    "selected": false
}
```

### 2. **GET /api/bids/invention/{inventionId}**

Retrieve all bids for a specific invention.

**Path Parameter:**
- `inventionId`: The ID of the invention to get bids for

**Response Body:**

```json
{
    "inventionId": 1,
    "bids": [
        {
            "orderId": 1,
            "investorId": 123,
            "bidAmount": 10000,
            "equity": 5,
            "selected": false
        },
        {
            "orderId": 2,
            "investorId": 456,
            "bidAmount": 15000,
            "equity": 10,
            "selected": false
        }
    ]
}
```

If no bids are found for the specified invention, an empty list is returned:

```json
{
    "inventionId": 1,
    "bids": []
}
```

### 3. **GET /swagger-ui.html**

Access the Swagger UI to view and interact with the API documentation.

## Database Schema

The service uses a MySQL database with a table called **`bidding_data`**. The following fields are included in the `bidding_data` table:

- **Order_ID**: Primary key, auto-incremented.
- **Invention_ID**: The ID of the invention being bid on.
- **Investor_ID**: The ID of the investor placing the bid.
- **Bid_Amount**: The amount being bid.
- **Equity**: The percentage equity offered for the bid.
- **Selected**: Indicates if the bid has been selected.

## Dependencies

- **Spring Boot**: Main framework for the service.
- **Spring Data JPA**: To interact with the MySQL database.
- **Swagger**: To generate API documentation.
- **MySQL**: Database for storing bid information.
- **Lombok**: For reducing boilerplate code.

## Troubleshooting

If you encounter issues with Lombok not working, ensure you have the Lombok plugin installed in your IDE and that the correct Maven dependencies are included.

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.38</version>
    <optional>true</optional>
</dependency>
```

If you're facing issues with Swagger, ensure the `springdoc-openapi` dependency is added:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
