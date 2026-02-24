# Car Rental System

A simulated car rental system built with Java 21 and JUnit 5.

## Requirements

- Reserve a car of a given type (SEDAN, SUV, VAN) at a desired date and time for a given number of days
- Limited number of cars per type
- Overlapping reservations on the same car are prevented

## Running

```bash
mvn test
```

## Project Structure

```
src/main/java/org/carrental/
├── car/                    core domain
│   ├── CarType.java
│   ├── BookedPeriod.java
│   └── Car.java
└── rental/                 orchestration
    ├── Reservation.java
    └── RentalService.java
```

## AI Disclosure

This project was developed with the assistance of AI tooling. See [AI_USAGE.md](AI_USAGE.md) for details on the process.
