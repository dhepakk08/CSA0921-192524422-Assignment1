public class ElectricCar extends Car implements Chargeable {
    public ElectricCar(String reg) { super(reg); }

    // TODO 3 (done): charging at 12 per kWh
    @Override public double charging(int kWh) { return kWh * 12.0; }
    // rate() is NOT overridden: inherited from Car (30/hour)
}
