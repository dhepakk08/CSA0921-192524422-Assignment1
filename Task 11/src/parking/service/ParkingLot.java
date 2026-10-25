package parking.service;

import parking.model.ParkingSlot;
import parking.model.ParkingTicket;
import parking.model.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ParkingLot {
    private final String lotName;
    private final ParkingFloor[] floors;
    private final Map<String, ParkingTicket> activeTickets = new HashMap<>();
    private double dailyCollection;
    private int nextTicketNumber = 1001;

    public ParkingLot(String lotName, int floorCount, int slotsPerFloor) {
        if (lotName == null || lotName.isBlank()) {
            throw new IllegalArgumentException("Lot name is required.");
        }
        if (floorCount < 1 || slotsPerFloor < 1) {
            throw new IllegalArgumentException("Floor and slot counts must be positive.");
        }
        this.lotName = lotName.trim();
        this.floors = new ParkingFloor[floorCount];
        for (int i = 0; i < floorCount; i++) {
            floors[i] = new ParkingFloor(i + 1, slotsPerFloor);
        }
    }

    public String getLotName() {
        return lotName;
    }

    public ParkingSlot park(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle is required.");
        }
        for (ParkingFloor floor : floors) {
            ParkingSlot slot = floor.findAvailableSlot();
            if (slot != null) {
                slot.park(vehicle);
                return slot;
            }
        }
        throw new IllegalStateException("Parking lot is full.");
    }

    public ParkingTicket enterVehicle(Vehicle vehicle, LocalDateTime entryTime) {
        if (vehicle == null || entryTime == null) {
            throw new IllegalArgumentException("Vehicle and entry time are required.");
        }
        String registration = vehicle.getRegistrationNumber();
        if (activeTickets.containsKey(registration.toUpperCase())) {
            throw new IllegalStateException("Vehicle is already parked: " + registration);
        }
        ParkingSlot slot = park(vehicle);
        ParkingTicket ticket = new ParkingTicket(
                "T" + nextTicketNumber++, vehicle, slot, entryTime);
        activeTickets.put(registration.toUpperCase(), ticket);
        return ticket;
    }

    public double exitVehicle(String registrationNumber, LocalDateTime exitTime) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new IllegalArgumentException("Registration number is required.");
        }
        String key = registrationNumber.trim().toUpperCase();
        ParkingTicket ticket = activeTickets.remove(key);
        if (ticket == null) {
            throw new IllegalArgumentException("No active ticket found for: " + registrationNumber);
        }
        if (exitTime == null || exitTime.isBefore(ticket.getEntryTime())) {
            activeTickets.put(key, ticket);
            throw new IllegalArgumentException("Exit time must not be before entry time.");
        }
        long minutes = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();
        double fee = ticket.getVehicle().calculateFee((int) minutes);
        ticket.close(exitTime, fee);
        if (ticket.getSlot() != null) {
            ticket.getSlot().release();
        }
        dailyCollection += fee;
        return fee;
    }

    public ParkingTicket getActiveTicket(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new IllegalArgumentException("Registration number is required.");
        }
        return activeTickets.get(registrationNumber.trim().toUpperCase());
    }

    public double getDailyCollection() {
        return dailyCollection;
    }

    public int activeTicketCount() {
        return activeTickets.size();
    }

    public int occupiedCount() {
        int count = 0;
        for (ParkingFloor floor : floors) {
            count += floor.occupiedCount();
        }
        return count;
    }
}
