package parking.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class LookupBenchmark {
    private static final int ENTRY_COUNT = 100_000;
    private static final int LOOKUP_COUNT = 1_000;
    private static final int WARMUP_ROUNDS = 5;

    private LookupBenchmark() {
    }

    public static void main(String[] args) {
        String[] registrations = new String[ENTRY_COUNT];
        ArrayList<String> arrayList = new ArrayList<>(ENTRY_COUNT);
        Map<String, Integer> hashMap = new HashMap<>(ENTRY_COUNT);

        for (int i = 0; i < ENTRY_COUNT; i++) {
            String registration = "TN" + String.format("%08d", i);
            registrations[i] = registration;
            arrayList.add(registration);
            hashMap.put(registration, i);
        }

        for (int round = 0; round < WARMUP_ROUNDS; round++) {
            lookupArray(registrations);
            lookupArrayList(arrayList);
            lookupHashMap(hashMap);
        }

        long arrayNanos = measure(() -> lookupArray(registrations));
        long arrayListNanos = measure(() -> lookupArrayList(arrayList));
        long hashMapNanos = measure(() -> lookupHashMap(hashMap));

        System.out.println("=== ACTIVE-TICKET LOOKUP BENCHMARK ===");
        System.out.println("Entries: " + ENTRY_COUNT + ", lookups: " + LOOKUP_COUNT);
        printResult("Array (linear scan)", arrayNanos);
        printResult("ArrayList (linear scan)", arrayListNanos);
        printResult("HashMap (key lookup)", hashMapNanos);
        System.out.println("Expected result: HashMap performs expected O(1) lookup; "
                + "Array and ArrayList perform O(n) lookup.");
    }

    private static long measure(Runnable lookup) {
        long start = System.nanoTime();
        lookup.run();
        return System.nanoTime() - start;
    }

    private static void printResult(String name, long elapsedNanos) {
        System.out.printf("%s: %,d ns total, %.2f ns/lookup%n",
                name, elapsedNanos, (double) elapsedNanos / LOOKUP_COUNT);
    }

    private static int lookupArray(String[] registrations) {
        int found = 0;
        for (int i = 0; i < LOOKUP_COUNT; i++) {
            String target = registrations[ENTRY_COUNT - 1 - i];
            for (String registration : registrations) {
                if (registration.equals(target)) {
                    found++;
                    break;
                }
            }
        }
        return found;
    }

    private static int lookupArrayList(ArrayList<String> registrations) {
        int found = 0;
        for (int i = 0; i < LOOKUP_COUNT; i++) {
            if (registrations.contains(registrations.get(ENTRY_COUNT - 1 - i))) {
                found++;
            }
        }
        return found;
    }

    private static int lookupHashMap(Map<String, Integer> registrations) {
        int found = 0;
        for (int i = 0; i < LOOKUP_COUNT; i++) {
            if (registrations.containsKey("TN" + String.format("%08d", ENTRY_COUNT - 1 - i))) {
                found++;
            }
        }
        return found;
    }
}
