package parking.service;

import parking.model.ParkingSlot;
import parking.model.Vehicle;

public class ParkingLot {
    private final String lotName;
    private final ParkingFloor[] floors;

    public ParkingLot(String lotName, int floorCount, int slotsPerFloor) {
        if (lotName == null || lotName.isBlank()) {
            throw new IllegalArgumentException("Lot name is required.");
        }
        if (floorCount < 1 || slotsPerFloor < 1) {
            throw new IllegalArgumentException("Floor and slot counts must be positive.");
        }
        this.lotName = lotName.trim();
        this.floors = new ParkingFloor[floorCount];
        for (int i = 0; i < floorCount; i++) {
            floors[i] = new ParkingFloor(i + 1, slotsPerFloor);
        }
    }

    public String getLotName() {
        return lotName;
    }

    public ParkingSlot park(Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            ParkingSlot slot = floor.findAvailableSlot();
            if (slot != null) {
                slot.park(vehicle);
                return slot;
            }
        }
        throw new IllegalStateException("Parking lot is full.");
    }

    public int occupiedCount() {
        int count = 0;
        for (ParkingFloor floor : floors) {
            count += floor.occupiedCount();
        }
        return count;
    }
}
