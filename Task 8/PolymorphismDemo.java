import java.lang.reflect.Method;

/** Extra evidence: overloading, upcasting, static-vs-dynamic selection, and a traced dispatch. */
public class PolymorphismDemo {

    // --- overloads chosen by the COMPILER from the DECLARED type of the argument ---
    static String admit(Vehicle v) { return "admit(Vehicle)"; }
    static String admit(Car c)     { return "admit(Car)"; }

    public static void main(String[] args) {
        Vehicle v = new ElectricCar("TN3C3");

        System.out.println("=== 1. Compile-time polymorphism: overloaded calculate() ===");
        System.out.println("v.calculate(130)     = " + v.calculate(130)    + "   -> calculate(int)       [1 argument]");
        System.out.println("v.calculate(2, 10)   = " + v.calculate(2, 10)  + "   -> calculate(int,int)   [2 arguments]");
        byte b = 90;
        System.out.println("v.calculate(byte 90) = " + v.calculate(b)      + "   -> byte widened to int, calculate(int)");

        System.out.println("\n=== 2. Overloading is STATIC, overriding is DYNAMIC ===");
        Car realCar = new Car("TN0282");
        Vehicle asVehicle = new Car("TN0282");
        System.out.println("admit(realCar)    : " + admit(realCar)    + "   (declared type Car)");
        System.out.println("admit(asVehicle)  : " + admit(asVehicle)  + "   (declared type Vehicle, actual Car)");

        System.out.println("\n=== 3. Upcasting: one array, many types ===");
        Vehicle[] lot = { new TwoWheeler("TN01A1"), new Car("TN0282"), new ElectricCar("TN3C3") };
        for (Vehicle x : lot)
            System.out.printf("%-12s declared=Vehicle  actual=%-12s fee(130 min)=%6.1f%n",
                    x.getReg(), x.getClass().getSimpleName(), x.calculate(130));

        System.out.println("\n=== 4. Traced dynamic dispatch: x.calculate(130) with x = ElectricCar ===");
        Vehicle x = lot[2];
        System.out.println("Step 1 (compile time) : declared type of x = Vehicle -> compiler checks Vehicle has calculate(int): OK");
        System.out.println("Step 2 (run time)     : actual object type = " + x.getClass().getSimpleName());
        System.out.println("Step 3                : calculate(int) found in " + declaringClass(x.getClass(), "calculate", int.class)
                + " (not overridden lower down) -> its body runs");
        System.out.println("Step 4                : inside it, this.rate() is a virtual call; actual type is "
                + x.getClass().getSimpleName() + ", so the JVM searches from the bottom of the chain");
        System.out.println("Step 5                : rate() found in " + declaringClass(x.getClass(), "rate") + " -> returns 30");
        System.out.println("Step 6                : hours = (130+59)/60 = 3 ; fee = 3 * 30 = " + x.calculate(130));
        Vehicle t = lot[0];
        System.out.println("Same call, x = " + t.getClass().getSimpleName() + "  : rate() found in "
                + declaringClass(t.getClass(), "rate") + " -> 3 * 10 = " + t.calculate(130));

        System.out.println("\n=== 5. equals(): same plate => same vehicle ===");
        System.out.println(new Car("TN0282").equals(new ElectricCar("TN0282")));
        System.out.println(new Car("TN0282").equals(new Car("TN9999")));

        System.out.println("\n=== 6. Alternative: instanceof + cast (same fees) ===");
        for (Vehicle y : lot)
            System.out.println(y.getClass().getSimpleName() + " " + y.getReg() + " -> "
                    + InstanceofFeeCalculator.fee(y, 130, 70));

        System.out.println("\n=== 7. Micro-benchmark (indicative only, 50M calls each) ===");
        benchmark(lot);
    }

    /** Walks UP the class chain to find which class actually declares the method. */
    static String declaringClass(Class<?> c, String name, Class<?>... params) {
        while (c != null) {
            try { Method m = c.getDeclaredMethod(name, params); return c.getSimpleName(); }
            catch (NoSuchMethodException e) { c = c.getSuperclass(); }
        }
        return "?";
    }

    static void benchmark(Vehicle[] lot) {
        final int N = 50_000_000;
        double sink = 0;
        for (int i = 0; i < 5_000_000; i++) { sink += lot[i % 3].calculate(130); sink += InstanceofFeeCalculator.fee(lot[i % 3], 130, 0); }
        long t = System.nanoTime();
        for (int i = 0; i < N; i++) sink += lot[i % 3].calculate(i & 255);
        long dyn = System.nanoTime() - t;
        t = System.nanoTime();
        for (int i = 0; i < N; i++) sink += InstanceofFeeCalculator.fee(lot[i % 3], i & 255, 0);
        long ins = System.nanoTime() - t;
        System.out.printf("Dynamic dispatch   : %.2f ns/call%n", dyn / (double) N);
        System.out.printf("instanceof chain   : %.2f ns/call%n", ins / (double) N);
        System.out.println("(checksum " + (long) sink + ")");
    }
}
