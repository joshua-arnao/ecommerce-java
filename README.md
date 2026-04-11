<p align="center">
    <b>Select language:</b><br>
    <a href="README.md">🇺🇸 English</a> |
    <a href="README.sp.md">🇪🇸 Español</a>
</p>

---

<h1 align="center">🏋️ Sports Center — E-commerce Backend</h1>
<p align="center">A refactored Spring Boot e-commerce backend — from a course project to production-ready architecture</p>

---

## What is this project?

Sports Center is a backend REST API for a sports products e-commerce platform. This project started as a Udemy course project and was fully refactored to apply real-world professional standards: domain-driven structure, JWT security, proper money types, atomicity, and clean code principles.

The refactoring process itself is part of the value — it demonstrates the ability to work on existing codebases, identify problems, and improve them with solid technical criteria.

---

## What was refactored and why

### 🔴 Critical fixes

**Security vulnerability — all endpoints were open**
The original `SecurityConfig` had `anyRequest().permitAll()` which left orders, carts, and all endpoints completely public. Fixed to `anyRequest().authenticated()` with explicit public routes only.

**Data inconsistency in order creation**
`createOrder` was saving to the database and then deleting the cart without `@Transactional`. If the delete failed, the database would be left with a duplicate order and a cart that still existed. Fixed with `@Transactional` to guarantee atomicity.

**Wrong money types**
`price`, `subTotal`, and `deliveryFee` were using `Long` and `Double`. Both are unacceptable for financial data — `Long` can't represent decimals, `double` has floating-point precision errors. Migrated all monetary values to `BigDecimal`.

**`@Data` on JPA entities with lazy relationships**
`@Data` generates `equals()` and `hashCode()` using all fields, including lazy-loaded relationships. This triggers unintended N+1 queries or `LazyInitializationException`. Replaced with `@Getter @Setter` and `@EqualsAndHashCode(onlyExplicitlyIncluded = true)` on the `id` field only.

### 🟠 Architecture improvements

**Layer-based → Domain-based structure**

Before:
```
controllers/
services/
repositories/
entities/
```

After:
```
product/   → entity, dto, repository, service, controller
order/     → entity, dto, repository, service, controller, mapper
cart/      → entity, dto, repository, service, controller
auth/      → controller, dto
user/      → entity, dto, repository, service
shared/    → security, exceptions, error, config
```

Each domain owns all its layers. Adding a feature means touching one package, not five.

**Real user authentication**
The original project had no `User` entity — credentials were hardcoded in memory. Built a complete user domain with registration, login, BCrypt password hashing, and JWT stateless authentication.

**Cart security — userId from token, not from body**
The original cart accepted `shoppingCartId` from the request body — any client could create or read another user's cart. Fixed to extract the `userId` from the authenticated JWT token in the backend.

**Null returns → exceptions**
Methods returned `null` when resources weren't found, forcing the controller to do `if (result == null)` checks. Replaced with domain-specific exceptions caught by `GlobalExceptionHandler` returning clean HTTP responses.

---

## Architecture

```
com.ecommerce.sportscenter
    ├── product/            → Catalog, brands, types, pagination
    ├── order/              → Order lifecycle, shipping, status
    ├── cart/               → Temporary cart in Redis
    ├── auth/               → Registration and login endpoints
    ├── user/               → User entity, JWT integration
    └── shared/
        ├── security/       → JwtService, JwtAuthFilter, SecurityConfig, PasswordConfig
        ├── exceptions/     → Domain exceptions + GlobalExceptionHandler
        ├── error/          → Standardized error response
        └── config/         → CorsConfig
```

### Why Redis for the cart?

Redis stores data in memory — reads and writes are extremely fast. The cart is temporary data: it exists while the user shops and disappears when the order is placed. Storing temporary data in MySQL (persistent) would be wasteful. Redis is the right tool for short-lived, fast-access data.

### Why `shoppingCartId = userId`?

