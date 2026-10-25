package parking.model;
import java.time.LocalDate;
public final class MonthlyPass {
    private final String registrationNumber;
    private final LocalDate validThrough;
    private final String holderCategory;
    public MonthlyPass(String registrationNumber, String holderCategory, LocalDate validThrough) {
        if (validThrough == null || holderCategory == null ||
            !(holderCategory.equalsIgnoreCase("STUDENT") || holderCategory.equalsIgnoreCase("STAFF")))
            throw new IllegalArgumentException("Pass requires student/staff and expiration");
        this.registrationNumber = new Car(registrationNumber).getRegistrationNumber();
        this.holderCategory = holderCategory.toUpperCase();
        this.validThrough = validThrough;
    }
    public boolean isValidFor(Vehicle vehicle, LocalDate day) {
        if (vehicle == null || day == null) throw new IllegalArgumentException("Vehicle and day required");
        return registrationNumber.equals(vehicle.getRegistrationNumber()) && !day.isAfter(validThrough);
    }
    public String getHolderCategory() { return holderCategory; }
    public LocalDate getValidThrough() { return validThrough; }
}
