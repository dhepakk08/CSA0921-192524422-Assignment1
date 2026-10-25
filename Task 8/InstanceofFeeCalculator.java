/**
 * Alternative design: ONE static method that tests the type with instanceof and casts.
 * ORDER MATTERS: ElectricCar must be tested BEFORE Car, because every ElectricCar is also a Car.
 */
public class InstanceofFeeCalculator {
    public static double fee(Vehicle v, int minutes, int kWh) {
        int hours = (minutes + 59) / 60;
        if (v instanceof ElectricCar) {                       // check 1
            return hours * 30 + ((ElectricCar) v).charging(kWh);
        } else if (v instanceof Car) {                        // check 2
            return hours * 30;
        } else if (v instanceof TwoWheeler) {                 // check 3
            return hours * 10;
        } else {
            throw new IllegalArgumentException("Unknown vehicle type: " + v.getClass());
        }
    }
}
