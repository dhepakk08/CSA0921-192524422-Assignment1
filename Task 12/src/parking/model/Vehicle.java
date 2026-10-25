package parking.model;

import parking.service.PricingPolicy;

import java.util.Objects;

public abstract class Vehicle {
    private String registrationNumber;
    private String ownerName;

    protected Vehicle(String registrationNumber, String ownerName) {
        setRegistrationNumber(registrationNumber);
        setOwnerName(ownerName);
    }

    public final String getRegistrationNumber() {
        return registrationNumber;
    }

    public final void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = requireText(registrationNumber, "Registration number");
    }

    public final String getOwnerName() {
        return ownerName;
    }

    public final void setOwnerName(String ownerName) {
        this.ownerName = requireText(ownerName, "Owner name");
    }

    public abstract String getVehicleType();

    protected abstract double hourlyRate();

    public double calculateFee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Parking duration cannot be negative.");
        }
        int billableHours = Math.max(1, (minutes + 59) / 60);
        return billableHours * hourlyRate();
    }

    public final double calculateFee(int minutes, PricingPolicy policy) {
        Objects.requireNonNull(policy, "Pricing policy is required.");
        if (minutes < 0) {
            throw new IllegalArgumentException("Parking duration cannot be negative.");
        }
        int billableHours = Math.max(1, (minutes + 59) / 60);
        return billableHours * policy.rateFor(getVehicleType());
    }

    protected static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    @Override
    public final boolean equals(Object other) {
        return other instanceof Vehicle vehicle
                && registrationNumber.equalsIgnoreCase(vehicle.registrationNumber);
    }

    @Override
    public final int hashCode() {
        return registrationNumber.toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return getVehicleType() + "{registrationNumber='" + registrationNumber
                + "', ownerName='" + ownerName + "'}";
    }
}
