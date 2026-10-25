/** final class: an immutable receipt; extending it could break that guarantee. */
public final class ParkingTicket {
    private final Vehicle vehicle;
    private final int hours;
    private final double fee;
    public ParkingTicket(Vehicle v, int hours) {
        this.vehicle = v; this.hours = hours;
        this.fee = v.calculateFee(hours) * (1 + Vehicle.GST_RATE);   // polymorphic call
    }
    public double getFee() { return fee; }
    @Override public String toString() {
        return String.format("Ticket{%s, %dh, fee incl. GST = %.2f}", vehicle, hours, fee);
    }
}
