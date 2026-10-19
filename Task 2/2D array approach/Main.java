import java.util.Scanner;

public class Main {

    static final int TOTAL_FLOORS = 9;

    static final int[] FLOOR_CAPACITY = {
        20, 50, 50, 50, 100, 100, 100, 100, 100
    };

    static String[][] slots = new String[TOTAL_FLOORS][];

    static void initializeSlots() {
        for (int i = 0; i < TOTAL_FLOORS; i++) {
            slots[i] = new String[FLOOR_CAPACITY[i]];
        }
    }

    static int[] findVehicle(String registration, int[] metrics) {
        for (int floor = 0; floor < slots.length; floor++) {
            for (int slot = 0; slot < slots[floor].length; slot++) {
                metrics[0]++;

                if (slots[floor][slot] != null) {
                    metrics[1]++;

                    if (slots[floor][slot].equals(registration)) {
                        return new int[] { floor, slot };
                    }
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        initializeSlots();

        System.out.println("=== SMART PARKING: 2D ARRAY ===");
        System.out.println("Floors: " + TOTAL_FLOORS);
        System.out.println("Total slots: 670");

        // Same sample data as Program 2
        slots[0][0] = "TN01AB1234";
        slots[1][5] = "TN02CD5678";
        slots[2][0] = "TN03EF9012";
        slots[5][50] = "TN04GH3456";
        slots[8][99] = "TN05JK7890";

        System.out.print("Enter registration number to search: ");
        String registration = sc.nextLine().trim().toUpperCase();

        int[] metrics = new int[2];

        long start = System.nanoTime();
        int[] position = findVehicle(registration, metrics);
        long end = System.nanoTime();

        if (position != null) {
            int floor = position[0];
            int slot = position[1];

            System.out.println("\nVehicle found.");
            System.out.println("Registration: " + slots[floor][slot]);
            System.out.println("Floor: " + (floor + 1));
            System.out.println("Slot: " + (slot + 1));
            System.out.println("Slot availability: Occupied");
        } else {
            System.out.println("\nVehicle not found.");
        }

        System.out.println("\n--- Search Metrics ---");
        System.out.println("Array representation: 2D Jagged Array");
        System.out.println("Records checked: " + metrics[0]);
        System.out.println("Comparisons: " + metrics[1]);
        System.out.println("Search time: " + (end - start) + " ns");
        System.out.println("Time complexity: O(n)");
        System.out.println("Space complexity: O(n)");

        sc.close();
    }
}