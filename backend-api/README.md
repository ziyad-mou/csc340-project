# PokeVerse Backend API

**CSC 340 — Software Engineering**

**Team Members:**
- Ziyad Abdelhamid — Customer Backend API
- Anuraj Subedi — Provider Backend API

PokeVerse is an online marketplace where customers can browse, purchase, and review Pokémon trading cards. Providers can create listings, manage inventory, track sales, and respond to customer reviews.

The backend uses Java 25, Spring Boot, Spring Data JPA, and Neon PostgreSQL. It is deployed to Render using Docker.

# UML Class Diagram

![PokeVerse Class Diagram](../docs/PokeVerseClassDiagram.png)

# API Overview

**Version:** 1.0  
**Last Updated:** October 23, 2026


**API Base URLs:**

- Local: `http://localhost:8080/api`
- Production: `https://pokeverse-fk54.onrender.com/api`

These are API prefixes, not standalone endpoints.

**Example Working Endpoints:**

- Local Card Listings: http://localhost:8080/api/cards
- Production Card Listings: https://pokeverse-fk54.onrender.com/api/cards
- Production Provider Profile: https://pokeverse-fk54.onrender.com/api/providers/1
- Production Card Reviews: https://pokeverse-fk54.onrender.com/api/reviews/card/1


The API accepts and returns JSON data. Request bodies are validated using Jakarta Bean Validation.

Common HTTP status codes:

- `200 OK` — Successful request.
- `201 Created` — Resource created.
- `204 No Content` — Resource deleted.
- `400 Bad Request` — Invalid input.
- `403 Forbidden` — Review does not belong to the specified customer.
- `404 Not Found` — Resource not found.
- `409 Conflict` — Duplicate email, insufficient stock, or conflicting operation.

## Customer Profiles and Card Discovery

**Developer:** Ziyad Abdelhamid

| Method | Path | Purpose | Response Code | Error Codes | User Story |
|---|---|---|---|---|---|
| `POST` | `/api/customers` | Register a customer profile | `201 Created` | `400, 409` | [US-1](../docs/PokeVerseSRS.md) |
| `GET` | `/api/customers/{id}` | View customer profile | `200 OK` | `404` | [US-1](../docs/PokeVerseSRS.md) |
| `PUT` | `/api/customers/{id}` | Update customer profile | `200 OK` | `400, 404, 409` | [US-1](../docs/PokeVerseSRS.md) |
| `GET` | `/api/cards` | View all available Pokémon cards | `200 OK` | — | [US-2](../docs/PokeVerseSRS.md) |
| `GET` | `/api/cards?search={name}` | Search cards by name | `200 OK` | — | [US-2](../docs/PokeVerseSRS.md) |

### Example Customer Registration

```http
POST /api/customers
Content-Type: application/json

{
  "name": "Test Customer",
  "email": "customer@example.com",
  "password": "TestPassword123!",
  "shippingAddress": "Greensboro, NC"
}
```

### Example Customer Update

```http
PUT /api/customers/1
Content-Type: application/json

{
  "name": "Updated Customer",
  "email": "updated@example.com",
  "shippingAddress": "Charlotte, NC"
}
```

Passwords are hashed using BCrypt before being stored in PostgreSQL and are excluded from Customer API responses.

### Example Card Search

```http
GET /api/cards?search=Pikachu
```

This returns Pokémon card listings with names containing "Pikachu", ignoring capitalization.

Card browsing uses the shared `CardListing` entity and repository so customers can view cards created by providers.

## Customer Orders and Purchase History

**Developer:** Ziyad Abdelhamid  
**GitHub Issue:** #15

| Method | Path | Purpose | Response Code | Error Codes | User Story |
|---|---|---|---|---|---|
| `POST` | `/api/orders` | Purchase Pokémon cards | `201 Created` | `400, 404, 409` | Purchase workflow |
| `GET` | `/api/orders/customer/{customerId}` | View customer purchase history | `200 OK` | `404` | [US-4](../docs/PokeVerseSRS.md) |
| `GET` | `/api/orders/{orderId}` | View individual order details | `200 OK` | `404` | [US-4](../docs/PokeVerseSRS.md) |

