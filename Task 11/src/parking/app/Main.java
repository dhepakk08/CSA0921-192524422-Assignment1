package parking.app;

import parking.model.Car;
import parking.model.ElectricCar;
import parking.model.Motorcycle;
import parking.model.ParkingTicket;
import parking.model.Truck;
import parking.service.ParkingLot;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot("Campus North Gate", 1, 4);
        LocalDateTime start = LocalDateTime.of(2026, 10, 25, 9, 0);

        ParkingTicket carTicket = lot.enterVehicle(
                new Car("TN01AB1234", "Asha"), start);
        ParkingTicket bikeTicket = lot.enterVehicle(
                new Motorcycle("TN22CD2222", "Kiran"), start);
        ParkingTicket electricTicket = lot.enterVehicle(
                new ElectricCar("TN01EV9999", "Ravi", 80), start);
        ParkingTicket truckTicket = lot.enterVehicle(
                new Truck("TN33EF3333", "Meena"), start);

        System.out.println("=== CAMPUS SMART-PARKING SYSTEM ===");
        System.out.println("Active tickets after entry: " + lot.activeTicketCount());
        System.out.printf("Car exit fee: Rs. %.2f%n",
                lot.exitVehicle(carTicket.getVehicle().getRegistrationNumber(), start.plusMinutes(130)));
        System.out.printf("Motorcycle exit fee: Rs. %.2f%n",
                lot.exitVehicle(bikeTicket.getVehicle().getRegistrationNumber(), start.plusMinutes(60)));
        System.out.printf("Electric car exit fee: Rs. %.2f%n",
                lot.exitVehicle(electricTicket.getVehicle().getRegistrationNumber(), start.plusMinutes(61)));
        System.out.printf("Truck exit fee: Rs. %.2f%n",
                lot.exitVehicle(truckTicket.getVehicle().getRegistrationNumber(), start.plusHours(20)));
        System.out.println("Active tickets after exit: " + lot.activeTicketCount());
        System.out.printf("Daily collection: Rs. %.2f%n", lot.getDailyCollection());

        ParkingLot manualLot = new ParkingLot("Manual Collection Demo", 1, 2);
        ParkingTicket manualBike = manualLot.enterVehicle(
                new Motorcycle("TN44GH4444", "Anita"), start);
        ParkingTicket manualCar = manualLot.enterVehicle(
                new Car("TN55IJ5555", "Vijay"), start);
        double motorcycleFee = manualLot.exitVehicle(
                manualBike.getVehicle().getRegistrationNumber(), start.plusMinutes(60));
        double carFee = manualLot.exitVehicle(
                manualCar.getVehicle().getRegistrationNumber(), start.plusMinutes(61));
        double chargingFee = new ElectricCar("TN66EV6666", "Divya", 40).chargingFee(2);

        System.out.println();
        System.out.println("=== MANUAL RS. 94 COLLECTION SCENARIO ===");
        System.out.printf("Motorcycle parking: Rs. %.2f%n", motorcycleFee);
        System.out.printf("Car parking: Rs. %.2f%n", carFee);
        System.out.printf("Electric-car charging (2 kWh): Rs. %.2f%n", chargingFee);
        System.out.printf("Manual total: Rs. %.2f%n",
                motorcycleFee + carFee + chargingFee);
    }
}
