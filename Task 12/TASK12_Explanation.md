# Task 12 Explanation

## Assumptions

1. An E-Bike costs a flat Rs. 25 per parking session, including zero-minute and long parking sessions. Negative duration is still invalid.
2. A OneDayPass is valid only when the parking date equals its configured `LocalDate`. The pass identifies a visitor but does not silently bypass vehicle capacity or ticket tracking.
3. DeliveryVan uses the car base rate of Rs. 30 per started hour. Each started hour whose start time is from 09:00 inclusive through 16:59:59 is charged at Rs. 60. The implementation therefore handles a session crossing 09:00 or 17:00 without guessing a single rate for the whole session.
4. Existing rules remain: motorcycle Rs. 10/hour, car and electric car Rs. 30/hour, truck Rs. 50/hour capped at 12 hours, and electric-car charging Rs. 12/kWh.

## Impact analysis (2 marks)

| Existing area | Impact | Result |
|---|---|---|
| `Vehicle` hierarchy | New vehicle categories need identity, validation, and fee behavior | `EBike` and `DeliveryVan` extend `Vehicle`; existing subclasses are unchanged |
| Fee calculation | DeliveryVan depends on entry time and peak hours | `PricingPolicy.calculateFee` is time-aware; ordinary vehicles still delegate to their existing fee method |
| Pricing | New categories must be recognized without fragile conditionals in the lot | `PricingPolicy` adds E-Bike and DeliveryVan rates; `PricingRule` documents the calculation seam |
| Parking capacity | Five floors may have different capacities | `ParkingLot` now accepts `int[]`, creating a jagged `ParkingFloor[]` |
| Visitor access | Visitors need a date-bound credential | `Pass` and `OneDayPass` are new, validated value objects |
| Regression risk | Existing entry, exit, ticket, and rates must remain stable | Automated regression test covers car and motorcycle behavior |

Existing classes modified: **3** (`ParkingFloor`, `ParkingLot`, and `PricingPolicy`). New production classes/interfaces: **6** (`EBike`, `DeliveryVan`, `Visitor`, `Pass`, `OneDayPass`, and `PricingRule`). New application/test entry classes: **2** (`parking.app.Main` and `parking.test.Task12Test`).

## Evaluation of the original design (2 marks)

The Task 11 design already had strong encapsulation, a `Vehicle` inheritance hierarchy, polymorphic fee calculation, validated tickets, and a `HashMap` for expected O(1) active-ticket lookup. It also separated model objects from service coordination.

Its limitation was that pricing was effectively a vehicle-only, fixed-rate decision. A delivery van whose rate changes by time would not fit cleanly in `Vehicle.calculateFee(int)` because that method has no entry timestamp. Its uniform `floorCount × slotsPerFloor` constructor also could not represent real floors with different capacities. Visitor passes were not represented at all.

## Adaptation and redesign (2 marks)

The lot keeps its existing entry/exit workflow but delegates final fee calculation to `PricingPolicy.calculateFee(vehicle, entry, exit)`. That is the smallest change that supplies time context only where required. Ordinary vehicle fees still use their existing polymorphic methods, while DeliveryVan receives the peak-hour rule.

The new `int[] slotsPerFloor` constructor creates one `ParkingFloor` per array element. For example, `{2, 3, 1, 4, 2}` produces five floors with five different capacities: this is a jagged representation rather than a rectangular two-dimensional array.

`EBike` is a normal `Vehicle` subclass, so it participates in slot allocation and ticket identity while overriding the fee to a flat amount. `OneDayPass` implements the small `Pass` capability interface and composes a validated `Visitor`; it does not pollute the vehicle hierarchy with visitor-only state.

## Evidence-based defence (2 marks)

The dependency-free test runner completed **10 tests with 10 passes and 0 failures**:

| Test | Expected | Actual |
|---|---|---|
| E-Bike flat fee | Rs. 25.00 | PASS |
| DeliveryVan off-peak | Rs. 30.00/hour | PASS |
| DeliveryVan peak | Rs. 60.00/hour | PASS |
| DeliveryVan crossing 09:00 | Rs. 90.00 for 08:00–10:00 | PASS |
| OneDayPass same date | Valid | PASS |
| OneDayPass next date | Invalid | PASS |
| Five-floor jagged capacities | First 1, last 5 | PASS |
| Existing car/motorcycle rates | Rs. 30/Rs. 10 | PASS |
| Invalid capacity/pass/category | Rejected | PASS |

The application also produced an E-Bike fee of Rs. 25, a crossing peak DeliveryVan fee of Rs. 90, and a valid visitor pass. This evidence supports keeping the inheritance model while moving contextual pricing into the policy service.

## Complexity and limitations

The jagged capacity storage is O(f + total slots), where `f` is the number of floors. First-available slot allocation remains O(total slots) in the worst case. Active-ticket registration lookup, insertion, and removal use `HashMap` with expected O(1) time and O(n) storage for `n` active tickets. DeliveryVan fee calculation is O(h), where `h` is the number of started hours in the session.

The main limitation is that the in-memory pass and ticket data disappear when the JVM stops. A production version would persist passes, tickets, and collections and would use database constraints for duplicate registrations. Another policy limitation is that billing by started-hour timestamps is an explicit business assumption; a different institution could require minute-level peak prorating.
