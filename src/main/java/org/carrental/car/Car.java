package org.carrental.car;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Car {

    private final String id;
    private final CarType type;
    private final List<BookedPeriod> bookedPeriods = new ArrayList<>();

    public Car(String id, CarType type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public CarType getType() {
        return type;
    }

    public boolean isAvailableFor(LocalDateTime start, LocalDateTime end) {
        return bookedPeriods.stream()
                .noneMatch(period -> period.overlaps(start, end));
    }

    public boolean tryBook(LocalDateTime start, LocalDateTime end) {
        if (!isAvailableFor(start, end)) {
            return false;
        }
        bookedPeriods.add(new BookedPeriod(start, end));
        return true;
    }
}
