# TASK 11 — Implement, Test and Defend the Design (5 marks)

## Integrated implementation

`parking.model` separates abstract `Vehicle`, concrete `TwoWheeler`/`Car`/`ElectricCar`/`ElectricBike`/`Truck`, `Payable` and `Chargeable` from floor, slot, pass and ticket entities. `parking.service.ParkingLot` uses nine configured `ParkingFloor` objects (20, 50, 50, 50, 100, 100, 100, 100, 100) giving **670 slots**. Active tickets are held in a fixed `ParkingTicket[]` as requested. Each floor reserves its final slot for EVs. Valid student/staff monthly passes waive parking fees; electricity remains chargeable.

## Consistent assumptions

Prior README exercises use conflicting demonstration rates. This integration chooses Tasks 8–9 rates: TwoWheeler/ElectricBike Rs 10/h, Car/ElectricCar Rs 30/h, electricity Rs 12/kWh; adds Task 7's Truck Rs 50/h with a Rs 600 per-stay cap. Fee slab is ceiling of positive minutes/60; zero minutes is free **for parking**. No GST at checkout. Time is controlled elapsed minutes for reproducible tests, not wall-clock time. Pass eligibility is checked against supplied parking date. Single threaded. For real deployment, an EV-only bay should be selected by physical charger capability and car/bike size; here these are simplified.

## Expected / observed test table

| Case | Expected | Observed by automated test |
|---|---|---|
| Campus configuration | 9 floors, 670 slots | Verified equal |
| Two-wheeler, car, EV enter floor 1 | First 2 normal bays, EV bay 3 | Verified |
| Last free bay / full lot (2-bay floor) | Bay 2 assigned; third denied | Verified |
| Unknown type `PLANE` | Rejected | Verified |
| Duplicate case-insensitive registration | Rejected | Verified |
| Floor 0 / slot 9 / mismatched slot | Rejected | Verified, occupancy unchanged |
| Exit before entry / negative duration | Rejected | Verified |
| 60 minutes two-wheeler | Rs 10 | Verified |
| 61 minutes car | Rs 60 | Verified |
| Zero-minute EV, 2 kWh | Rs 24 (0 parking + 24 charging) | Verified |
| Sum of 3 exits | Rs 94 | Verified |
| Truck 20 h | Rs 600 cap | Verified |
| Student pass valid / staff pass expired | Rs 0 / Rs 30 | Verified |
| Freed slot reused | Slot 1 | Verified |
| Non-electric car charged kWh | Rejected | Verified |

Run `sh run_tests.sh` to check the source. `ParkingTests.java` also covers equality/hash contract, overloading, `Payable` default/static methods, `Chargeable` default tax calculation, ticket identities and intact state after failed operations. The generated output is intentionally **not bundled**.

## Manual billing scenario

On one day:

1. TwoWheeler enters at 0, exits at 60: `ceil(60/60) × 10 = Rs 10`.
2. Car enters at 10, exits at 71: `ceil(61/60) × 30 = Rs 60`.
3. ElectricCar enters/exits at 20 with 2 kWh: `0 × 30 + 2 × 12 = Rs 24`.

**Hand total = 10 + 60 + 24 = Rs 94**. The test directly asserts `getDailyCollection() == 94` after the three exits, so this is a calculated and checked figure, not just a startup message.

## Evaluation of OOP principles

**Encapsulation:** private backing fields, immutable plate and ticket, guarded slot mutation and validated operations. **Inheritance:** Vehicle → Car → ElectricCar, Vehicle → TwoWheeler → ElectricBike, Vehicle → Truck. **Polymorphism:** parking invokes `Vehicle.amountDue()` dynamically; `Truck` overrides for the capped algorithm, subclass `rate()` determines other fees. **Abstraction:** abstract `Vehicle.rate()` captures variation; `Payable` and `Chargeable` describe independent capabilities; non-electric vehicles cannot bill charging energy.

**Limitation:** `ParkingLot` is an in-memory, single-day, single-threaded assignment design. The O(670) registration scan, no persistent audit trail, no separate ticket closure state, simple EV bay sizing and fixed policy are unsuited to production without additional engineering. The global static ticket number also needs durable ID allocation across restarts.

## Ticket lookup alternatives and complexity

Let `n` = number of active tickets, `C` = total configured slots (670 here), and registration string length be bounded (up to 15). Current exit: scan the full capacity array for matching plate, validate, compute fee, release corresponding floor/slot, clear ticket.

| Data structure | Exit lookup | Removal | Ticket-storage space | Tradeoff |
|---|---|---|---|---|
| `ParkingTicket[]` current | O(C), worst and average when array sparsely populated; O(C) = O(n) if C scales with n | O(1) after locating position | O(C) references | Lowest overhead, simple fixed capacity |
| `ArrayList<ParkingTicket>` | O(n) scan by normalized plate | O(n) shifts for middle deletion | O(n) reference backing array | Dynamic size but slow random exits |
| `HashMap<String,ParkingTicket>` | expected O(1), worst O(n) for hash collisions/degradation; plus bounded key length | expected O(1) | O(n) nodes/buckets plus keys | Best frequent exit lookup, greater overhead |

In both alternatives `ParkingFloor` / `ParkingSlot` tracking still needs O(C) space. An ArrayList exits with `for (int i=0; i<list.size();i++)` then `list.remove(i)`. A HashMap exits with `map.get(normalizedRegistration)`, validates slot/time, then `map.remove(key)`. With a map, immutable normalized registration prevents lookup-key drift. Full exit also takes constant-time slot validation and fee computation (apart from scanning), assuming a slot locator is known.

**Measurement evidence (executed in this environment):** `parking.test.LookupBenchmark` uses 30,000 lookups for each size and one short warm-up pass, Java 21 OpenJDK on the test container. Recorded approximate nanoseconds per successful lookup:

| Active tickets | Ticket array | ArrayList | HashMap |
|---:|---:|---:|---:|
| 100 | 384.5 ns | 674.6 ns | 16.1 ns |
| 1,000 | 3,014.5 ns | 2,506.3 ns | 8.5 ns |
| 5,000 | 12,022.2 ns | 14,278.1 ns | 14.1 ns |

Results are **illustrative, one run only**, not statistically robust: timings can change with JVM optimization, CPU scheduling, load and machine. Benchmark 5,000 is a **data-structure microbenchmark**, not 5,000 parked vehicles inside the 670-slot campus. To reproduce: `sh build.sh && java -cp build parking.test.LookupBenchmark`. Run multiple warmed trials and use a harness such as JMH for publishable numbers. Measured trends support the expected O(n) scan versus expected O(1) map lookup.

## Recommendation justified by tests

Keep constructors, private state, immutable identity, abstract `Vehicle` and charge interfaces. The included tests verify boundary fees, correct type-dependent dispatch, prevention of duplicates, validation at the service boundary and no occupancy corruption after a failed exit. For frequent real-world retrieval by plate, replace the primary active `ParkingTicket[]` with a `HashMap<String,ParkingTicket>` while retaining structured floor/slot occupancy; add a persistent payment ledger and transactional updates for production. This follows the tests and O(n) vs expected O(1) lookup evidence, rather than the claim that the program merely runs.
