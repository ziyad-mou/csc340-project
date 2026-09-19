# Requirements – PokeVerse

**Project Name:** PokeVerse  
**Team:** Ziyad Abdelhamid – Customer; Anuraj Subedi – Provider  
**Course:** CSC 340  
**Version:** 1.0  
**Date:** 2026-09-18

---

## 1. Overview

**Vision.** PokeVerse is an online marketplace for Pokémon card collectors and sellers. It helps users find Pokémon cards with better affordability and availability while supporting buying, selling, and collecting.

**Glossary**
- **Customer / Buyer:** A user who searches for and purchases Pokémon cards.
- **Provider / Seller:** A user who creates card listings and manages inventory.
- **Inventory:** The Pokémon cards a provider currently has available for sale.
- **Card Listing:** A Pokémon card listed for sale.

**Primary Users / Roles.**
- **Customer** — Finds and buys Pokémon cards at a good value.
- **Provider** — Lists Pokémon cards and manages inventory.

**Scope (this semester).**
- User profile
- Add to cart
- Beginner tutorial
- Top chase cards
- Collection

**Out of scope (deferred).**
- Other collectibles
- Pokémon clothing
- Binders and notebooks

> This document is **requirements-level** and solution-neutral; design decisions (UI layouts, API endpoints, schemas) are documented separately.

---

## 2. Functional Requirements (User Stories)

### 2.1 Customer Stories

- **US-1 — Create an Account**  
  _Story:_ As a customer, I want to create an account so that I can have a profile in PokeVerse.  
  _Priority:_ Must  
  _Acceptance:_
  ```gherkin
  Scenario: Customer creates an account
    Given the customer does not already have an account
    When the customer submits the required account information
    Then the customer's profile is created
  ```

- **US-2 — Search for Pokémon Cards**  
  _Story:_ As a customer, I want to search for Pokémon cards so that I can find available cards.  
  _Priority:_ Must  
  _Acceptance:_
  ```gherkin
  Scenario: Customer searches for Pokémon cards
    Given Pokémon cards are available
    When the customer searches for a Pokémon card
    Then matching cards are shown
  ```

- **US-3 — Add Cards to Cart**  
  _Story:_ As a customer, I want to add Pokémon cards to my cart so that I can keep track of cards I want to purchase.  
  _Priority:_ Must  
  _Acceptance:_
  ```gherkin
  Scenario: Customer adds a card to the cart
    Given the customer is viewing an available card
    When the customer adds the card to the cart
    Then the card appears in the customer's cart
  ```

- **US-4 — View Purchase History**  
  _Story:_ As a customer, I want to view my purchase history so that I can see cards I previously purchased.  
  _Priority:_ Must  
  _Acceptance:_
  ```gherkin
  Scenario: Customer views purchase history
    Given the customer has previous purchases
    When the customer opens purchase history
    Then the previous purchases are shown
  ```

- **US-5 — View Beginner Tutorial**  
  _Story:_ As a customer, I want to view a beginner tutorial so that I can learn how to use PokeVerse.  
  _Priority:_ Could  
  _Acceptance:_
  ```gherkin
  Scenario: Customer views the beginner tutorial
    Given the customer is using PokeVerse
    When the customer opens the beginner tutorial
    Then the tutorial is shown
  ```

### 2.2 Provider Stories

<!-- To be completed by Anuraj -->

---

## 3. Non-Functional Requirements

- **Performance:** Card search results should display within 3 seconds.
- **Availability/Reliability:** Saved user information should remain available when the user returns.
- **Security/Privacy:** Users should only be able to modify information associated with their own accounts.
- **Usability:** A new user should be able to create an account, search for a card, and add it to the cart within 5 minutes.

---

## 4. Assumptions, Constraints, and Policies

- PokeVerse focuses on Pokémon cards.
- The main user roles are Customer and Provider.
- Other collectibles, clothing, binders, and notebooks are outside the current scope.

---

## 5. Milestones (course-aligned)

- **M1 Requirements** — this file + stories opened as issues.
- **M2 High-fidelity prototype** — core customer/provider flows fully interactive.
- **M3 Design** — architecture, schema, API outline.
- **M4 Backend API** — key endpoints + tests.
- **M5 Increment** — ≥2 use cases end-to-end.
- **M6 Final** — complete system & documentation.

---

## 6. Change Management

- Stories are living artifacts; changes are tracked via repository issues and linked pull requests.
- Major changes should update this SRS.