/**
 * ALTERNATIVE: Chargeable as an ABSTRACT CLASS. It can now hold state and a constructor,
 * but a class may extend only ONE class, so using it uses up the single inheritance slot.
 */
public abstract class ChargeableBase {
    private final double ratePerKWh;                 // instance field: +8 bytes in EVERY charging object
    protected ChargeableBase(double ratePerKWh) { this.ratePerKWh = ratePerKWh; }
    public double charging(int kWh) { return kWh * ratePerKWh; }
}
