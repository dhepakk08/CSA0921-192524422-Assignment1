public class Car extends Vehicle {
    public Car(String reg) { super(reg); }
    @Override protected double rate() { return 30; }
}
