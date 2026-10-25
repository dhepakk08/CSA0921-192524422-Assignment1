public class FeeDemo {
    public static void main(String[] args) {
        Vehicle[] parked = { new TwoWheeler("TN01A1"), new Car("TN0282"), new ElectricCar("TN3C3") };

        for (Vehicle x : parked) {
            double fee = x.calculate(130);                         // dynamic method dispatch
            if (x instanceof Chargeable)                           // capability check (interface)
                fee += ((Chargeable) x).charging(70);
            System.out.println(x.getClass().getSimpleName() + " " + x.getReg() + " -> " + fee);
        }

        System.out.println(new Car("TN0282").equals(new ElectricCar("TN0282")));
    }
}
