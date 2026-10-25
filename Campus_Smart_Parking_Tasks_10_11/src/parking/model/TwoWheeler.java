package parking.model;
public class TwoWheeler extends Vehicle {
    public TwoWheeler(String reg) { super(reg); }
    @Override protected int rate() { return 10; }
    @Override public String getType() { return "TWO_WHEELER"; }
}
