import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

class Vehicle {
    String registrationNumber;
    String type;

    Vehicle(String registrationNumber, String type) {
        this.registrationNumber = registrationNumber;
        this.type = type;
    }
}

class ParkingSlot {
    int floorNumber;
    int slotNumber;
    Vehicle vehicle;
    boolean available = true;

    ParkingSlot(int floorNumber, int slotNumber) {
        this.floorNumber = floorNumber;
        this.slotNumber = slotNumber;
    }

    void occupy(Vehicle vehicle) {
        this.vehicle = vehicle;
        available = false;
    }

    void release() {
        vehicle = null;
        available = true;
    }
}

class ParkingFloor {
    int floorNumber;
    ParkingSlot[] slots;

    ParkingFloor(int floorNumber, int capacity) {
        this.floorNumber = floorNumber;
        slots = new ParkingSlot[capacity];

        for (int i = 0; i < capacity; i++) {
            slots[i] = new ParkingSlot(floorNumber, i + 1);
        }
    }
}

class ParkingTicket {
    int ticketNumber;
    Vehicle vehicle;
    ParkingSlot slot;
    LocalDateTime entryTime;

    ParkingTicket(int ticketNumber, Vehicle vehicle,
                  ParkingSlot slot, LocalDateTime entryTime) {
        this.ticketNumber = ticketNumber;
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = entryTime;
    }
}

class PricingPolicy {
    private final BigDecimal bikeRate = new BigDecimal("20");
    private final BigDecimal carRate = new BigDecimal("40");
    private final BigDecimal evRate = new BigDecimal("50");
    private final BigDecimal evChargingRate = new BigDecimal("15");

    BigDecimal calculateFee(String type, long durationMinutes) {
        BigDecimal hourlyRate;

        switch (type) {
            case "BIKE":
                hourlyRate = bikeRate;
                break;
            case "CAR":
                hourlyRate = carRate;
                break;
            case "EV":
                hourlyRate = evRate.add(evChargingRate);
                break;
            default:
                throw new IllegalArgumentException("Invalid vehicle type.");
        }

        BigDecimal hours = BigDecimal.valueOf(durationMinutes)
                .divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);

        return hourlyRate.multiply(hours)
                .setScale(2, RoundingMode.HALF_UP);
    }
}

class ParkingLot {
    ParkingFloor[] floors;
    PricingPolicy pricingPolicy = new PricingPolicy();
    int nextTicketNumber = 1001;

    ParkingLot() {
        floors = new ParkingFloor[9];

        floors[0] = new ParkingFloor(1, 20);

        for (int i = 1; i <= 3; i++) {
            floors[i] = new ParkingFloor(i + 1, 50);
        }

        for (int i = 4; i < 9; i++) {
            floors[i] = new ParkingFloor(i + 1, 100);
        }
    }

    ParkingSlot findAvailableSlot() {
        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.slots) {
                if (slot.available) {
                    return slot;
                }
            }
        }
        return null;
    }

    ParkingSlot findSlot(int floorNumber, int slotNumber) {
        if (floorNumber < 1 || floorNumber > floors.length) {
            return null;
        }

        ParkingFloor floor = floors[floorNumber - 1];

        if (slotNumber < 1 || slotNumber > floor.slots.length) {
            return null;
        }

        return floor.slots[slotNumber - 1];
    }

    int getAvailableSlotCount() {
        int available = 0;

        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.slots) {
                if (slot.available) {
                    available++;
                }
            }
        }

        return available;
    }

    void displayAvailability() {
        int total = 0;
        int available = 0;

        for (ParkingFloor floor : floors) {
            int floorAvailable = 0;

            for (ParkingSlot slot : floor.slots) {
                total++;

                if (slot.available) {
                    available++;
                    floorAvailable++;
                }
            }

            System.out.println("Floor " + floor.floorNumber
                    + ": " + floorAvailable + "/" + floor.slots.length
                    + " slots available");
        }

        System.out.println("Total capacity: " + total);
        System.out.println("Available slots: " + available);
        System.out.println("Occupied slots: " + (total - available));
    }
}

public class Main {
    static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    static LocalDateTime readDateTime(Scanner sc, String message) {
        while (true) {
            System.out.print(message);

            try {
                return LocalDateTime.parse(sc.nextLine().trim(), FORMATTER);
            } catch (Exception e) {
                System.out.println(
                    "Invalid format. Use dd-MM-yyyy HH:mm (e.g. 19-10-2026 09:00)."
                );
            }
        }
    }

