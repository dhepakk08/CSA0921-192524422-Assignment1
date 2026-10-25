package parking.app;

import parking.model.Car;
import parking.model.DeliveryVan;
import parking.model.EBike;
import parking.model.Vehicle;
import parking.model.Visitor;
import parking.pass.OneDayPass;
import parking.service.ParkingLot;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        int[] capacities = {2, 3, 1, 4, 2};
        ParkingLot lot = new ParkingLot("Adaptive Campus Lot", capacities);
        LocalDateTime morning = LocalDateTime.of(2026, 10, 25, 8, 0);

        Vehicle bike = new EBike("EB01", "Asha");
        Vehicle van = new DeliveryVan("DV01", "Ravi");
        lot.enterVehicle(bike, morning);
        lot.enterVehicle(van, morning);

        Visitor visitor = new Visitor("V100", "Meena");
        OneDayPass pass = new OneDayPass("P100", visitor, morning.toLocalDate());

        System.out.println("=== TASK 12: DESIGN ADAPTATION ===");
        System.out.println("Floor capacities: " + java.util.Arrays.toString(capacities));
        System.out.printf("E-Bike flat fee: Rs. %.2f%n",
                lot.exitVehicle("EB01", morning.plusHours(5)));
        System.out.printf("Delivery van 8:00-10:00 fee: Rs. %.2f%n",
                lot.exitVehicle("DV01", morning.plusHours(2)));
        System.out.println("One-day visitor pass valid: " + pass.isValidOn(LocalDate.of(2026, 10, 25)));
        System.out.println("Active tickets: " + lot.activeTicketCount());
    }
}
