
# PokeVerse Backend API

## Project Description

PokeVerse is an online marketplace for buying and
selling Pokémon trading cards.

The backend uses Java 25, Spring Boot, Spring Data JPA,
and PostgreSQL hosted on Neon.

## Team Responsibilities

- Ziyad: Customer Backend API
- Anuraj: Provider Backend API

## UML Class Diagram

![PokeVerse UML](../docs/PokeVerseClassDiagram.png)

## Customer API Endpoints

| Use Case | Method | Endpoint | Success | Developer |
|---|---|---|---|---|
| Create Customer | POST | /api/customers | 201 | Ziyad |
| View Profile | GET | /api/customers/{id} | 200 | Ziyad |
| Update Profile | PUT | /api/customers/{id} | 200 | Ziyad |

## Example POST Request

Endpoint: POST /api/customers

```json
{
  "name": "Test Customer",
  "email": "test@example.com",
  "password": "TestPassword123!",
  "shippingAddress": "Greensboro, NC"
}
```

## Example PUT Request

Endpoint: PUT /api/customers/1

```json
{
  "name": "Updated Customer",
  "email": "updated@example.com",
  "shippingAddress": "Charlotte, NC"
}
```

## HTTP Status Codes

- 201: Customer created successfully
- 200: Customer retrieved or updated
- 400: Invalid request data
- 404: Customer not found
- 409: Email already registered

## Database

The backend uses Neon PostgreSQL.

Development branches:
- customer-dev
- provider-dev

Main database branch:
- production

Database credentials are configured through
environment variables and are never committed
to GitHub.

## Testing

Customer Profile API tested successfully:

- POST /api/customers
- GET /api/customers/{id}
- PUT /api/customers/{id}
- Invalid input (400)
- Missing customer (404)

Customer data persistence was verified in Neon.

## Remaining Features

Customer:
- Browse and search card listings
- Place orders and view purchase history
- Write and update reviews

Provider:
- Provider profile management
- Card listing management
- Inventory management
- Sales statistics
- Review replies



## Customer Orders & Purchase History API (Issue #15)

Developer: Ziyad

### API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/orders | Place a customer order |
| GET | /api/orders/customer/{customerId} | View purchase history |
| GET | /api/orders/{orderId} | View an individual order |

### Features

- Create customer orders containing multiple cards.
- Verify customer and card IDs.
- Validate requested quantities.
- Check available stock before completing an order.
- Calculate order totals.
- Reduce stock after a successful purchase.
- Save orders and order items in Neon PostgreSQL.
- Retrieve previous customer orders.

### Database Entities

- Order.java — Order details and total
- OrderItem.java — Purchased card details

### HTTP Status Codes

- 201 — Order placed
- 200 — Order retrieved
- 400 — Invalid order request
- 404 — Customer, card, or order not found
- 409 — Insufficient stock

### Testing

Test using curl and Neon SQL Editor.

Development database: customer-dev

Note: Authentication and real payment processing
are not implemented in this milestone.
