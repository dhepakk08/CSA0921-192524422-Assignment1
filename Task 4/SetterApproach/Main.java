import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Vehicle {
    private String registrationNumber;
    private String vehicleType;

    public Vehicle(String registrationNumber, String vehicleType) {
        setRegistrationNumber(registrationNumber);
        setVehicleType(vehicleType);
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    private void setRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Registration number is required."
            );
        }

        this.registrationNumber = registrationNumber;
    }

    private void setVehicleType(String vehicleType) {
        if (vehicleType == null || vehicleType.isBlank()) {
            throw new IllegalArgumentException(
                "Vehicle type is required."
            );
        }

        this.vehicleType = vehicleType;
    }
}

class ParkingTicket {
    private String ticketNumber;
    private Vehicle vehicle;
    private LocalDateTime entryTime;

    public ParkingTicket() {
        // Values are assigned later using setter methods.
    }

    public void setTicketNumber(String ticketNumber) {
        if (ticketNumber == null || ticketNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Ticket number is required."
            );
        }

        this.ticketNumber = ticketNumber;
    }

    public void setVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException(
                "Vehicle is required."
            );
        }

        this.vehicle = vehicle;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        if (entryTime == null) {
            throw new IllegalArgumentException(
                "Entry time is required."
            );
        }

        this.entryTime = entryTime;
    }

    public boolean isComplete() {
        return ticketNumber != null
            && vehicle != null
            && entryTime != null;
    }

    public void display() {
        if (!isComplete()) {
            System.out.println(
                "Ticket is incomplete; details cannot be displayed."
            );
            return;
        }

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
        ParkingTicket ticket = new ParkingTicket();

        System.out.println("=== SETTER-BASED CREATION ===");

        System.out.println(
            "Complete immediately after construction: " +
            ticket.isComplete()
        );

        ticket.display();

        ticket.setTicketNumber("T1001");
        ticket.setVehicle(vehicle);
        ticket.setEntryTime(
            LocalDateTime.of(2026, 10, 25, 9, 0)
        );

        System.out.println("\nAfter calling setters:");

        System.out.println(
            "Ticket complete: " + ticket.isComplete()
        );

        ticket.display();
    }
}