One user = one active cart. The cart ID is the user's email extracted from the JWT token. This guarantees no user can access another user's cart, and the frontend doesn't need to track a separate cart ID.

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language — Records, modern APIs |
| Spring Boot | 4.0.x | Application framework |
| Spring Security | 7.x | Authentication and authorization |
| Spring Data JPA | — | MySQL persistence |
| Spring Data Redis | — | Cart storage |
| MySQL | 8.0 | Relational database |
| Redis | 7.0 | In-memory cart storage |
| Lombok | — | Boilerplate reduction |
| MapStruct | 1.4.x | Object mapping |
| JJWT | 0.12.3 | JWT generation and validation |
| SpringDoc OpenAPI | 2.8.6 | Swagger UI |
| Docker | — | Local environment |

---

## API Endpoints

**Authentication** — public
```
POST /api/auth/register   → Register new user
POST /api/auth/login      → Login and receive JWT token
```

**Products** — GET is public, rest requires token
```
GET  /api/products                → List products (paginated, filterable)
GET  /api/products/{id}           → Get product by ID
GET  /api/products/brands         → List all brands
GET  /api/products/types          → List all types
```

**Shopping Cart** — requires token (cart belongs to authenticated user)
```
POST   /api/shopping-carts        → Create or update cart
GET    /api/shopping-carts        → Get current user's cart
DELETE /api/shopping-carts        → Delete cart
```

**Orders** — requires token
```
POST   /api/orders                → Create order from cart (cart is deleted)
GET    /api/orders                → Get authenticated user's orders
GET    /api/orders/{id}           → Get order by ID
DELETE /api/orders/{id}           → Delete order
```

---

## Security Model

```
Public:              POST /api/auth/**  |  GET /api/products/**  |  /swagger-ui
Authenticated:       Everything else
```

Every authenticated request goes through `JwtAuthFilter` which:
1. Extracts the token from the `Authorization: Bearer <token>` header
2. Validates signature and expiration
3. Loads the user from the database
4. Sets the authentication in `SecurityContextHolder`

The `userId` is always extracted from the token — never trusted from the request body.

---

## Running Locally

### Prerequisites
- Java 21
- Docker

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/joshua-arnao/ecommerce-java.git
cd ecommerce-java
```

**2. Start MySQL and Redis with Docker**
```bash
docker-compose up -d
```

**3. Configure environment**

Copy `application.yaml.example` to `application.yaml` and fill in:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/data_ecommerce
    username: root
    password: your_password
jwt:
  secret: your-base64-secret-key
  expiration: 86400000
```

**4. Run the application**
```bash
./mvnw spring-boot:run
```

**5. Access Swagger UI**
```
http://localhost:8081/swagger-ui
```

---

## Key Technical Decisions

**`BigDecimal` for all monetary values** — `double` and `float` have precision errors unacceptable in any financial context. `Long` can't represent decimals. `BigDecimal` provides exact decimal arithmetic.

**`@EqualsAndHashCode(onlyExplicitlyIncluded = true)`** — JPA entities are equal when they have the same database ID, not when all their fields match. Using `@Data` or default `equals()` on entities with lazy relationships triggers N+1 queries silently.

**`@Transactional` on `createOrder`** — The order creation involves saving to MySQL and deleting from Redis. Without a transaction boundary, a partial failure leaves the database inconsistent. `@Transactional` guarantees all-or-nothing.

**Generic error messages on auth endpoints** — `UserNotFoundException` returns `"Invalid credentials"` instead of `"User not found"`. This prevents user enumeration attacks where an attacker could determine which emails are registered.

**`anyRequest().authenticated()` as default** — Security rules are evaluated in order and stop at the first match. Starting from the most restrictive default means any new endpoint is automatically protected without needing to remember to add it to the security config.

---

## Related

- [Sports Center Frontend](https://github.com/joshua-arnao/ecommerce-angular) — Angular client

---

## Author

**Joshua Arnao**  
Self-taught Java developer focused on backend architecture and clean code.
