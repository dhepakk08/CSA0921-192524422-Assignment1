/**
 * ALTERNATIVE: Vehicle as an INTERFACE with default methods.
 * It cannot hold the registration number, cannot have a constructor,
 * cannot have protected members, and cannot define equals()/hashCode().
 */
public interface VehicleI {
    String getReg();                 // state is replaced by an accessor every implementer must back with its own field
    double rate();                   // must be PUBLIC now (interface members are always public)

    default double calculate(int minutes) {          // the shared algorithm CAN live here
        if (minutes < 0) throw new IllegalArgumentException("minutes must be >= 0");
        return ((minutes + 59) / 60) * rate();
    }
}
