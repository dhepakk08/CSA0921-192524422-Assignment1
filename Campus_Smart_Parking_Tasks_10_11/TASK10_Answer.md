# TASK 10 — Enforce Encapsulation through Packages and Access Modifiers (3 marks)

## Package structure

```text
src/parking/model/     Vehicle, Car, TwoWheeler, ElectricCar, ElectricBike, Truck,
                       ParkingSlot, ParkingFloor, ParkingTicket, MonthlyPass,
                       Payable, Chargeable
src/parking/service/   ParkingLot, PricingPolicy
src/parking/app/       Main
src/parking/test/      ParkingTests
```

`model` holds domain entities and contracts; `service` enforces lot-level operations and billing; `app` is the user-facing entry point; `test` checks invariants and failure paths.

## Access modifiers and justification

| Member | Access | Reason |
|---|---|---|
| `Vehicle.registrationNumber`; `ParkingSlot.occupied`; `ParkingLot.activeTickets`, `dailyCollection`, `activeCount` | `private` | Prevent outside code bypassing identity, capacity, and billing checks |
| `Vehicle.rate()` | `protected abstract` | Implementation detail required from child classes; not a general public setter |
| `Vehicle.getRegistrationNumber()`, `getType()`, `amountDue()`, `ParkingLot.enter/exit/isOccupied` | `public` | Safe, validated system API |
| `ParkingTicket` fields | `private final` | Ticket identity/location/entry cannot change once created |
| `ParkingLot.CAMPUS_CAPACITIES` / `campusCapacities()` | `private static final int[]` / `public` defensive-copy accessor | Prevents callers changing default floor capacities via a shared array. |
| package-private (default) | Not required by the current cross-package API | Could be used for package-internal helpers; avoid gratuitously exposing a second mutable pathway |

## Validation and state protection

- Constructors reject missing/invalid vehicle registration, non-positive floor and slot numbers, negative entry times and null dates.
- `ParkingFloor.getSlot()` rejects slot indices outside `1..capacity`; `ParkingLot.getSlot()` rejects invalid floors. Public access is only via checked APIs.
- `ParkingLot.enter()` first rejects duplicate, normalized registration values; it selects a suitable free slot; it issues an immutable ticket and updates occupancy only once admission succeeds.
- `ParkingLot.exit()` first validates floor/slot, matching ticket, exit time, energy input, and fee arithmetic; then releases the slot, removes active ticket and updates total. Rejected invalid exits do not alter occupancy or collections.
- `Vehicle` registration is immutable rather than offering a mutable setter: validation occurs once in its constructor, preserving HashMap-key and equality safety. This improves over an unrestricted setter and matches the fixed identity design in Tasks 7–9.
- `ParkingSlot.occupy()`/`release()` are public to support the service across packages, but guarded; for stricter encapsulation one could move them to a same-package manager or limit mutation behind a package-private service bridge.
- Setter example (if mutable duration were needed): `setDurationMinutes(long m) { if (m < 0) throw new IllegalArgumentException(); durationMinutes = m; }`. This project instead computes duration from immutable ticket time and validated exit time, avoiding inconsistent duration setters.

## Wrappers, autoboxing and comparisons

The required active-ticket array uses primitive `int`/`long` indices, counts and rupees. For a `HashMap<String, ParkingTicket>` alternative, registration is a `String` immutable key, and for `HashMap<Integer, ParkingTicket>` slot IDs auto-box `int` to `Integer`. `Integer x = 200; Integer y = 200; x == y` may be false because `==` compares references; use `x.equals(y)` or unbox to `int`. Wrapper values may be `null`, so unboxing may throw `NullPointerException`, and boxed objects have allocation/memory overhead. The implementation uses `.equals()` for string registration rather than `==`.

## Alternative: public fields validated by callers

With `public long durationMinutes` or `public boolean occupied`, every caller must manually check the rules before each assignment. A forgotten check, direct access from another package, or a new UI import path can introduce negative durations, double-booked slots, or impossible totals. If `u` updates occur, a constant-time range/null check costs **O(1) per update**, **O(u) total**, whether centrally or at the call site. If there are `k` independent call sites, duplicated validation spans **O(k) code locations** (and roughly O(k) maintenance edits when a rule changes); centralized constructors/validated setters maintain **one** rule. Encapsulation improves correctness and maintenance, not the Big-O of the check.

## Evidence

See `Vehicle.java`, `ParkingFloor.java`, `ParkingTicket.java`, `ParkingLot.java`, and the invalid input/unchanged-state assertions in `ParkingTests.java`. The `campusCapacities()` accessor returns a defensive copy of the static configuration. The constructor also creates floors immediately, so changes to a caller-provided input array after construction cannot alter the already-created lot.
