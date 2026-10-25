public class CarAlt implements VehicleI {
    private final String reg;                         // field repeated in every implementer
    public CarAlt(String reg) { this.reg = reg; }
    @Override public String getReg() { return reg; }
    @Override public double rate() { return 30; }
    // equals()/hashCode() must be rewritten here (cannot be inherited from the interface)
    @Override public boolean equals(Object o) { return o instanceof VehicleI && reg.equals(((VehicleI) o).getReg()); }
    @Override public int hashCode() { return reg.hashCode(); }
}
