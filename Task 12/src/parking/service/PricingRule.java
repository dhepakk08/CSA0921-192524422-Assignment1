package parking.service;

import parking.model.Vehicle;

import java.time.LocalDateTime;

@FunctionalInterface
public interface PricingRule {
    double calculate(Vehicle vehicle, LocalDateTime entryTime, LocalDateTime exitTime);
}
