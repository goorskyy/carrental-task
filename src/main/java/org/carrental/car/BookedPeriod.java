package org.carrental.car;

import java.time.LocalDateTime;

record BookedPeriod(LocalDateTime start, LocalDateTime end) {

    boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return this.start.isBefore(otherEnd) && otherStart.isBefore(this.end);
    }
}