### Example Order Request

```http
POST /api/orders
Content-Type: application/json

{
  "customerId": 1,
  "items": [
    {
      "cardListingId": 1,
      "quantity": 2
    }
  ]
}
```

The Orders API:

- Validates the Customer and CardListing IDs.
- Checks available inventory.
- Calculates the total purchase amount.
- Stores Order and OrderItem records in PostgreSQL.
- Reduces card stock after a successful purchase.
- Supports orders containing multiple card listings.
- Returns previous orders through the Purchase History API.

Orders use database transactions and pessimistic locking to prevent concurrent purchases from overselling inventory.

## Customer Reviews

**Developer:** Ziyad Abdelhamid  
**GitHub Issue:** #16

| Method | Path | Purpose | Response Code | Error Codes | User Story |
|---|---|---|---|---|---|
| `POST` | `/api/reviews` | Create a card review | `201 Created` | `400, 404` | Review API #16 |
| `PUT` | `/api/reviews/{reviewId}` | Update a customer review | `200 OK` | `400, 403, 404` | Review API #16 |
| `GET` | `/api/reviews/{reviewId}` | View a review | `200 OK` | `404` | Review API #16 |
| `GET` | `/api/reviews/card/{cardListingId}` | View reviews for a card | `200 OK` | `404` | Review API #16 |
| `GET` | `/api/reviews/customer/{customerId}` | View reviews written by a customer | `200 OK` | `404` | Review API #16 |

### Example Create Review Request

```http
POST /api/reviews
Content-Type: application/json

{
  "customerId": 1,
  "cardListingId": 1,
  "rating": 5,
  "comment": "Excellent Pikachu card!"
}
```

### Example Update Review Request

```http
PUT /api/reviews/1
Content-Type: application/json

{
  "customerId": 1,
  "rating": 4,
  "comment": "Great card and fast delivery!"
}
```

Review ratings must be between 1 and 5.

Each review is associated with a Customer and a CardListing. Reviews are stored in PostgreSQL and may include seller replies.

## Provider Profiles

**Developer:** Anuraj Subedi  
**GitHub Issue:** #22

| Method | Path | Purpose | Response Code | Error Codes | User Story |
|---|---|---|---|---|---|
| `POST` | `/api/providers` | Register a Provider profile | `201 Created` | `400, 409` | Provider Profile #22 |
| `GET` | `/api/providers/{providerId}` | View Provider profile | `200 OK` | `404` | Provider Profile #22 |
| `PUT` | `/api/providers/{providerId}` | Update Provider information | `200 OK` | `400, 404, 409` | Provider Profile #22 |
| `DELETE` | `/api/providers/{providerId}` | Delete a Provider without listings | `204 No Content` | `404, 409` | Provider Profile #22 |

### Example Provider Registration

```http
POST /api/providers
Content-Type: application/json

{
  "storeName": "PokeVerse Test Store",
  "email": "seller@example.com",
  "password": "TestPassword123!",
  "biography": "Selling Pokemon trading cards."
}
```

### Example Provider Update

```http
PUT /api/providers/1
Content-Type: application/json

{
  "storeName": "Updated PokeVerse Store",
  "email": "seller@example.com",
  "biography": "Specializing in rare Pokemon cards."
}
```

Provider passwords are hashed using BCrypt. Duplicate email addresses return `409 Conflict`.

Providers cannot be deleted while they still own card listings.

## Provider Card Listings and Inventory

**Developer:** Anuraj Subedi  
**GitHub Issues:** #23 and #24

