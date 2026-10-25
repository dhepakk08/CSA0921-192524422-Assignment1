import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Vehicle {
    private final String registrationNumber;
    private final String vehicleType;

    // Default constructor with constructor chaining
    public Vehicle() {
        this("UNKNOWN", "BIKE");
    }

    // Parameterized constructor
    public Vehicle(String registrationNumber, String vehicleType) {
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
    }

    // Copy constructor
    public Vehicle(Vehicle other) {
        this(other.registrationNumber, other.vehicleType);
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void display() {
        System.out.println(registrationNumber + " | " + vehicleType);
    }
}

class ParkingSlot {
    private final int floorNumber;
    private final int slotNumber;
    private Vehicle parkedVehicle;

    public ParkingSlot(int floorNumber, int slotNumber) {
        this.floorNumber = floorNumber;
        this.slotNumber = slotNumber;
        this.parkedVehicle = null;
    }

    public boolean isAvailable() {
        return parkedVehicle == null;
    }

    public boolean park(Vehicle vehicle) {
        if (vehicle == null || !isAvailable()) {
            return false;
        }

        parkedVehicle = vehicle;
        return true;
    }

    public Vehicle release() {
        Vehicle oldVehicle = parkedVehicle;
        parkedVehicle = null;
        return oldVehicle;
    }

    public void display() {
        System.out.println(
            "Floor " + floorNumber +
            ", Slot " + slotNumber +
            ", Available: " + isAvailable()
        );
    }
}

class ParkingTicket {
    private final String ticketNumber;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;

    public ParkingTicket(
            String ticketNumber,
            Vehicle vehicle,
            LocalDateTime entryTime) {

        if (ticketNumber == null || ticketNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Ticket number is required."
            );
        }

        if (vehicle == null || entryTime == null) {
            throw new IllegalArgumentException(
                "Vehicle and entry time are required."
            );
        }

        this.ticketNumber = ticketNumber;
        this.vehicle = vehicle;
        this.entryTime = entryTime;
    }

    public void display() {
        DateTimeFormatter format =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        System.out.println("Ticket Number: " + ticketNumber);
        System.out.println(
            "Vehicle: " + vehicle.getRegistrationNumber()
        );
        System.out.println("Entry Time: " + entryTime.format(format));
    }
}

class PricingPolicy {
    private final double carRatePerHour;
    private final double bikeRatePerHour;

    // Default constructor
    public PricingPolicy() {
        this(40.0, 20.0);
    }

    // Parameterized constructor
    public PricingPolicy(
            double carRatePerHour,
            double bikeRatePerHour) {

        this.carRatePerHour = carRatePerHour;
        this.bikeRatePerHour = bikeRatePerHour;
    }

    public double getRate(String vehicleType) {
        return "CAR".equalsIgnoreCase(vehicleType)
            ? carRatePerHour
            : bikeRatePerHour;
    }
}

class MonthlyPass {
    private final String passNumber;
    private final String registrationNumber;
    private final LocalDateTime validUntil;

    public MonthlyPass(
            String passNumber,
            String registrationNumber,
            LocalDateTime validUntil) {

        this.passNumber = passNumber;
        this.registrationNumber = registrationNumber;
        this.validUntil = validUntil;
    }

    public boolean isValid(LocalDateTime dateTime) {
        return passNumber != null
            && registrationNumber != null
            && validUntil != null
            && dateTime != null
            && !dateTime.isAfter(validUntil);
    }
}

class ParkingLot {
    private final String lotName;
    private final ParkingSlot[] slots;

    public ParkingLot(String lotName, int numberOfSlots) {
        if (numberOfSlots < 1) {
            throw new IllegalArgumentException(
                "At least one slot is required."
            );
        }

        this.lotName = lotName;
        this.slots = new ParkingSlot[numberOfSlots];

        for (int i = 0; i < numberOfSlots; i++) {
            slots[i] = new ParkingSlot(1, i + 1);
        }
    }

    public String getLotName() {
        return lotName;
    }

    public ParkingSlot findAvailableSlot() {
        for (ParkingSlot slot : slots) {
            if (slot.isAvailable()) {
                return slot;
            }
        }

        return null;
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== VEHICLE CONSTRUCTORS ===");

        Vehicle defaultVehicle = new Vehicle();
        Vehicle car = new Vehicle("TN01AB1234", "CAR");
        Vehicle copiedVehicle = new Vehicle(car);

        System.out.print("Default vehicle: ");
        defaultVehicle.display();

        System.out.print("Parameterized vehicle: ");
        car.display();

        System.out.print("Copied vehicle (separate object): ");
        copiedVehicle.display();

        System.out.println(
            "Same object reference: " + (car == copiedVehicle)
        );

        System.out.println("\n=== PARKING TICKET ===");

        ParkingTicket ticket = new ParkingTicket(
            "T1001",
            car,
            LocalDateTime.of(2026, 10, 25, 9, 0)
        );

        ticket.display();

        System.out.println("\n=== OTHER CLASS OBJECTS ===");

        ParkingSlot slot = new ParkingSlot(1, 1);

        System.out.println(
            "Park vehicle successfully: " + slot.park(car)
        );

        slot.display();

        PricingPolicy policy = new PricingPolicy();

        System.out.println(
            "Car rate per hour: Rs. " + policy.getRate("CAR")
        );

        MonthlyPass pass = new MonthlyPass(
            "MP101",
            "TN01AB1234",
            LocalDateTime.of(2026, 12, 31, 23, 59)
        );

        System.out.println(
            "Monthly pass valid at entry: " +
            pass.isValid(LocalDateTime.of(2026, 10, 25, 9, 0))
        );

        ParkingLot lot = new ParkingLot("North Gate", 2);

        System.out.println("Parking lot: " + lot.getLotName());

        System.out.println(
            "Available slot found: " +
            (lot.findAvailableSlot() != null)
        );
    }
}