package parking.model;
public final class Truck extends Vehicle {
    public Truck(String reg) { super(reg); }
    @Override protected int rate() { return 50; }
    @Override public long amountDue(long minutes) { return Math.min(600, super.amountDue(minutes)); }
    @Override public String getType() { return "TRUCK"; }
}
