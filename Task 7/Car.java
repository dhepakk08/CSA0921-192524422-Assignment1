public class Car extends Vehicle {
    public static final double RATE = 20.0;
    public Car(String regNo, String ownerName) { super(regNo, ownerName); }

    @Override public double calculateFee(int hours) { return RATE * billableHours(hours); }

    @Override public String category() { return "Car"; }           // protected -> public (allowed: wider)

    @Override public Car copyFor(String newOwner) {                 // covariant return type: Car is-a Vehicle
        return new Car(getRegNo(), newOwner);
    }
}