    static String readVehicleType(Scanner sc) {
        while (true) {
            System.out.print("Vehicle type (BIKE/CAR/EV): ");
            String type = sc.nextLine().trim().toUpperCase();

            if (type.equals("BIKE") || type.equals("CAR")
                    || type.equals("EV")) {
                return type;
            }

            System.out.println("Please enter BIKE, CAR or EV.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ParkingLot parkingLot = new ParkingLot();

        System.out.println("===== CAMPUS SMART-PARKING SYSTEM =====");
        System.out.println("Predefined floors: 9");
        System.out.println("Total parking capacity: 670 slots");

        boolean running = true;

        while (running) {
            System.out.println("\n1. Vehicle Entry");
            System.out.println("2. Vehicle Exit");
            System.out.println("3. Display Slot Availability");
            System.out.println("4. Exit Program");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": {
                    System.out.print("Vehicle registration number: ");
                    String regNo = sc.nextLine().trim().toUpperCase();

                    if (regNo.isEmpty()) {
                        System.out.println("Registration number cannot be empty.");
                        break;
                    }

                    String type = readVehicleType(sc);

                    ParkingSlot slot = parkingLot.findAvailableSlot();

                    if (slot == null) {
                        System.out.println("Parking is full.");
                        break;
                    }

                    LocalDateTime entryTime =
                            readDateTime(sc, "Entry time (dd-MM-yyyy HH:mm): ");

                    Vehicle vehicle = new Vehicle(regNo, type);
                    slot.occupy(vehicle);

                    ParkingTicket ticket = new ParkingTicket(
                            parkingLot.nextTicketNumber++, vehicle, slot, entryTime
                    );

                    System.out.println("\nVehicle parked successfully.");
                    System.out.println("Ticket number: " + ticket.ticketNumber);
                    System.out.println("Registration: " + vehicle.registrationNumber);
                    System.out.println("Type: " + vehicle.type);
                    System.out.println("Floor: " + slot.floorNumber);
                    System.out.println("Slot: " + slot.slotNumber);
                    System.out.println("Entry time: "
                            + ticket.entryTime.format(FORMATTER));
                    System.out.println("Slot availability: Occupied");
                        System.out.println("Remaining slots: "
                            + parkingLot.getAvailableSlotCount());

                    // This simple version keeps active tickets in memory.
                    ActiveTickets.add(ticket);
                    break;
                }

                case "2": {
                    System.out.print("Enter ticket number: ");
                    int ticketNumber;

                    try {
                        ticketNumber = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ticket number.");
                        break;
                    }

                    ParkingTicket ticket = ActiveTickets.find(ticketNumber);

                    if (ticket == null) {
                        System.out.println("Active ticket not found.");
                        break;
                    }

                    LocalDateTime exitTime =
                            readDateTime(sc, "Exit time (dd-MM-yyyy HH:mm): ");

                    if (exitTime.isBefore(ticket.entryTime)) {
                        System.out.println("Exit time cannot be before entry time.");
                        break;
                    }

                    long durationMinutes =
                            Duration.between(ticket.entryTime, exitTime).toMinutes();

                    BigDecimal fee = parkingLot.pricingPolicy.calculateFee(
                            ticket.vehicle.type, durationMinutes
                    );

                    System.out.println("\n===== PARKING BILL =====");
                    System.out.println("Ticket number: " + ticket.ticketNumber);
                    System.out.println("Registration: "
                            + ticket.vehicle.registrationNumber);
                    System.out.println("Vehicle type: " + ticket.vehicle.type);
                    System.out.println("Floor: " + ticket.slot.floorNumber);
                    System.out.println("Slot: " + ticket.slot.slotNumber);
                    System.out.println("Entry time: "
                            + ticket.entryTime.format(FORMATTER));
                    System.out.println("Exit time: "
                            + exitTime.format(FORMATTER));
                    System.out.println("Duration: " + durationMinutes + " minutes");
                    System.out.println("Parking fee: Rs. " + fee);

                    ticket.slot.release();
                    ActiveTickets.remove(ticket);

                    System.out.println("Slot availability after exit: Available");
                        System.out.println("Remaining slots: "
                            + parkingLot.getAvailableSlotCount());
                    break;
                }

                case "3":
                    parkingLot.displayAvailability();
                    break;

                case "4":
                    running = false;
                    System.out.println("Parking system closed.");
                    break;

                default:
                    System.out.println("Invalid option. Choose 1 to 4.");
            }
        }

        sc.close();
    }
}

class ActiveTickets {
    private static final java.util.ArrayList<ParkingTicket> tickets =
            new java.util.ArrayList<>();

    static void add(ParkingTicket ticket) {
        tickets.add(ticket);
    }

    static ParkingTicket find(int ticketNumber) {
        for (ParkingTicket ticket : tickets) {
            if (ticket.ticketNumber == ticketNumber) {
                return ticket;
            }
        }
        return null;
    }

    static void remove(ParkingTicket ticket) {
        tickets.remove(ticket);
    }
}