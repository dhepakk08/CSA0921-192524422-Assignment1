/**
 * Must extend ChargeableBase (to get charging) -> CANNOT also extend CarAlt.
 * So reg, getReg(), rate()=30, equals() and hashCode() are all DUPLICATED from CarAlt.
 */
public class ElectricCarAlt extends ChargeableBase implements VehicleI {
    private final String reg;
    public ElectricCarAlt(String reg) { super(12.0); this.reg = reg; }
    @Override public String getReg() { return reg; }
    @Override public double rate() { return 30; }                      // copy of CarAlt.rate()
    @Override public boolean equals(Object o) { return o instanceof VehicleI && reg.equals(((VehicleI) o).getReg()); }
    @Override public int hashCode() { return reg.hashCode(); }
}
