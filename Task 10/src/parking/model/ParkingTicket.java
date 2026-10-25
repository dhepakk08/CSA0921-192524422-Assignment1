package parking.model;

import java.time.LocalDateTime;
import java.util.Objects;

public final class ParkingTicket {
    private final String ticketNumber;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;

    public ParkingTicket(String ticketNumber, Vehicle vehicle, LocalDateTime entryTime) {
        if (ticketNumber == null || ticketNumber.isBlank()) {
            throw new IllegalArgumentException("Ticket number is required.");
        }
        this.ticketNumber = ticketNumber.trim();
        this.vehicle = Objects.requireNonNull(vehicle, "Vehicle is required.");
        this.entryTime = Objects.requireNonNull(entryTime, "Entry time is required.");
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}
