import java.util.*;

public class AbstractionDemo {
    public static void main(String[] args) {
        Vehicle[] lot = { new TwoWheeler("TN01A1"), new Car("TN0282"),
                          new ElectricCar("TN3C3"), new ElectricBike("TN7B7") };

        System.out.println("=== 1. Abstract class: Vehicle fixes HOW the fee is computed, subclasses give rate() ===");
        for (Vehicle v : lot)
            System.out.printf("%-26s calculate(130) = %6.1f%n", v.label(), v.calculate(130));

        System.out.println("\n=== 2. Payable interface crosses hierarchies (Vehicle + non-Vehicle) ===");
        List<Payable> bills = new ArrayList<>(Arrays.asList(lot));
        bills.add(new MonthlyPermit("Dr. Rao", 1500));
        for (Payable p : bills) System.out.println(p.receipt(130));          // default method
        System.out.println("Total (static Payable.total) = " + Payable.total(bills, 130));

        System.out.println("\n=== 3. Chargeable capability only on EVs (different branches of the tree) ===");
        List<Chargeable> evs = new ArrayList<>();
        for (Vehicle v : lot) if (v instanceof Chargeable) evs.add((Chargeable) v);
        for (Chargeable c : evs)
            System.out.printf("%-12s charging(70) = %7.2f   with tax (default method) = %8.2f%n",
                    c.getClass().getSimpleName(), c.charging(70), c.chargingWithTax(70));
        System.out.println("Total (static Chargeable.totalCharging) = " + Chargeable.totalCharging(evs, 70));

        System.out.println("\n=== 4. Multiple behaviours: ElectricCar is-a ... ===");
        Object ev = lot[2];
        System.out.println("Vehicle    : " + (ev instanceof Vehicle));
        System.out.println("Car        : " + (ev instanceof Car));
        System.out.println("Payable    : " + (ev instanceof Payable));
        System.out.println("Chargeable : " + (ev instanceof Chargeable));
        System.out.println("TwoWheeler : " + (ev instanceof TwoWheeler));

        System.out.println("\n=== 5. Call cost: abstract-class call vs interface call (50M, indicative) ===");
        benchmark(lot);
    }

    static void benchmark(Vehicle[] lot) {
        final int N = 50_000_000;
        Payable[] pay = lot;                              // same objects viewed through the interface
        double sink = 0;
        for (int i = 0; i < 5_000_000; i++) { sink += lot[i & 3].calculate(130); sink += pay[i & 3].amountDue(130); }
        long t = System.nanoTime();
        for (int i = 0; i < N; i++) sink += lot[i & 3].calculate(i & 255);
        long cls = System.nanoTime() - t;
        t = System.nanoTime();
        for (int i = 0; i < N; i++) sink += pay[i & 3].amountDue(i & 255);
        long itf = System.nanoTime() - t;
        System.out.printf("via abstract class Vehicle.calculate : %.2f ns/call%n", cls / (double) N);
        System.out.printf("via interface Payable.amountDue      : %.2f ns/call%n", itf / (double) N);
        System.out.println("(checksum " + (long) sink + ")");
    }
}
