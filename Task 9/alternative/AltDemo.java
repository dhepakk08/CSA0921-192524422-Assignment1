public class AltDemo {
    public static void main(String[] args) {
        VehicleI car = new CarAlt("TN0282");
        ElectricCarAlt ev = new ElectricCarAlt("TN3C3");
        System.out.println("CarAlt         fee(130) = " + car.calculate(130));
        System.out.println("ElectricCarAlt fee(130) = " + ev.calculate(130) + " + charging(70) = " + ev.charging(70));
        System.out.println("equals across types     = " + new CarAlt("TN0282").equals(new ElectricCarAlt("TN0282")));
        System.out.println("ev instanceof CarAlt    = " + (((Object) ev) instanceof CarAlt) + "   <- the is-a Car relationship is LOST");
    }
}
