import java.util.Collection;

/** CAPABILITY: "can be billed". Any class, in any hierarchy, may implement it. */
public interface Payable {
    double GST_RATE = 0.18;                       // interface fields are implicitly public static final

    double amountDue(int minutes);                // abstract: WHAT must be provided

    /** default method: shared behaviour built only from the abstract methods. */
    default String label() { return getClass().getSimpleName(); }

    default String receipt(int minutes) {
        double total = amountDue(minutes) * (1 + GST_RATE);
        return String.format("%-22s Rs %8.2f (incl. %.0f%% GST)", label(), total, GST_RATE * 100);
    }

    /** static method: a utility that belongs to the interface, not to any object. */
    static double total(Collection<? extends Payable> items, int minutes) {
        double sum = 0;
        for (Payable p : items) sum += p.amountDue(minutes);
        return sum;
    }
}
