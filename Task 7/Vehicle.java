import java.util.Locale;

/** Task 7 - Abstract superclass. Fee behaviour is deferred to subclasses. */
public abstract class Vehicle {
    public static final double GST_RATE = 0.18;            // final variable (constant)

    private final String regNo;                            // final field: identity never changes
    private final String ownerName;

    protected Vehicle(String regNo, String ownerName) {
        // normalise so "tn01 ab 1234" and "TN01AB1234" are the same vehicle
        this.regNo = regNo.replace(" ", "").toUpperCase(Locale.ROOT);
        this.ownerName = ownerName;
    }

    /** final method: no subclass may change how identity is exposed. */
    public final String getRegNo()     { return regNo; }
    public final String getOwnerName() { return ownerName; }

    /** Behaviour that MUST differ per subclass -> abstract, forces every subclass to override. */
    public abstract double calculateFee(int hours);

    /** protected in the superclass; subclasses may widen it to public. */
    protected String category() { return "Vehicle"; }

    /** Covariant-return hook: subclasses override it returning their own type. */
    public Vehicle copyFor(String newOwner) { throw new UnsupportedOperationException(); }

    protected final int billableHours(int hours) { return Math.max(1, hours); }   // shared rule, not overridable

    // ---------- Object-class overrides ----------
    @Override
    public String toString() {
        return category() + "[reg=" + regNo + ", owner=" + ownerName + "]";
    }

    /** final: equality rule is "same registration number" for ALL vehicle types. */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;                        // reflexive shortcut
        if (!(o instanceof Vehicle)) return false;         // also handles null
        Vehicle other = (Vehicle) o;
        return regNo.equals(other.regNo);                  // only regNo decides
    }

    /** final: must use exactly the same field(s) as equals(). */
    @Override
    public final int hashCode() { return regNo.hashCode(); }
}
