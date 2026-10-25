package parking.app;
import java.time.LocalDate;
import parking.model.*;
import parking.service.ParkingLot;
public final class Main {
    private Main() { }
    public static Vehicle createVehicle(String type, String reg) {
        if (type == null) throw new IllegalArgumentException("Unknown vehicle type");
        switch (type.trim().toUpperCase()) {
            case "TWO_WHEELER": case "BIKE": return new TwoWheeler(reg);
            case "CAR": return new Car(reg);
            case "TRUCK": return new Truck(reg);
            case "ELECTRIC_CAR": return new ElectricCar(reg);
            case "ELECTRIC_BIKE": return new ElectricBike(reg);
            default: throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot();
        LocalDate day = LocalDate.of(2026, 10, 25);
        ParkingTicket bike = lot.enter(createVehicle("BIKE", "TN01AB1234"), 0, day, null);
        ParkingTicket car = lot.enter(createVehicle("CAR", "TN02CD5678"), 10, day, null);
        ParkingTicket ev = lot.enter(createVehicle("ELECTRIC_CAR", "TN03EV9012"), 20, day, null);
        long a = lot.exit("TN01AB1234", bike.getFloorNumber(), bike.getSlotNumber(), 60, 0);
        long b = lot.exit("TN02CD5678", car.getFloorNumber(), car.getSlotNumber(), 71, 0);
        long c = lot.exit("TN03EV9012", ev.getFloorNumber(), ev.getSlotNumber(), 20, 2);
        System.out.println("Campus parking: " + lot.getFloorCount() + " floors / " + lot.getCapacity() + " slots");
        System.out.println("Fees: Rs " + a + ", Rs " + b + ", Rs " + c);
        System.out.println("Daily collection: Rs " + lot.getDailyCollection());
    }
}
