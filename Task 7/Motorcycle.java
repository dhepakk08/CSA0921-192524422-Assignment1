public class Motorcycle extends Vehicle {
    public static final double RATE = 10.0;
    public Motorcycle(String regNo, String ownerName) { super(regNo, ownerName); }

    @Override public double calculateFee(int hours) { return RATE * billableHours(hours); }
    @Override protected String category() { return "Motorcycle"; }
}
