import java.util.Collection;

/** CAPABILITY: "can be charged per kWh". Only electric vehicles implement it. */
public interface Chargeable {
    double RATE_PER_KWH = 12.0;

    double charging(int kWh);                     // abstract

    /** default method: new behaviour added without breaking existing implementers. */
    default double chargingWithTax(int kWh) {
        return charging(kWh) * (1 + Payable.GST_RATE);
    }

    /** static helper. */
    static double totalCharging(Collection<? extends Chargeable> items, int kWh) {
        double sum = 0;
        for (Chargeable c : items) sum += c.charging(kWh);
        return sum;
    }
}
