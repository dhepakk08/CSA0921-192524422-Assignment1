public class Car extends Vehicle {
    // TODO 2 (done): constructor using super, and override rate() to return 30
    public Car(String reg) { super(reg); }
    @Override protected double rate() { return 30; }
}
