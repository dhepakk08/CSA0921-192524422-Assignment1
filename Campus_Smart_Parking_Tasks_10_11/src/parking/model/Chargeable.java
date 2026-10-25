package parking.model;
public interface Chargeable {
    long charging(int kWh);
    default long chargingWithTax(int kWh) { return charging(kWh) * 118 / 100; }
}
