package parking.model;

public final class DeliveryVan extends Vehicle {
    public DeliveryVan(String registrationNumber, String ownerName) {
        super(registrationNumber, ownerName);
    }

    @Override
    public String getVehicleType() {
        return "DELIVERY_VAN";
    }

    @Override
    protected double hourlyRate() {
        return 30.0;
    }
}
