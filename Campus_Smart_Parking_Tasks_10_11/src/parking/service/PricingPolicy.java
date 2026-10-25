package parking.service;
import parking.model.*;
public final class PricingPolicy {
    private PricingPolicy() { }
    public static long fee(ParkingTicket ticket, long durationMinutes, int chargedKWh) {
        if (ticket == null || durationMinutes < 0 || chargedKWh < 0)
            throw new IllegalArgumentException("Invalid billing inputs");
        Vehicle vehicle = ticket.getVehicle();
        if (chargedKWh > 0 && !(vehicle instanceof Chargeable))
            throw new IllegalArgumentException("Cannot charge a non-electric vehicle");
        long parking = ticket.hasValidPass() ? 0 : vehicle.amountDue(durationMinutes);
        long energy = vehicle instanceof Chargeable ? ((Chargeable)vehicle).charging(chargedKWh) : 0;
        return Math.addExact(parking, energy);
    }
}
