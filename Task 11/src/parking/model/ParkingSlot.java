package parking.model;

import java.util.Objects;

public class ParkingSlot {
    private final int slotNumber;
    private Vehicle parkedVehicle;

    public ParkingSlot(int slotNumber) {
        if (slotNumber < 1) {
            throw new IllegalArgumentException("Slot number must be positive.");
        }
        this.slotNumber = slotNumber;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public boolean isAvailable() {
        return parkedVehicle == null;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public void park(Vehicle vehicle) {
        Objects.requireNonNull(vehicle, "Vehicle is required.");
        if (!isAvailable()) {
            throw new IllegalStateException("Parking slot is already occupied.");
        }
        parkedVehicle = vehicle;
    }

    public Vehicle release() {
        Vehicle released = parkedVehicle;
        parkedVehicle = null;
        return released;
    }
}
