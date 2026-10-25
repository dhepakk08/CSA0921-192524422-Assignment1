package parking.app;

import parking.model.Car;
import parking.model.ElectricCar;
import parking.model.Motorcycle;
import parking.model.ParkingSlot;
import parking.model.ParkingTicket;
import parking.model.Truck;
import parking.model.Vehicle;
import parking.service.ParkingLot;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot("North Gate", 2, 3);
        Vehicle car = new Car("TN01AB1234", "Asha");
        Vehicle bike = new Motorcycle("TN22CD2222", "Kiran");
        ElectricCar electricCar = new ElectricCar("TN01EV9999", "Ravi", 80);
        Vehicle truck = new Truck("TN33EF3333", "Meena");

        ParkingSlot carSlot = lot.park(car);
        lot.park(bike);
        lot.park(electricCar);
        ParkingTicket ticket = new ParkingTicket(
                "T1001", car, LocalDateTime.of(2026, 10, 25, 9, 0));

        System.out.println("=== TASK 10: ENCAPSULATION ===");
        System.out.println("Lot: " + lot.getLotName());
        System.out.println("Car slot: " + carSlot.getSlotNumber());
        System.out.println("Occupied slots: " + lot.occupiedCount());
        System.out.println("Ticket owner: " + ticket.getVehicle().getOwnerName());
        System.out.printf("Car fee (130 minutes): %.2f%n", car.calculateFee(130));
        System.out.printf("Electric car fee + charging: %.2f%n",
                electricCar.calculateFee(130) + electricCar.chargingFee(20));
        System.out.printf("Truck fee (20 hours capped): %.2f%n", truck.calculateFee(20 * 60));

        Integer boxedMinutes = 130;
        Double boxedFee = car.calculateFee(boxedMinutes);
        System.out.println("Autoboxing example: Integer -> int, double -> Double = " + boxedFee);

        try {
            electricCar.setBatteryCapacityKWh(0);
        } catch (IllegalArgumentException exception) {
            System.out.println("Validation rejected invalid battery capacity.");
        }
    }
}
