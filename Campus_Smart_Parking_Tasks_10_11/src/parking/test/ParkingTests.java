package parking.test;
import java.time.LocalDate;
import parking.model.*;
import parking.app.Main;
import parking.service.*;
public final class ParkingTests {
    private static int passed = 0;
    private static final LocalDate DAY = LocalDate.of(2026,10,25);
    private static void check(String label, boolean valid) {
        if (!valid) throw new AssertionError(label);
        passed++;
        System.out.println("PASS: " + label);
    }
    private static void rejects(String label, Runnable action) {
        try { action.run(); } catch (IllegalArgumentException | IllegalStateException ex) { check(label,true); return; }
        throw new AssertionError("Expected rejection: " + label);
    }
    public static void main(String[] args) {
        ParkingLot campus = new ParkingLot();
        check("Nine floors", campus.getFloorCount()==9);
        check("670 campus slots", campus.getCapacity()==670);
        ParkingLot lot = new ParkingLot(new int[]{3,2});
        ParkingTicket bike = lot.enter(new TwoWheeler("tn01 ab1234"),0,DAY,null);
        ParkingTicket car = lot.enter(new Car("TN02CD5678"),10,DAY,null);
        ParkingTicket ev = lot.enter(new ElectricCar("TN03EV9012"),20,DAY,null);
        check("Regular allocation", bike.getFloorNumber()==1 && bike.getSlotNumber()==1);
        check("Car gets another slot", car.getFloorNumber()==1 && car.getSlotNumber()==2);
        check("EV gets reserved bay", ev.getFloorNumber()==1 && ev.getSlotNumber()==3);
        check("Last bay on floor 1 occupied", lot.isOccupied(1,3));
        rejects("Unknown vehicle type", ()->Main.createVehicle("PLANE","TN04AA1234"));
        rejects("Duplicate plate", ()->lot.enter(new Car("tn01ab1234"),30,DAY,null));
        rejects("Invalid floor", ()->lot.exit("TN01AB1234",0,1,60,0));
        rejects("Invalid slot", ()->lot.exit("TN01AB1234",1,9,60,0));
        rejects("Wrong slot", ()->lot.exit("TN01AB1234",1,2,60,0));
        check("Rejected exit preserved occupancy", lot.getActiveCount()==3);
        rejects("Exit before entry", ()->lot.exit("TN02CD5678",1,2,9,0));
        rejects("Negative fee duration", ()->new Car("TN04AB1234").amountDue(-1));
        rejects("Negative energy", ()->((Chargeable)new ElectricCar("TN04EV1234")).charging(-1));
        rejects("Non-EV cannot charge", ()->lot.exit("TN02CD5678",1,2,60,1));
        check("60 minutes exactly one slab", lot.exit("TN01AB1234",1,1,60,0)==10);
        check("61 minutes two slabs", lot.exit("TN02CD5678",1,2,71,0)==60);
        check("Zero minutes EV plus 2 kWh", lot.exit("TN03EV9012",1,3,20,2)==24);
        check("Hand-calculated daily total 94", lot.getDailyCollection()==94);
        check("All spaces released", lot.getActiveCount()==0);
        check("Reused first freed slot", lot.enter(new TwoWheeler("TN05AA1234"),0,DAY,null).getSlotNumber()==1);
        ParkingLot small = new ParkingLot(new int[]{2});
        ParkingTicket one=small.enter(new Car("TN06AA1234"),0,DAY,null);
        ParkingTicket two=small.enter(new ElectricBike("TN06EV1234"),0,DAY,null);
        check("Final available bay allocated", two.getSlotNumber()==2);
        rejects("Parking full", ()->small.enter(new Car("TN07AB1234"),0,DAY,null));
        check("Full rejection does not change count", small.getActiveCount()==2);
        check("Truck capped fee", new Truck("TN08TR1234").amountDue(1200)==600);
        check("Polymorphic two wheeler rate", new TwoWheeler("TN08BI1234").amountDue(130)==30);
        check("Overloaded duration", new Car("TN08CA1234").calculate(2,10)==90);
        check("Plate equality across subclasses", new Car("TN09EV1234").equals(new ElectricCar("tn09ev1234")));
        check("Hash code agrees", new Car("TN09EV1234").hashCode()==new ElectricCar("TN09EV1234").hashCode());
        ParkingLot permits=new ParkingLot(new int[]{2});
        MonthlyPass valid=new MonthlyPass("TN10AA1234","STUDENT",DAY);
        ParkingTicket permit=permits.enter(new Car("TN10AA1234"),0,DAY,valid);
        check("Valid student pass free parking", permits.exit("TN10AA1234",1,permit.getSlotNumber(),120,0)==0);
        MonthlyPass expired=new MonthlyPass("TN10BB1234","STAFF",DAY.minusDays(1));
        ParkingTicket expiredTicket=permits.enter(new Car("TN10BB1234"),0,DAY,expired);
        check("Expired pass charged", permits.exit("TN10BB1234",1,expiredTicket.getSlotNumber(),60,0)==30);
        check("Ticket IDs are unique", one.getTicketNumber()!=two.getTicketNumber());
        check("Payable default method", new Car("TN11AA1234").receipt(60).contains("30"));
        check("Static Payable total", Payable.total(new Payable[]{new Car("TN12AA1234"),new TwoWheeler("TN13AA1234")},60)==40);
        check("Chargeable default tax", new ElectricBike("TN14EV1234").chargingWithTax(10)==141);
        check("Immutable ticket fields", permit.getEntryMinute()==0 && permit.getParkingDate().equals(DAY));
        System.out.println("TOTAL PASS: " + passed);
    }
}
