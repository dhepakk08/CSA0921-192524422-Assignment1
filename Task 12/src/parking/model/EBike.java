package parking.model;

public final class EBike extends Vehicle {
    private static final double FLAT_PARKING_FEE = 25.0;

    public EBike(String registrationNumber, String ownerName) {
        super(registrationNumber, ownerName);
    }

    @Override
    public String getVehicleType() {
        return "E_BIKE";
    }

    @Override
    protected double hourlyRate() {
        return FLAT_PARKING_FEE;
    }

    @Override
    public double calculateFee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Parking duration cannot be negative.");
        }
        return FLAT_PARKING_FEE;
    }
}
