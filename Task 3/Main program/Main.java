import java.util.Arrays;

public class Main {

    static class ParkingFacility {

        // Each row represents a floor.
        // Each element stores a vehicle number or null.
        private final String[][] slots;

        public ParkingFacility(int[] slotsPerFloor) {

            slots = new String[slotsPerFloor.length][];

            for (int f = 0; f < slotsPerFloor.length; f++) {
                slots[f] = new String[slotsPerFloor[f]];
            }
        }

        // Allocate the first available parking slot.
        public int[] allocate(String vehicleNo) {

            for (int floor = 0; floor < slots.length; floor++) {

                for (int slot = 0; slot < slots[floor].length; slot++) {

                    if (slots[floor][slot] == null) {

                        slots[floor][slot] = vehicleNo;

                        return new int[]{floor, slot};
                    }
                }
            }

            // No free slot is available.
            return null;
        }

        // Release an occupied parking slot.
        public boolean release(int floor, int slot) {

            // Validate the floor index.
            if (floor < 0 || floor >= slots.length) {
                return false;
            }

            // Validate the slot index.
            if (slot < 0 || slot >= slots[floor].length) {
                return false;
            }

            // Reject a slot that is already empty.
            if (slots[floor][slot] == null) {
                return false;
            }

            // Make the slot available again.
            slots[floor][slot] = null;

            return true;
        }

        public static void main(String[] args) {

            // Floor 0 has 2 slots; Floor 1 has 1 slot.
            ParkingFacility p =
                    new ParkingFacility(new int[]{2, 1});

            System.out.println(
                    Arrays.toString(p.allocate("TN01AB1111")));

            System.out.println(
                    Arrays.toString(p.allocate("TN02CD2222")));

            System.out.println(
                    Arrays.toString(p.allocate("TN03EF3333")));

            System.out.println(
                    Arrays.toString(p.allocate("TN04GH4444")));

            System.out.println(
                    p.release(0, 1) + " " + p.release(0, 1));

            System.out.println(
                    Arrays.toString(p.allocate("TN04GH4444")));
        }
    }
}