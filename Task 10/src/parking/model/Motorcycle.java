package parking.model;

public class Motorcycle extends Vehicle {
    public Motorcycle(String registrationNumber, String ownerName) {
        super(registrationNumber, ownerName);
    }

    @Override
    public String getVehicleType() {
        return "MOTORCYCLE";
    }

    @Override
    protected double hourlyRate() {
        return 10.0;
    }
}
