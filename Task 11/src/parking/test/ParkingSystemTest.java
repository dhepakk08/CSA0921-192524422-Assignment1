package parking.test;

import parking.model.Car;
import parking.model.ElectricCar;
import parking.model.Motorcycle;
import parking.model.ParkingSlot;
import parking.model.Truck;
import parking.service.ParkingLot;
import parking.service.PricingPolicy;

import java.time.LocalDateTime;

public final class ParkingSystemTest {
    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        test("normal vehicle entry and exit", () -> {
            ParkingLot lot = new ParkingLot("Test", 1, 2);
            LocalDateTime entry = LocalDateTime.of(2026, 10, 25, 9, 0);
            lot.enterVehicle(new Car("TN01", "Asha"), entry);
            assertEquals(90.0, lot.exitVehicle("TN01", entry.plusMinutes(130)));
        });
        test("last available slot allocation", () -> {
            ParkingLot lot = new ParkingLot("Test", 1, 2);
            LocalDateTime entry = LocalDateTime.of(2026, 10, 25, 9, 0);
            lot.enterVehicle(new Motorcycle("TN01", "Asha"), entry);
            lot.enterVehicle(new Truck("TN02", "Bala"), entry);
            assertEquals(2, lot.activeTicketCount());
        });
        test("zero-minute parking bills one hour", () -> {
            ParkingLot lot = new ParkingLot("Test", 1, 1);
            LocalDateTime entry = LocalDateTime.of(2026, 10, 25, 9, 0);
            lot.enterVehicle(new Motorcycle("TN01", "Asha"), entry);
            assertEquals(10.0, lot.exitVehicle("TN01", entry));
        });
        test("exact 60-minute boundary bills one hour", () -> {
            ParkingLot lot = new ParkingLot("Test", 1, 1);
            LocalDateTime entry = LocalDateTime.of(2026, 10, 25, 9, 0);
            lot.enterVehicle(new Car("TN01", "Asha"), entry);
            assertEquals(30.0, lot.exitVehicle("TN01", entry.plusMinutes(60)));
        });
        test("61-minute boundary bills two hours", () -> {
            ParkingLot lot = new ParkingLot("Test", 1, 1);
            LocalDateTime entry = LocalDateTime.of(2026, 10, 25, 9, 0);
            lot.enterVehicle(new Car("TN01", "Asha"), entry);
            assertEquals(60.0, lot.exitVehicle("TN01", entry.plusMinutes(61)));
        });
        test("electric car and truck rules", () -> {
            assertEquals(240.0, new ElectricCar("EV", "Ravi", 80).chargingFee(20));
            assertEquals(600.0, new Truck("TR", "Meena").calculateFee(20 * 60));
        });
        test("unknown vehicle type rejected", () ->
                assertThrows(() -> new PricingPolicy().rateFor("BUS")));
        test("duplicate registration rejected", () -> {
            ParkingLot lot = new ParkingLot("Test", 1, 2);
            LocalDateTime entry = LocalDateTime.of(2026, 10, 25, 9, 0);
            lot.enterVehicle(new Car("TN01", "Asha"), entry);
            assertThrows(() -> lot.enterVehicle(new Motorcycle("tn01", "Bala"), entry));
        });
        test("invalid slot number rejected", () ->
                assertThrows(() -> new ParkingSlot(0)));
        test("daily collection is accumulated", () -> {
            ParkingLot lot = new ParkingLot("Test", 1, 2);
            LocalDateTime entry = LocalDateTime.of(2026, 10, 25, 9, 0);
            lot.enterVehicle(new Motorcycle("TN01", "Asha"), entry);
            lot.enterVehicle(new Car("TN02", "Bala"), entry);
            lot.exitVehicle("TN01", entry.plusMinutes(60));
            lot.exitVehicle("TN02", entry.plusMinutes(130));
            assertEquals(100.0, lot.getDailyCollection());
        });
        test("manual parking and charging total is Rs. 94", () -> {
            ParkingLot lot = new ParkingLot("Test", 1, 2);
            LocalDateTime entry = LocalDateTime.of(2026, 10, 25, 9, 0);
            lot.enterVehicle(new Motorcycle("TN01", "Asha"), entry);
            lot.enterVehicle(new Car("TN02", "Bala"), entry);
            double motorcycleFee = lot.exitVehicle("TN01", entry.plusMinutes(60));
            double carFee = lot.exitVehicle("TN02", entry.plusMinutes(61));
            double chargingFee = new ElectricCar("EV01", "Ravi", 40).chargingFee(2);
            assertEquals(94.0, motorcycleFee + carFee + chargingFee);
        });

        System.out.printf("%nTEST SUMMARY: %d passed, %d failed%n", passed, failed);
        if (failed > 0) {
            throw new AssertionError("Automated tests failed.");
        }
    }

    private static void test(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS: " + name);
        } catch (RuntimeException | AssertionError exception) {
            failed++;
            System.out.println("FAIL: " + name + " -> " + exception.getMessage());
        }
    }

    private static void assertEquals(double expected, double actual) {
        if (Math.abs(expected - actual) > 0.000001) {
            throw new AssertionError("expected " + expected + ", actual " + actual);
        }
    }

    private static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("expected " + expected + ", actual " + actual);
        }
    }

    private static void assertThrows(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException | IllegalStateException expected) {
            return;
        }
        throw new AssertionError("expected validation exception");
    }
}
