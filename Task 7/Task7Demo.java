import java.util.*;

public class Task7Demo {
    public static void main(String[] args) {
        Vehicle car   = new Car("TN01AB1234", "Asha");
        Vehicle bike  = new Motorcycle("TN22CD2222", "Kiran");
        Vehicle truck = new Truck("TN33EF3333", "Meena");
        Vehicle ev    = new ElectricCar("TN01EV9999", "Ravi", 20);

        System.out.println("=== 1. Overriding: one call, four behaviours (10 hours) ===");
        for (Vehicle v : List.of(car, bike, truck, ev))
            System.out.printf("%-55s fee = %8.2f%n", v, v.calculateFee(10));
        System.out.println("Truck, 20 hours (capped)  : " + truck.calculateFee(20));
        System.out.println("Car, 0 hours (min 1 hour) : " + car.calculateFee(0));

        System.out.println("\n=== 2. Covariant return type ===");
        Car copied = ((Car) car).copyFor("NewOwner");        // no cast needed on result: returns Car
        System.out.println(copied);

        System.out.println("\n=== 3. equals() and hashCode() on registration number ===");
        Vehicle a = new Car("tn01 ab 1234", "Someone Else");      // same reg, different case/spacing/owner
        Vehicle b = new Motorcycle("TN01AB1234", "Another");      // same reg, different TYPE
        System.out.println("car.equals(a)       = " + car.equals(a));
        System.out.println("car.equals(b)       = " + car.equals(b));
        System.out.println("car.equals(bike)    = " + car.equals(bike));
        System.out.println("car.equals(null)    = " + car.equals(null));
        System.out.println("symmetric a<->car   = " + (a.equals(car) == car.equals(a)));
        System.out.println("hash equal          = " + (car.hashCode() == a.hashCode()));

        Set<Vehicle> parked = new HashSet<>();
        parked.add(car); parked.add(a); parked.add(b); parked.add(bike);
        System.out.println("HashSet size after adding car, a, b, bike = " + parked.size() + "  (expected 2)");
        Map<Vehicle, String> slot = new HashMap<>();
        slot.put(car, "Slot A1");
        System.out.println("Lookup with different object, same reg    = " + slot.get(a));

        System.out.println("\n=== 4. final class ParkingTicket ===");
        System.out.println(new ParkingTicket(car, 3));
        System.out.println(new ParkingTicket(ev, 3));

        System.out.println("\n=== 5. Switch alternative gives identical fees ===");
        SwitchVehicle sc = new SwitchVehicle("X", SwitchVehicle.Type.ELECTRIC_CAR, 20);
        System.out.println("switch EV fee(10h) = " + sc.calculateFee(10) + " | override EV fee(10h) = " + ev.calculateFee(10));

        System.out.println("\n=== 6. Micro-benchmark (indicative only, 50M calls each) ===");
        benchmark();
    }

    static void benchmark() {
        Vehicle[] poly = { new Car("A", "x"), new Motorcycle("B", "x"), new Truck("C", "x"), new ElectricCar("D", "x", 5) };
        SwitchVehicle[] sw = { new SwitchVehicle("A", SwitchVehicle.Type.CAR, 0), new SwitchVehicle("B", SwitchVehicle.Type.MOTORCYCLE, 0),
                               new SwitchVehicle("C", SwitchVehicle.Type.TRUCK, 0), new SwitchVehicle("D", SwitchVehicle.Type.ELECTRIC_CAR, 5) };
        final int N = 50_000_000;
        double sink = 0;
        for (int i = 0; i < 5_000_000; i++) { sink += poly[i & 3].calculateFee(i & 15); sink += sw[i & 3].calculateFee(i & 15); } // warm-up
        long t = System.nanoTime();
        for (int i = 0; i < N; i++) sink += poly[i & 3].calculateFee(i & 15);
        long polyNs = System.nanoTime() - t;
        t = System.nanoTime();
        for (int i = 0; i < N; i++) sink += sw[i & 3].calculateFee(i & 15);
        long swNs = System.nanoTime() - t;
        System.out.printf("Overriding (virtual dispatch): %.2f ns/call%n", polyNs / (double) N);
        System.out.printf("Switch on type tag           : %.2f ns/call%n", swNs / (double) N);
        System.out.println("(both O(1); checksum " + (long) sink + ")");
    }
}
