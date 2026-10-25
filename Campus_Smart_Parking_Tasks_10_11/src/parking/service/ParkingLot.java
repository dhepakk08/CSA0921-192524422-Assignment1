package parking.service;
import parking.model.*;
import java.time.LocalDate;
public final class ParkingLot {
    private static final int[] CAMPUS_CAPACITIES = {20, 50, 50, 50, 100, 100, 100, 100, 100};
    private final ParkingFloor[] floors;
    private final ParkingTicket[] activeTickets; // required Task 11 array
    private int activeCount;
    private long dailyCollection;
    public static int[] campusCapacities() { return CAMPUS_CAPACITIES.clone(); }
    public ParkingLot() { this(CAMPUS_CAPACITIES); }
    public ParkingLot(int[] capacities) {
        if (capacities == null || capacities.length == 0) throw new IllegalArgumentException("Floors required");
        floors = new ParkingFloor[capacities.length];
        int total = 0;
        for (int i = 0; i < capacities.length; i++) {
            if (capacities[i] < 2) throw new IllegalArgumentException("Each floor needs 2+ slots");
            total = Math.addExact(total, capacities[i]);
            floors[i] = new ParkingFloor(i + 1, capacities[i]);
        }
        activeTickets = new ParkingTicket[total];
    }
    public int getCapacity() { return activeTickets.length; }
    public int getFloorCount() { return floors.length; }
    public int getActiveCount() { return activeCount; }
    public long getDailyCollection() { return dailyCollection; }
    public boolean isOccupied(int floor, int slot) { return getSlot(floor,slot).isOccupied(); }
    private ParkingSlot getSlot(int floor, int slot) {
        if (floor < 1 || floor > floors.length) throw new IllegalArgumentException("Invalid floor number");
        return floors[floor - 1].getSlot(slot);
    }
    private int ticketIndex(String reg) {
        if (reg == null) throw new IllegalArgumentException("Registration required");
        String normalized = reg.replaceAll("\\s+", "").toUpperCase(java.util.Locale.ROOT);
        for (int i = 0; i < activeTickets.length; i++)
            if (activeTickets[i] != null && activeTickets[i].getVehicle().getRegistrationNumber().equals(normalized)) return i;
        return -1;
    }
    public ParkingTicket enter(Vehicle vehicle, long entryMinute, LocalDate day, MonthlyPass pass) {
        if (vehicle == null || day == null || entryMinute < 0) throw new IllegalArgumentException("Invalid entry");
        if (ticketIndex(vehicle.getRegistrationNumber()) >= 0) throw new IllegalArgumentException("Duplicate registration");
        boolean electric = vehicle instanceof Chargeable;
        for (ParkingFloor floor : floors) {
            ParkingSlot slot = floor.findFreeSlot(electric);
            if (slot != null) {
                ParkingTicket ticket = new ParkingTicket(vehicle, floor.getFloorNumber(), slot.getSlotNumber(), entryMinute, day, pass);
                slot.occupy();
                for (int i=0;i<activeTickets.length;i++) if (activeTickets[i]==null) {
                    activeTickets[i]=ticket; activeCount++; return ticket;
                }
                slot.release(); throw new IllegalStateException("Ticket storage full");
            }
        }
        throw new IllegalStateException("Parking full or suitable slots unavailable");
    }
    public long exit(String reg, int floor, int slot, long exitMinute, int chargedKWh) {
        ParkingSlot space = getSlot(floor,slot); // validate before any changes
        int index = ticketIndex(reg);
        if (index < 0) throw new IllegalArgumentException("Registration not parked");
        ParkingTicket ticket = activeTickets[index];
        if (ticket.getFloorNumber()!=floor || ticket.getSlotNumber()!=slot)
            throw new IllegalArgumentException("Registration-slot mismatch");
        if (exitMinute < ticket.getEntryMinute()) throw new IllegalArgumentException("Exit before entry");
        long fee = PricingPolicy.fee(ticket, exitMinute - ticket.getEntryMinute(), chargedKWh);
        long newTotal = Math.addExact(dailyCollection, fee);
        space.release(); activeTickets[index]=null; activeCount--; dailyCollection=newTotal;
        return fee;
    }
}
