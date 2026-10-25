package parking.model;
import java.util.Locale;
import java.util.Objects;
public abstract class Vehicle implements Payable {
    private final String registrationNumber;
    protected Vehicle(String registrationNumber) {
        if (registrationNumber == null) throw new IllegalArgumentException("Registration required");
        String clean = registrationNumber.replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
        if (!clean.matches("[A-Z0-9-]{3,15}")) throw new IllegalArgumentException("Invalid registration");
        this.registrationNumber = clean;
    }
    public final String getRegistrationNumber() { return registrationNumber; }
    protected abstract int rate();
    public final long calculate(long minutes) { return amountDue(minutes); }
    public final long calculate(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes >= 60) throw new IllegalArgumentException("Invalid duration");
        return amountDue(Math.addExact(Math.multiplyExact((long)hours, 60), minutes));
    }
    @Override public long amountDue(long minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Negative duration");
        long slabs = minutes / 60 + (minutes % 60 == 0 ? 0 : 1);
        return Math.multiplyExact(slabs, rate());
    }
    public abstract String getType();
    @Override public final boolean equals(Object obj) {
        return obj instanceof Vehicle && registrationNumber.equals(((Vehicle)obj).registrationNumber);
    }
    @Override public final int hashCode() { return Objects.hashCode(registrationNumber); }
    @Override public String toString() { return getType() + "[" + registrationNumber + "]"; }
}