| Method | Path | Purpose | Response Code | Error Codes | User Story |
|---|---|---|---|---|---|
| `POST` | `/api/providers/{providerId}/cards` | Create a Pokémon card listing | `201 Created` | `400, 404` | [US-6](../docs/PokeVerseSRS.md) |
| `GET` | `/api/providers/{providerId}/cards` | View Provider listings | `200 OK` | `404` | [US-6](../docs/PokeVerseSRS.md) |
| `GET` | `/api/providers/{providerId}/cards/{cardId}` | View a Provider listing | `200 OK` | `404` | [US-6](../docs/PokeVerseSRS.md) |
| `PUT` | `/api/providers/{providerId}/cards/{cardId}` | Update a card listing | `200 OK` | `400, 404` | [US-6](../docs/PokeVerseSRS.md) |
| `DELETE` | `/api/providers/{providerId}/cards/{cardId}` | Delete an eligible listing | `204 No Content` | `404, 409` | [US-6](../docs/PokeVerseSRS.md) |
| `PUT` | `/api/providers/{providerId}/cards/{cardId}/inventory` | Update card stock | `200 OK` | `400, 404` | [US-7](../docs/PokeVerseSRS.md) |

### Example Create Card Listing

```http
POST /api/providers/1/cards
Content-Type: application/json

{
  "name": "Pikachu V",
  "description": "Rare electric Pokemon card",
  "condition": "Near Mint",
  "price": 25.00,
  "stock": 5,
  "imageUrl": "https://example.com/pikachu.png"
}
```

### Example Update Card Listing

```http
PUT /api/providers/1/cards/1
Content-Type: application/json

{
  "name": "Pikachu V",
  "description": "Pikachu card in excellent condition",
  "condition": "Near Mint",
  "price": 27.00,
  "stock": 5,
  "imageUrl": "https://example.com/pikachu.png"
}
```

### Example Inventory Update

```http
PUT /api/providers/1/cards/1/inventory
Content-Type: application/json

{
  "stock": 10
}
```

Providers can create, retrieve, update, and delete card listings.

The CardListing entity is shared by the Provider and Customer APIs. A listing created by a Provider can be retrieved through the Customer Card Browsing API.

Deleting listings that already have orders or reviews returns `409 Conflict`.

## Provider Sales Statistics and Review Replies

**Developer:** Anuraj Subedi  
**GitHub Issues:** #25 and #26

| Method | Path | Purpose | Response Code | Error Codes | User Story |
|---|---|---|---|---|---|
| `GET` | `/api/providers/{providerId}/statistics` | View sales statistics | `200 OK` | `404` | [US-8](../docs/PokeVerseSRS.md) |
| `GET` | `/api/providers/{providerId}/reviews` | View customer reviews | `200 OK` | `404` | [US-9](../docs/PokeVerseSRS.md) |
| `POST` | `/api/providers/{providerId}/reviews/{reviewId}/replies` | Reply to a customer review | `201 Created` | `400, 404, 409` | [US-9](../docs/PokeVerseSRS.md) |
| `PUT` | `/api/providers/{providerId}/reviews/{reviewId}/replies` | Update an existing reply | `200 OK` | `400, 404` | [US-9](../docs/PokeVerseSRS.md) |

### Example Seller Reply

```http
POST /api/providers/1/reviews/1/replies
Content-Type: application/json

{
  "sellerReply": "Thank you for your purchase!"
}
```

The same JSON structure is used when updating an existing reply:

```http
PUT /api/providers/1/reviews/1/replies
Content-Type: application/json

{
  "sellerReply": "Thank you for your feedback and support!"
}
```

Providers can only reply to reviews associated with their own listings.

A Provider's sales statistics include:

- Total orders.
- Total revenue.
- Total card quantities sold.
- Number of unique customers.

Statistics are calculated from the Order and OrderItem records associated with the Provider's listings.

# Database and JPA Relationships

PokeVerse uses PostgreSQL hosted on Neon.

The primary entities are:

- Customer
- Provider
- CardListing
- Order
- OrderItem
- Review

Relationships are implemented using JPA annotations such as `@ManyToOne`, `@JoinColumn`, and `@GeneratedValue`.

The project uses DTOs for requests and selected responses to control JSON data and avoid serialization problems.

**Neon database branches:**

