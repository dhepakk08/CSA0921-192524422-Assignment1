package parking.model;
import java.time.LocalDate;
public final class ParkingTicket {
    private static long nextNumber = 1;
    private final long ticketNumber;
    private final Vehicle vehicle;
    private final int floorNumber;
    private final int slotNumber;
    private final long entryMinute;
    private final LocalDate parkingDate;
    private final MonthlyPass pass;
    public ParkingTicket(Vehicle vehicle, int floorNumber, int slotNumber, long entryMinute,
                         LocalDate parkingDate, MonthlyPass pass) {
        if (vehicle == null || parkingDate == null || floorNumber < 1 || slotNumber < 1 || entryMinute < 0)
            throw new IllegalArgumentException("Invalid ticket fields");
        this.ticketNumber = nextNumber++;
        this.vehicle = vehicle; this.floorNumber = floorNumber; this.slotNumber = slotNumber;
        this.entryMinute = entryMinute; this.parkingDate = parkingDate; this.pass = pass;
    }
    public long getTicketNumber() { return ticketNumber; }
    public Vehicle getVehicle() { return vehicle; }
    public int getFloorNumber() { return floorNumber; }
    public int getSlotNumber() { return slotNumber; }
    public long getEntryMinute() { return entryMinute; }
    public LocalDate getParkingDate() { return parkingDate; }
    public boolean hasValidPass() { return pass != null && pass.isValidFor(vehicle, parkingDate); }
}
