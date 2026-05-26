# Tennis Scoreboard

Web application for tracking tennis matches with real-time score calculation based on official tennis rules.

The project was created as a pet-project for practicing Java backend development using Servlets, JSP, Hibernate, and relational databases.

## Features

* Create a new tennis match
* Real-time score calculation
* Support for:

    * points
    * games
    * sets
    * deuce
    * advantage
    * tie-break
* Match winner detection
* Match history persistence
* Validation of player names
* Error handling pages
* Unit tests for match score calculation

---

## Tech Stack

### Backend

* Java 17
* Jakarta Servlet API
* JSP/JSTL
* Hibernate ORM
* Maven
* H2 Database
* Lombok

### Database

* H2 Database

### Testing

* JUnit 5

### Server

* Apache Tomcat 10

---

## Project Structure

```text
src/main/java/ru/tennis
│
├── dao              # Database access layer
├── dto              # Match state DTOs
├── exceptions       # Custom exceptions
├── gameState        # Tennis score models
├── mapper           # Entity/DTO mapping
├── model            # Hibernate entities
├── service          # Business logic
├── servlet          # HTTP request handling
├── util             # Utility classes
└── validation       # Input validation
```

---

## Application Flow

### New Match

1. User enters player names
2. Application validates input
3. Match state is created in memory
4. Unique UUID is assigned to the match

### Score Update

1. User clicks on the player who won the point
2. Match score is recalculated
3. Application checks:

    * game winner
    * set winner
    * tie-break state
    * match winner

### Match Finish

1. Finished match is saved to database
2. Match is removed from ongoing matches storage

---

## Tennis Rules Implemented

* Standard tennis scoring:

    * 0 → 15 → 30 → 40
* Deuce / Advantage
* Tie-break at 6:6
* Match victory after winning 2 sets

---

## Running the Project

### Requirements

* Java 17
* Maven
* Tomcat 11

### Clone repository

```
git clone https://github.com/Olegarh86/Tennis-scoreboard.git
```

### Build project

```
mvn clean package
```

### Deploy

Deploy generated .war file to Tomcat.

---

## Tests

The project contains unit tests for:

* point calculation
* game win logic
* set win logic
* tie-break logic
* match finish logic

Run tests:

```
mvn test
```