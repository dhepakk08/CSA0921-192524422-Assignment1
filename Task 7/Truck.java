public class Truck extends Vehicle {
    public static final double RATE = 50.0;
    private static final double OVERNIGHT_CAP = 600.0;   // trucks never pay more than this per day
    public Truck(String regNo, String ownerName) { super(regNo, ownerName); }

    @Override public double calculateFee(int hours) {
        return Math.min(OVERNIGHT_CAP, RATE * billableHours(hours));   // different ALGORITHM, not just rate
    }
    @Override protected String category() { return "Truck"; }
}
