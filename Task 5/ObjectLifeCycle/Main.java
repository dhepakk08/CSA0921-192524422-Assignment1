class ParkingTicket {
    private static int nextTicketNumber = 2001;
    private final String ticketNumber;
    private final String vehicleNumber;

    ParkingTicket(String vehicleNumber) {
        ticketNumber = "T" + nextTicketNumber++;
        this.vehicleNumber = vehicleNumber;
    }
    String getTicketNumber() { return ticketNumber; }
    void displayDetails() {
        System.out.println("Processing Ticket: " + ticketNumber);
        System.out.println("Vehicle Number: " + vehicleNumber);
    }
}

public class Main {
    static void processTicket(ParkingTicket ticket) {
        ticket.displayDetails();
    }
    static ParkingTicket createTicket(String vehicleNumber) {
        return new ParkingTicket(vehicleNumber);
    }
    public static void main(String[] args) {
        System.out.println("========== OBJECT PASSING ==========");
        ParkingTicket ticket = createTicket("TN01AB1234");
        processTicket(ticket);

        System.out.println("\\n========== OBJECT LIFE CYCLE ==========");
        System.out.println("Ticket retained for records: " + ticket.getTicketNumber());
        System.out.println("A vehicle exit does not automatically destroy its ticket.");
        System.out.println("A ticket is eligible for garbage collection only when");
        System.out.println("no reachable references to that object remain.");
        ticket = null;
        System.out.println("Local ticket reference cleared; collection timing is JVM-controlled.");
    }
}
