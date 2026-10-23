
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


## Customer Card Browsing & Search API (Issue #14)

Developer: Ziyad

### API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | /api/cards | Browse all Pokémon card listings |
| GET | /api/cards?search=Pikachu | Search cards by name |

### Features
- Retrieve card listings from Neon PostgreSQL.
- Search cards by name (case-insensitive).
- Return card details in JSON format.
- Share the CardListing entity with the Provider API.

### Java Classes
- CardListing.java — Shared database entity
- CardListingRepository.java — Database access
- CardBrowseService.java — Browsing and search logic
- CardBrowseController.java — REST API endpoints

### Testing
- GET /api/cards — 200 OK
- GET /api/cards?search=Pikachu — 200 OK
- Neon PostgreSQL persistence — Verified

### Team Integration
The CardListing entity and repository are shared
between Customer and Provider APIs.

Anuraj will use these classes for managing
seller card listings.
