/** One superclass (Car) + two interfaces: a class can gain many CAPABILITIES but only one parent CLASS. */
public class ElectricCar extends Car implements Chargeable {
    public ElectricCar(String reg) { super(reg); }
    @Override public double charging(int kWh) { return kWh * RATE_PER_KWH; }
}
