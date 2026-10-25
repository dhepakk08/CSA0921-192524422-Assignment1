package parking.model;

public class Truck extends Vehicle {
    public Truck(String registrationNumber, String ownerName) {
        super(registrationNumber, ownerName);
    }

    @Override
    public String getVehicleType() {
        return "TRUCK";
    }

    @Override
    protected double hourlyRate() {
        return 50.0;
    }

    @Override
    public final double calculateFee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Parking duration cannot be negative.");
        }
        int cappedMinutes = Math.min(minutes, 12 * 60);
        return Math.max(1, (cappedMinutes + 59) / 60) * hourlyRate();
    }
}
