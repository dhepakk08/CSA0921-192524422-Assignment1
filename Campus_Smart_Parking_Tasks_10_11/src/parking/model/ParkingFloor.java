package parking.model;
public final class ParkingFloor {
    private final int floorNumber;
    private final ParkingSlot[] slots;
    public ParkingFloor(int floorNumber, int capacity) {
        if (floorNumber <= 0 || capacity <= 0) throw new IllegalArgumentException("Invalid floor capacity");
        this.floorNumber = floorNumber;
        slots = new ParkingSlot[capacity];
        for (int i = 0; i < capacity; i++)
            slots[i] = new ParkingSlot(floorNumber, i + 1, i == capacity - 1); // one reserved EV bay
    }
    public int getFloorNumber() { return floorNumber; }
    public int getCapacity() { return slots.length; }
    public ParkingSlot getSlot(int oneBased) {
        if (oneBased < 1 || oneBased > slots.length) throw new IllegalArgumentException("Invalid slot number");
        return slots[oneBased - 1];
    }
    public ParkingSlot findFreeSlot(boolean electric) {
        // Electric vehicles prefer dedicated bays; they can use ordinary bays if none remain.
        if (electric) for (ParkingSlot slot : slots) if (slot.isEvOnly() && !slot.isOccupied()) return slot;
        for (ParkingSlot slot : slots) if (!slot.isEvOnly() && !slot.isOccupied()) return slot;
        return null;
    }
}
