package parking.test;

import parking.model.Car;
import parking.model.DeliveryVan;
import parking.model.EBike;
import parking.model.Motorcycle;
import parking.model.Visitor;
import parking.pass.OneDayPass;
import parking.service.ParkingLot;
import parking.service.PricingPolicy;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Task12Test {
    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        test("E-Bike flat fee", () -> {
            ParkingLot lot = new ParkingLot("Test", new int[]{1});
            LocalDateTime entry = time(8);
            lot.enterVehicle(new EBike("EB1", "Asha"), entry);
            assertEquals(25.0, lot.exitVehicle("EB1", entry.plusHours(8)));
        });
        test("DeliveryVan off-peak car rate", () -> {
            ParkingLot lot = new ParkingLot("Test", new int[]{1});
            LocalDateTime entry = time(7);
            lot.enterVehicle(new DeliveryVan("DV1", "Ravi"), entry);
            assertEquals(30.0, lot.exitVehicle("DV1", entry.plusHours(1)));
        });
        test("DeliveryVan peak rate doubles", () -> {
            ParkingLot lot = new ParkingLot("Test", new int[]{1});
            LocalDateTime entry = time(9);
            lot.enterVehicle(new DeliveryVan("DV1", "Ravi"), entry);
            assertEquals(60.0, lot.exitVehicle("DV1", entry.plusHours(1)));
        });
        test("DeliveryVan crossing peak boundary", () -> {
            ParkingLot lot = new ParkingLot("Test", new int[]{1});
            LocalDateTime entry = time(8);
            lot.enterVehicle(new DeliveryVan("DV1", "Ravi"), entry);
            assertEquals(90.0, lot.exitVehicle("DV1", entry.plusHours(2)));
        });
        test("OneDayPass valid only on its date", () -> {
            OneDayPass pass = new OneDayPass(
                    "P1", new Visitor("V1", "Meena"), LocalDate.of(2026, 10, 25));
            assertTrue(pass.isValidOn(LocalDate.of(2026, 10, 25)));
            assertTrue(!pass.isValidOn(LocalDate.of(2026, 10, 26)));
        });
        test("five-floor jagged capacities", () -> {
            ParkingLot lot = new ParkingLot("Test", new int[]{1, 2, 3, 4, 5});
            assertEquals(1, lot.getFloorCapacity(1));
            assertEquals(5, lot.getFloorCapacity(5));
        });
        test("existing car and motorcycle pricing preserved", () -> {
            ParkingLot lot = new ParkingLot("Test", new int[]{2});
            LocalDateTime entry = time(10);
            lot.enterVehicle(new Car("C1", "Asha"), entry);
            lot.enterVehicle(new Motorcycle("M1", "Bala"), entry);
            assertEquals(30.0, lot.exitVehicle("C1", entry.plusHours(1)));
            assertEquals(10.0, lot.exitVehicle("M1", entry.plusHours(1)));
        });
        test("invalid capacity rejected", () ->
                assertThrows(() -> new ParkingLot("Test", new int[]{2, 0})));
        test("invalid pass data rejected", () ->
                assertThrows(() -> new OneDayPass("", new Visitor("V1", "A"), LocalDate.now())));
        test("unknown pricing category rejected", () ->
                assertThrows(() -> new PricingPolicy().rateFor("BUS")));

        System.out.printf("%nTASK 12 TEST SUMMARY: %d passed, %d failed%n", passed, failed);
        if (failed > 0) {
            throw new AssertionError("Task 12 tests failed.");
        }
    }

    private static LocalDateTime time(int hour) {
        return LocalDateTime.of(2026, 10, 25, hour, 0);
    }

    private static void test(String name, Runnable action) {
        try {
            action.run();
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

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("expected true");
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
