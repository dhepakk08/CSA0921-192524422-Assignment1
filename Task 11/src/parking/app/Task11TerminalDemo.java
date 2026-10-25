package parking.app;

import parking.test.LookupBenchmark;
import parking.test.ParkingSystemTest;

public final class Task11TerminalDemo {
    private Task11TerminalDemo() {
    }

    public static void main(String[] args) {
        printPrompt("1. Vehicle-entry and exit scenario");
        Main.main(new String[0]);

        printPrompt("2. Automated test summary");
        ParkingSystemTest.main(new String[0]);

        printPrompt("3. Array versus ArrayList versus HashMap lookup benchmark");
        LookupBenchmark.main(new String[0]);
    }

    private static void printPrompt(String prompt) {
        System.out.println();
        System.out.println(">>> PROMPT: " + prompt);
        System.out.println();
    }
}
