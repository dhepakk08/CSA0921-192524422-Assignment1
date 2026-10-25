/** Capability interface: anything that can be charged per kWh. */
public interface Chargeable {
    double charging(int kWh);
}
