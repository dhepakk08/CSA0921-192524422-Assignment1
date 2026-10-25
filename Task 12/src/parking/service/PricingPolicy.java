package parking.service;

import parking.model.DeliveryVan;
import parking.model.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public final class PricingPolicy {
    private final Map<String, Double> hourlyRates;

    public PricingPolicy() {
        hourlyRates = Map.of(
                "MOTORCYCLE", 10.0,
                "CAR", 30.0,
                "ELECTRIC_CAR", 30.0,
                "E_BIKE", 25.0,
                "DELIVERY_VAN", 30.0,
                "TRUCK", 50.0);
    }

    public double rateFor(String vehicleType) {
        if (vehicleType == null || vehicleType.isBlank()) {
            throw new IllegalArgumentException("Vehicle type is required.");
        }
        Double rate = hourlyRates.get(vehicleType.trim().toUpperCase());
        if (rate == null) {
            throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
        return rate;
    }

    public double calculateFee(
            Vehicle vehicle,
            LocalDateTime entryTime,
            LocalDateTime exitTime) {
        if (vehicle == null || entryTime == null || exitTime == null) {
            throw new IllegalArgumentException("Vehicle and times are required.");
        }
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time must not be before entry time.");
        }
        if (!(vehicle instanceof DeliveryVan)) {
            return vehicle.calculateFee((int) Duration.between(entryTime, exitTime).toMinutes());
        }

        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        int billableHours = Math.max(1, (int) ((minutes + 59) / 60));
        double total = 0.0;
        for (int hour = 0; hour < billableHours; hour++) {
            LocalDateTime hourStart = entryTime.plusHours(hour);
            double rate = hourStart.toLocalTime().isBefore(java.time.LocalTime.of(17, 0))
                    && !hourStart.toLocalTime().isBefore(java.time.LocalTime.of(9, 0))
                    ? rateFor("DELIVERY_VAN") * 2
                    : rateFor("DELIVERY_VAN");
            total += rate;
        }
        return total;
    }

    public boolean isPeakHour(LocalDateTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Time is required.");
        }
        java.time.LocalTime localTime = time.toLocalTime();
        return !localTime.isBefore(java.time.LocalTime.of(9, 0))
                && localTime.isBefore(java.time.LocalTime.of(17, 0));
    }
}
