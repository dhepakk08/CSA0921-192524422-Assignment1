package parking.model;

public final class ElectricCar extends Car {
    private int batteryCapacityKWh;

    public ElectricCar(String registrationNumber, String ownerName, int batteryCapacityKWh) {
        super(registrationNumber, ownerName);
        setBatteryCapacityKWh(batteryCapacityKWh);
    }

    public int getBatteryCapacityKWh() {
        return batteryCapacityKWh;
    }

    public void setBatteryCapacityKWh(int batteryCapacityKWh) {
        if (batteryCapacityKWh <= 0) {
            throw new IllegalArgumentException("Battery capacity must be positive.");
        }
        this.batteryCapacityKWh = batteryCapacityKWh;
    }

    public double chargingFee(int kWh) {
        if (kWh <= 0 || kWh > batteryCapacityKWh) {
            throw new IllegalArgumentException("Charging units must be between 1 and battery capacity.");
        }
        return kWh * 12.0;
    }

    @Override
    public String getVehicleType() {
        return "ELECTRIC_CAR";
    }
}
