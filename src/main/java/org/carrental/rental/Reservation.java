package org.carrental.rental;

import org.carrental.car.CarType;

import java.time.LocalDateTime;

public record Reservation(
        String customerName,
        CarType carType,
        String assignedCarId,
        LocalDateTime start,
        LocalDateTime end
) {

    public Reservation {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End must be after start");
        }
    }

    public static Reservation of(String customerName, CarType carType, String assignedCarId,
                                  LocalDateTime start, int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Rental days must be positive");
        }
        return new Reservation(customerName, carType, assignedCarId, start, start.plusDays(days));
    }
}
