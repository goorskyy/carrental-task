package org.carrental.car;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    private static final LocalDateTime JAN_1 = LocalDateTime.of(2026, 1, 1, 10, 0);

    @Test
    void shouldBeAvailableWhenNoBookingsExist() {
        Car sedan = new Car("SEDAN-1", CarType.SEDAN);

        assertTrue(sedan.isAvailableFor(JAN_1, JAN_1.plusDays(5)));
    }

    @Test
    void shouldBeUnavailableForOverlappingPeriod() {
        Car sedan = new Car("SEDAN-1", CarType.SEDAN);
        sedan.tryBook(JAN_1, JAN_1.plusDays(3));

        boolean available = sedan.isAvailableFor(JAN_1.plusDays(1), JAN_1.plusDays(4));

        assertFalse(available);
    }

    @Test
    void shouldBeAvailableForAdjacentPeriod() {
        Car sedan = new Car("SEDAN-1", CarType.SEDAN);
        sedan.tryBook(JAN_1, JAN_1.plusDays(3));

        boolean available = sedan.isAvailableFor(JAN_1.plusDays(3), JAN_1.plusDays(6));

        assertTrue(available);
    }

    @Test
    void shouldRejectBookingForOverlappingPeriod() {
        Car sedan = new Car("SEDAN-1", CarType.SEDAN);
        sedan.tryBook(JAN_1, JAN_1.plusDays(3));

        boolean booked = sedan.tryBook(JAN_1.plusDays(1), JAN_1.plusDays(4));

        assertFalse(booked);
    }
}