| Branch | Purpose |
|---|---|
| `customer-dev` | Customer API development |
| `provider-dev` | Provider API development |
| `production` | Shared database for the deployed application |

Development branches are isolated. Their data does not automatically transfer to production.

The deployed Customer and Provider APIs both use the same production database.

# Docker and Render Deployment

PokeVerse is deployed using Docker and Render.

**Render URL:** https://pokeverse-fk54.onrender.com

**Live Card API:** https://pokeverse-fk54.onrender.com/api/cards

**Docker Configuration:**

- Dockerfile location: `backend-api/Dockerfile`
- Multi-stage Docker build.
- Java 25 JDK used for compiling.
- Java 25 JRE used for running.
- Maven Wrapper builds the executable JAR.
- Spring Boot runs using `java -jar`.
- Database credentials are provided through environment variables.

**Render Configuration:**

| Setting | Value |
|---|---|
| Repository | `ziyad-mou/csc340-project` |
| Branch | `main` |
| Language | Docker |
| Root Directory | `backend-api` |
| Instance | Free |
| Database | Neon PostgreSQL |
| Database Branch | `production` |
| Environment Variable | `SPRING_DATASOURCE_URL` |

The application uses:

```properties
spring.application.name=pokeverse
spring.jpa.hibernate.ddl-auto=update
server.port=${PORT:8080}
spring.jpa.open-in-view=false
```

The database password and JDBC connection string are not stored in the GitHub repository.

**Cold Start:** Render Free services may sleep after inactivity. The first request may take approximately one minute while the service starts.

# Local Development

The application requires JDK 25 and a Neon PostgreSQL connection string.

From the project root:

```bash
cd backend-api
```

On macOS, enter the Neon JDBC connection string without displaying it:

```bash
read -rs "SPRING_DATASOURCE_URL?Paste Neon JDBC URL: "
export SPRING_DATASOURCE_URL
```

Start the backend:

```bash
./mvnw spring-boot:run
```

The local API is available at:

http://localhost:8080/api/cards

Use the `customer-dev` or `provider-dev` database branch for ordinary development testing.

# Testing and Integration

The API was developed and tested using EchoAPI, curl, and the Neon SQL Editor.

### Customer API Testing — Ziyad Abdelhamid

- Customer profile creation, retrieval, and updates.
- Validation and duplicate email handling.
- Card browsing and searching.
- Order placement, purchase history, and stock updates.
- Review creation, retrieval, and updates.
- PostgreSQL persistence.

### Provider API Implementation — Anuraj Subedi

- Provider profile management.
- Card listing creation and management.
- Inventory updates.
- Sales statistics.
- Customer review retrieval.
- Seller replies.

### Shared Production Database Test

The following integration operations were verified:

1. Created a Provider through `POST /api/providers` — `201 Created`.
2. Created Pikachu V through `POST /api/providers/1/cards` — `201 Created`.
3. Retrieved the new listing through `GET /api/cards` — `200 OK`.
4. Confirmed that both the local Spring Boot API and deployed Render API returned the shared listing.

This verifies that the Provider Card Listing API and Customer Card Browsing API share data through Neon production.

The full purchase, review, and seller-reply workflow still requires final end-to-end verification.

# Current Limitations

- The HTML/CSS frontend prototype is not yet connected to the REST API.
- Authentication and authorization are not fully implemented.
- Online payment processing is not implemented.
- A persistent backend shopping cart (US-3) is not implemented.
- Customer IDs and Provider IDs in request paths or bodies are not a substitute for authenticated identity.

The scope of this milestone is the backend REST API, database persistence, and cloud deployment.

# GitHub Repository

**Repository:**  
https://github.com/ziyad-mou/csc340-project

**Backend API Folder:**  
https://github.com/ziyad-mou/csc340-project/tree/main/backend-api

**Live Render Deployment:**  
https://pokeverse-fk54.onrender.com

---

**PokeVerse — CSC 340 Software Engineering**

**Ziyad Abdelhamid — Customer Backend API**

**Anuraj Subedi — Provider Backend API**
