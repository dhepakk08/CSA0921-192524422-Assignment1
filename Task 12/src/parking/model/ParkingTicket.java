package parking.model;

import java.time.LocalDateTime;
import java.util.Objects;

public final class ParkingTicket {
    private final String ticketNumber;
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double fee;

    public ParkingTicket(String ticketNumber, Vehicle vehicle, LocalDateTime entryTime) {
        this(ticketNumber, vehicle, null, entryTime);
    }

    public ParkingTicket(
            String ticketNumber,
            Vehicle vehicle,
            ParkingSlot slot,
            LocalDateTime entryTime) {
        if (ticketNumber == null || ticketNumber.isBlank()) {
            throw new IllegalArgumentException("Ticket number is required.");
        }
        this.ticketNumber = ticketNumber.trim();
        this.vehicle = Objects.requireNonNull(vehicle, "Vehicle is required.");
        this.slot = slot;
        this.entryTime = Objects.requireNonNull(entryTime, "Entry time is required.");
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getFee() {
        return fee;
    }

    public boolean isClosed() {
        return exitTime != null;
    }

    public void close(LocalDateTime exitTime, double fee) {
        if (isClosed()) {
            throw new IllegalStateException("Parking ticket is already closed.");
        }
        if (exitTime == null || exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time must not be before entry time.");
        }
        if (fee < 0) {
            throw new IllegalArgumentException("Fee cannot be negative.");
        }
        this.exitTime = exitTime;
        this.fee = fee;
    }
}
