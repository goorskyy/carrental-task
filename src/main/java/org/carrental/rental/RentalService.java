package org.carrental.rental;

import org.carrental.car.Car;
import org.carrental.car.CarType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalService {

    private final List<Car> fleet = new ArrayList<>();

    public void addCar(Car car) {
        fleet.add(car);
    }

    public Optional<Reservation> reserve(String customerName, CarType carType,
                                          LocalDateTime start, int days) {
        LocalDateTime end = start.plusDays(days);

        for (Car car : fleet) {
            if (car.getType() == carType && car.tryBook(start, end)) {
                return Optional.of(Reservation.of(customerName, carType, car.getId(), start, days));
            }
        }

        return Optional.empty();
    }
}
