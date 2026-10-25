/** Not a Vehicle at all, yet Payable: interfaces cross class hierarchies. */
public class MonthlyPermit implements Payable {
    private final String holder;
    private final double monthlyFee;
    public MonthlyPermit(String holder, double monthlyFee) { this.holder = holder; this.monthlyFee = monthlyFee; }
    @Override public double amountDue(int minutes) { return monthlyFee; }     // flat fee
    @Override public String label() { return "Permit " + holder; }
}
