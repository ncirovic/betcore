# BetCore

A production-shaped **REST API for a sports betting platform**, built with Java 21 and Spring Boot 3.

Covers the full lifecycle of a bet - from browsing sports and events, through placing a wager, to settlement and wallet payout - secured with JWT authentication.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Persistence | Spring Data JPA + MySQL |
| Security | Spring Security + JWT |
| Testing | JUnit 5 + AssertJ |
| Infrastructure | Docker |
| Build | Maven |

---

## Features

- **Sports & Events** - CRUD for sports, events, markets, and selections with odds
- **Authentication** - JWT-based registration and login; role-based access control
- **Wallet System** - player balances with deposit, withdrawal, and bet deductions
- **Bet Placement & Settlement** - place bets on selections; settle events and calculate payouts
- **Leaderboard** - ranked player standings by winnings
- **Event-Driven Architecture** - internal Spring events decouple bet placement, wallet updates, and notifications (foundation for Kafka/RabbitMQ)
- **Validation & Error Handling** - Bean Validation with a global exception handler returning RFC 7807 `ProblemDetail` responses

---

## Project Structure

```
src/main/java/com/betcore/
├── controller/       # REST endpoints
├── service/          # Business logic
├── entity/           # JPA entities
├── dto/              # Request / response objects
├── repository/       # Spring Data repositories
├── security/         # JWT filter, config
├── event/            # Domain events + async listeners
└── exception/        # GlobalExceptionHandler
```

---

## Running Locally

**Prerequisites:** Java 21, Docker

```bash
# Start MySQL
docker compose up -d

# Run the app
./mvnw spring-boot:run
```

API available at `http://localhost:8080`

```bash
# Health check
curl http://localhost:8080/api/health
```

---

## Testing

```bash
./mvnw test
```

Includes unit tests for services and integration tests with `@SpringBootTest`, covering bet placement, settlement flows, and event publishing.

---

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a player |
| `POST` | `/api/auth/login` | Get JWT token |
| `GET` | `/api/sports` | List all sports |
| `GET` | `/api/events` | List events with markets |
| `POST` | `/api/bets` | Place a bet |
| `GET` | `/api/players/{id}/wallet` | Get wallet balance |
| `GET` | `/api/leaderboard` | Top players by winnings |