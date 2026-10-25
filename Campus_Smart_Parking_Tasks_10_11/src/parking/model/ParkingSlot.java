package parking.model;
public final class ParkingSlot {
    private final int floorNumber;
    private final int slotNumber;
    private final boolean evOnly;
    private boolean occupied;
    public ParkingSlot(int floorNumber, int slotNumber, boolean evOnly) {
        if (floorNumber <= 0 || slotNumber <= 0) throw new IllegalArgumentException("Invalid floor/slot");
        this.floorNumber = floorNumber; this.slotNumber = slotNumber; this.evOnly = evOnly;
    }
    public int getFloorNumber() { return floorNumber; }
    public int getSlotNumber() { return slotNumber; }
    public boolean isEvOnly() { return evOnly; }
    public boolean isOccupied() { return occupied; }
    public void occupy() { if (occupied) throw new IllegalStateException("Occupied slot"); occupied = true; }
    public void release() { if (!occupied) throw new IllegalStateException("Free slot"); occupied = false; }
}
