class ParkingTicket {
    private static int nextTicketNumber = 1001;
    private final String ticketNumber;
    private final String vehicleNumber;

    ParkingTicket(String facilityCode, String vehicleNumber) {
        ticketNumber = facilityCode + "-T" + nextTicketNumber++;
        this.vehicleNumber = vehicleNumber;
    }
    String getTicketNumber() { return ticketNumber; }
    static boolean isValidVehicleNumber(String number) {
        return number != null && !number.isBlank();
    }
}

class ParkingFacility {
    private final String facilityName;
    private final String facilityCode;
    private final int capacity;
    private int occupiedSlots;

    ParkingFacility(String facilityName, String facilityCode, int capacity) {
        this.facilityName = facilityName;
        this.facilityCode = facilityCode;
        this.capacity = capacity;
    }
    ParkingTicket parkVehicle(String vehicleNumber) {
        if (!ParkingTicket.isValidVehicleNumber(vehicleNumber))
            throw new IllegalArgumentException("Vehicle number cannot be empty.");
        if (occupiedSlots >= capacity)
            throw new IllegalStateException(facilityName + " is full.");
        occupiedSlots++;
        return new ParkingTicket(facilityCode, vehicleNumber);
    }
    String getFacilityName() { return facilityName; }
    int getOccupiedSlots() { return occupiedSlots; }
}

public class Main {
    public static void main(String[] args) {
        ParkingFacility northGate = new ParkingFacility("North Gate", "N", 20);
        ParkingFacility southGate = new ParkingFacility("South Gate", "S", 20);

        System.out.println("========== STATIC TICKET COUNTER ==========");
        ParkingTicket t1 = northGate.parkVehicle("TN01AB1234");
        ParkingTicket t2 = southGate.parkVehicle("TN02CD5678");
        ParkingTicket t3 = northGate.parkVehicle("TN03EF9012");
        System.out.println("North Gate Ticket: " + t1.getTicketNumber());
        System.out.println("South Gate Ticket: " + t2.getTicketNumber());
        System.out.println("North Gate Ticket: " + t3.getTicketNumber());

        System.out.println("\\n========== OCCUPANCY ==========");
        System.out.println(northGate.getFacilityName() + " Occupied Slots: " + northGate.getOccupiedSlots());
        System.out.println(southGate.getFacilityName() + " Occupied Slots: " + southGate.getOccupiedSlots());
        System.out.println("\\nStatic counter storage: O(1) for the shared counter.");
        System.out.println("Per-facility occupancy storage: O(k) for k facilities.");
        System.out.println("Ticket-object storage: O(n) for n retained tickets.");
    }
}
