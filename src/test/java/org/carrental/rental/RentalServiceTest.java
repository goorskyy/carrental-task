package org.carrental.rental;

import org.carrental.car.Car;
import org.carrental.car.CarType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RentalServiceTest {

    private RentalService service;
    private static final LocalDateTime JAN_1 = LocalDateTime.of(2026, 1, 1, 10, 0);

    @BeforeEach
    void setUp() {
        service = new RentalService();
        service.addCar(new Car("SEDAN-1", CarType.SEDAN));
        service.addCar(new Car("SEDAN-2", CarType.SEDAN));
        service.addCar(new Car("SUV-1", CarType.SUV));
        service.addCar(new Car("VAN-1", CarType.VAN));
    }

    @Test
    void shouldReserveCarSuccessfully() {
        Optional<Reservation> result = service.reserve("Alice", CarType.SEDAN, JAN_1, 3);

        assertTrue(result.isPresent());
        Reservation reservation = result.get();
        assertEquals("Alice", reservation.customerName());
        assertEquals(CarType.SEDAN, reservation.carType());
        assertEquals(JAN_1, reservation.start());
        assertEquals(JAN_1.plusDays(3), reservation.end());
        assertNotNull(reservation.assignedCarId());
    }

    @Test
    void shouldRejectReservationWhenAllCarsOfTypeAreBooked() {
        service.reserve("Alice", CarType.SEDAN, JAN_1, 3);
        service.reserve("Bob", CarType.SEDAN, JAN_1, 3);

        Optional<Reservation> thirdBooking = service.reserve("Charlie", CarType.SEDAN, JAN_1, 3);

        assertTrue(thirdBooking.isEmpty());
    }

    @Test
    void shouldAllowNonOverlappingReservationsOnSameCar() {
        service.reserve("Alice", CarType.SEDAN, JAN_1, 3);
        LocalDateTime startAfterFirstEnds = JAN_1.plusDays(3);

        Optional<Reservation> secondBooking = service.reserve("Bob", CarType.SEDAN, startAfterFirstEnds, 3);

        assertTrue(secondBooking.isPresent());
        assertEquals("SEDAN-1", secondBooking.get().assignedCarId());
    }

    @Test
    void shouldRejectOverlappingReservationWhenAllCarsAreBooked() {
        service.reserve("Alice", CarType.SEDAN, JAN_1, 3);
        service.reserve("Bob", CarType.SEDAN, JAN_1, 3);
        LocalDateTime overlappingStart = JAN_1.plusDays(1);

        Optional<Reservation> thirdBooking = service.reserve("Charlie", CarType.SEDAN, overlappingStart, 3);

        assertTrue(thirdBooking.isEmpty());
    }

    @Test
    void shouldAssignOverlappingReservationToDifferentCar() {
        service.reserve("Alice", CarType.SEDAN, JAN_1, 3);
        LocalDateTime overlappingStart = JAN_1.plusDays(1);

        Optional<Reservation> secondBooking = service.reserve("Bob", CarType.SEDAN, overlappingStart, 3);

        assertTrue(secondBooking.isPresent());
        assertEquals("SEDAN-2", secondBooking.get().assignedCarId());
    }

    @Test
    void shouldNotInterfereAcrossCarTypes() {
        Optional<Reservation> sedan = service.reserve("Alice", CarType.SEDAN, JAN_1, 3);
        Optional<Reservation> suv = service.reserve("Bob", CarType.SUV, JAN_1, 3);
        Optional<Reservation> van = service.reserve("Charlie", CarType.VAN, JAN_1, 3);

        assertTrue(sedan.isPresent());
        assertTrue(suv.isPresent());
        assertTrue(van.isPresent());
    }

    @Test
    void shouldRespectFleetSizePerType() {
        // given: fleet has 2 sedans, 1 SUV, 1 van
        service.reserve("A", CarType.SEDAN, JAN_1, 3);
        service.reserve("B", CarType.SEDAN, JAN_1, 3);
        service.reserve("D", CarType.SUV, JAN_1, 3);
        service.reserve("F", CarType.VAN, JAN_1, 3);

        // when/then: next reservation for each type should be rejected
        assertTrue(service.reserve("C", CarType.SEDAN, JAN_1, 3).isEmpty(), "Only 2 sedans in fleet");
        assertTrue(service.reserve("E", CarType.SUV, JAN_1, 3).isEmpty(), "Only 1 SUV in fleet");
        assertTrue(service.reserve("G", CarType.VAN, JAN_1, 3).isEmpty(), "Only 1 van in fleet");
    }

    @Test
    void shouldComputeEndDateFromDays() {
        Reservation reservation = Reservation.of("Alice", CarType.SEDAN, "SEDAN-1", JAN_1, 3);

        assertEquals(JAN_1, reservation.start());
        assertEquals(JAN_1.plusDays(3), reservation.end());
    }

    @Test
    void shouldRejectZeroDays() {
        assertThrows(IllegalArgumentException.class,
                () -> Reservation.of("Alice", CarType.SEDAN, "SEDAN-1", JAN_1, 0));
    }
}
