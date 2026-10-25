/** Alternative design: ONE class, a type tag, and a switch inside calculateFee(). */
public class SwitchVehicle {
    public enum Type { CAR, MOTORCYCLE, TRUCK, ELECTRIC_CAR }
    private final String regNo;
    private final Type type;
    private final double kWhCharged;

    public SwitchVehicle(String regNo, Type type, double kWhCharged) {
        this.regNo = regNo; this.type = type; this.kWhCharged = kWhCharged;
    }
    public double calculateFee(int hours) {
        int h = Math.max(1, hours);
        switch (type) {                                   // every new type edits THIS method
            case CAR:         return 20.0 * h;
            case MOTORCYCLE:  return 10.0 * h;
            case TRUCK:       return Math.min(600.0, 50.0 * h);
            case ELECTRIC_CAR:return 20.0 * h * 0.75 + kWhCharged * 8.0;
            default: throw new IllegalStateException("Unknown type " + type);
        }
    }
}
