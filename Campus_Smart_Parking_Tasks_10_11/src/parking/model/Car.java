package parking.model;
public class Car extends Vehicle {
    public Car(String reg) { super(reg); }
    @Override protected int rate() { return 30; }
    @Override public String getType() { return "CAR"; }
}
