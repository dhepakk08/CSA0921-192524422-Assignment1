package parking.model;
public final class ElectricCar extends Car implements Chargeable {
    public ElectricCar(String reg) { super(reg); }
    @Override public long charging(int kWh) {
        if (kWh < 0) throw new IllegalArgumentException("Negative charging");
        return Math.multiplyExact((long)kWh, 12);
    }
    @Override public String getType() { return "ELECTRIC_CAR"; }
}
