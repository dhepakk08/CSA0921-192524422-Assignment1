import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Vehicle {
    private final String registrationNumber;
    private final String vehicleType;

    public Vehicle(String registrationNumber, String vehicleType) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Registration number is required."
            );
        }

        if (vehicleType == null || vehicleType.isBlank()) {
            throw new IllegalArgumentException(
                "Vehicle type is required."
            );
        }

        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
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
            "Vehicle Number: " + vehicle.getRegistrationNumber()
        );
        System.out.println(
            "Vehicle Type: " + vehicle.getVehicleType()
        );
        System.out.println("Entry Time: " + entryTime.format(format));
    }
}

public class Main {
    public static void main(String[] args) {

        Vehicle vehicle = new Vehicle("TN01AB1234", "CAR");

        ParkingTicket ticket = new ParkingTicket(
            "T1001",
            vehicle,
            LocalDateTime.of(2026, 10, 25, 9, 0)
        );

        System.out.println("=== CONSTRUCTOR-BASED CREATION ===");

        ticket.display();

        System.out.println(
            "Ticket initialized with all required values."
        );
    }
}