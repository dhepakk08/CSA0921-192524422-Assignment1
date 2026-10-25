package parking.service;

import parking.model.ParkingSlot;
import parking.model.Vehicle;

public class ParkingFloor {
    private final int floorNumber;
    private final ParkingSlot[] slots;

    public ParkingFloor(int floorNumber, int slotCount) {
        if (floorNumber < 1 || slotCount < 1) {
            throw new IllegalArgumentException("Floor and slot count must be positive.");
        }
        this.floorNumber = floorNumber;
        this.slots = new ParkingSlot[slotCount];
        for (int i = 0; i < slotCount; i++) {
            slots[i] = new ParkingSlot(i + 1);
        }
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public ParkingSlot findAvailableSlot() {
        for (ParkingSlot slot : slots) {
            if (slot.isAvailable()) {
                return slot;
            }
        }
        return null;
    }

    public int occupiedCount() {
        int count = 0;
        for (ParkingSlot slot : slots) {
            if (!slot.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    // Package-private: available to parking.service classes, not the app package.
    ParkingSlot slotAt(int index) {
        if (index < 0 || index >= slots.length) {
            throw new IndexOutOfBoundsException("Slot index is outside this floor.");
        }
        return slots[index];
    }

    public ParkingSlot park(Vehicle vehicle) {
        ParkingSlot slot = findAvailableSlot();
        if (slot == null) {
            throw new IllegalStateException("No slot is available on this floor.");
        }
        slot.park(vehicle);
        return slot;
    }

    public ParkingSlot findSlot(Vehicle vehicle) {
        for (int i = 0; i < slots.length; i++) {
            ParkingSlot slot = slotAt(i);
            if (!slot.isAvailable() && slot.getParkedVehicle().equals(vehicle)) {
                return slot;
            }
        }
        return null;
    }
}
