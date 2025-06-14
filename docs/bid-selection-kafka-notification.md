# Bid Selection Kafka Notification

## Overview

When a bid is selected through the `/api/bids/select` endpoint with `"selected": true`, the bidding service performs the following actions:

1. Updates the bid selection status in the database
2. Notifies the invention service about the selection
3. Retrieves the investor's email from the investor service
4. Sends a Kafka message containing bid details and investor email

## External Service Integration

### Invention Service

- **URL**: `http://localhost:5002/api/inventions/bidSelected`
- **Method**: POST
- **Request Body**:
  ```json
  {
    "Invention_ID": 123,
    "Investor_ID": 456,
    "Is_Live": false
  }
  ```

### Investor Service

- **URL**: `http://localhost:5006/api/investors/{investorId}/email`
- **Method**: GET
- **Response**:
  ```json
  {
    "email": "investor@example.com"
  }
  ```

## Kafka Integration

### Topic

- **Name**: `bid-selected`

### Message Format

```json
{
  "email": "investor@example.com",
  "orderId": 1,
  "inventionId": 123,
  "investorId": 456,
  "bidAmount": 5000,
  "equity": 25
}
```

## Error Handling

The service implements a resilient design where failures in any of the integration points (invention service, investor service, or Kafka) do not affect the primary function of updating the bid selection status. All errors are logged for troubleshooting.

## Configuration

The service can be configured through the following properties in `application.properties`:

```properties
# External Service URLs
invention.service.url=http://localhost:5002
investor.service.url=http://localhost:5006

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
```
