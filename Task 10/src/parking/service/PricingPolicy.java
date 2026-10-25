package parking.service;

import java.util.Map;

public final class PricingPolicy {
    private final Map<String, Double> hourlyRates;

    public PricingPolicy() {
        hourlyRates = Map.of(
                "MOTORCYCLE", 10.0,
                "CAR", 30.0,
                "ELECTRIC_CAR", 30.0,
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
}
