public class ElectricCar extends Car {
    private static final double EV_DISCOUNT = 0.25;
    private static final double CHARGE_PER_KWH = 8.0;
    private final double kWhCharged;

    public ElectricCar(String regNo, String ownerName, double kWhCharged) {
        super(regNo, ownerName);
        this.kWhCharged = kWhCharged;
    }

    /** Re-uses Car's rule via super, then specialises it. */
    @Override public double calculateFee(int hours) {
        double parking = super.calculateFee(hours) * (1 - EV_DISCOUNT);
        return parking + kWhCharged * CHARGE_PER_KWH;
    }
    @Override public String category() { return "ElectricCar"; }

    @Override public String toString() { return super.toString() + "{kWh=" + kWhCharged + "}"; }
}
