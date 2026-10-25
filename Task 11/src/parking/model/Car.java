package parking.model;

public class Car extends Vehicle {
    public Car(String registrationNumber, String ownerName) {
        super(registrationNumber, ownerName);
    }

    @Override
    public String getVehicleType() {
        return "CAR";
    }

    @Override
    protected double hourlyRate() {
        return 30.0;
    }
}
