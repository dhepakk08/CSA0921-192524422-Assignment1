/**
 * ABSTRACT CLASS: a partly-built "is-a" base. It owns STATE (reg), a CONSTRUCTOR,
 * identity (equals/hashCode) and the shared fee algorithm; the one varying step, rate(), is abstract.
 */
public abstract class Vehicle implements Payable {
    private final String reg;                      // state: interfaces cannot hold this

    protected Vehicle(String reg) { this.reg = reg; }    // constructor: interfaces cannot have one

    public String getReg() { return reg; }

    protected abstract double rate();              // WHAT differs per subclass (protected: internal detail)

    /** Template method: HOW the fee is computed is fixed here; only rate() varies. */
    public double calculate(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("minutes must be >= 0");
        int hours = (minutes + 59) / 60;           // every started hour is charged in full
        return hours * rate();
    }

    @Override public double amountDue(int minutes) { return calculate(minutes); }   // fulfils Payable
    @Override public String label() { return getClass().getSimpleName() + " " + reg; }  // overrides the default

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        return reg.equals(((Vehicle) o).reg);
    }
    @Override public int hashCode() { return reg.hashCode(); }
}
