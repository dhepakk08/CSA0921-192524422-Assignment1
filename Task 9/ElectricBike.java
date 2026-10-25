/** Chargeable in a DIFFERENT branch of the tree: an abstract class could not be shared like this. */
public class ElectricBike extends TwoWheeler implements Chargeable {
    public ElectricBike(String reg) { super(reg); }
    @Override public double charging(int kWh) { return kWh * RATE_PER_KWH * 0.5; }
}
