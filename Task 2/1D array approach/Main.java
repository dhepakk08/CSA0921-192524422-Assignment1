import java.util.Scanner;

public class Main {

    static final int TOTAL_FLOORS = 9;
    static final int TOTAL_SLOTS = 670;

    static final int[] FLOOR_CAPACITY = {
        20, 50, 50, 50, 100, 100, 100, 100, 100
    };

    static String[] slots = new String[TOTAL_SLOTS];

    static int getFloor(int index) {
        int count = 0;

        for (int floor = 0; floor < TOTAL_FLOORS; floor++) {
            count += FLOOR_CAPACITY[floor];

            if (index < count) {
                return floor + 1;
            }
        }

        return -1;
    }

    static int getSlotNumber(int index) {
        int previousSlots = 0;

        for (int floor = 0; floor < TOTAL_FLOORS; floor++) {
            if (index < previousSlots + FLOOR_CAPACITY[floor]) {
                return index - previousSlots + 1;
            }

            previousSlots += FLOOR_CAPACITY[floor];
        }

        return -1;
    }

    static int findVehicle(String registration, int[] metrics) {
        for (int i = 0; i < slots.length; i++) {
            metrics[0]++;

            if (slots[i] != null) {
                metrics[1]++;

                if (slots[i].equals(registration)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== SMART PARKING: 1D ARRAY ===");
        System.out.println("Floors: " + TOTAL_FLOORS);
        System.out.println("Total slots: " + TOTAL_SLOTS);

        // Sample occupied slots for comparison
        slots[0] = "TN01AB1234";
        slots[25] = "TN02CD5678";
        slots[100] = "TN03EF9012";
        slots[300] = "TN04GH3456";
        slots[669] = "TN05JK7890";

        System.out.print("Enter registration number to search: ");
        String registration = sc.nextLine().trim().toUpperCase();

        int[] metrics = new int[2];

        long start = System.nanoTime();
        int index = findVehicle(registration, metrics);
        long end = System.nanoTime();

        if (index != -1) {
            System.out.println("\nVehicle found.");
            System.out.println("Registration: " + slots[index]);
            System.out.println("Floor: " + getFloor(index));
            System.out.println("Slot: " + getSlotNumber(index));
            System.out.println("Slot availability: Occupied");
        } else {
            System.out.println("\nVehicle not found.");
        }

        System.out.println("\n--- Search Metrics ---");
        System.out.println("Array representation: 1D Array");
        System.out.println("Records checked: " + metrics[0]);
        System.out.println("Comparisons: " + metrics[1]);
        System.out.println("Search time: " + (end - start) + " ns");
        System.out.println("Time complexity: O(n)");
        System.out.println("Space complexity: O(n)");

        sc.close();
    }
}