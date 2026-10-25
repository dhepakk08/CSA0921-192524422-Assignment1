# Task 11 Explanation and Design Defence

## Automated test table

The test program prints the actual result at runtime. The following table records the expected result and the verified actual result from the completed run:

| Test | Expected | Actual |
|---|---|---|
| Normal car entry and 130-minute exit | Rs. 90.00 | PASS, Rs. 90.00 |
| Last available slot | Two active tickets in two slots | PASS, 2 active tickets |
| Zero-minute motorcycle parking | Rs. 10.00 minimum fee | PASS, Rs. 10.00 |
| Exact 60-minute car boundary | Rs. 30.00 | PASS, Rs. 30.00 |
| 61-minute car boundary | Rs. 60.00 | PASS, Rs. 60.00 |
| Electric charging, 20 kWh | Rs. 240.00 | PASS, Rs. 240.00 |
| Truck, 20 hours capped at 12 | Rs. 600.00 | PASS, Rs. 600.00 |
| Unknown vehicle type `BUS` | Rejected | PASS, exception rejected |
| Duplicate registration | Rejected | PASS, exception rejected |
| Invalid slot number `0` | Rejected | PASS, exception rejected |
| Daily collection scenario | Rs. 100.00 | PASS, Rs. 100.00 |
| Manual parking and charging total | Rs. 94.00 | PASS, Rs. 94.00 |

## Manual daily collection

Scenario:

1. Motorcycle enters for exactly 60 minutes: `1 hour × Rs. 10 = Rs. 10`.
2. Car enters for 130 minutes: `ceil(130 / 60) = 3 hours`; `3 × Rs. 30 = Rs. 90`.
3. Daily collection: `Rs. 10 + Rs. 90 = Rs. 100`.

The automated test performs the same scenario and checks `getDailyCollection()`.

### Manual Rs. 94 collection

The runnable application also demonstrates the requested Rs. 94 total:

1. Motorcycle parking for 60 minutes: `Rs. 10`.
2. Car parking for 61 minutes: `Rs. 60`.
3. Electric-car charging for 2 kWh: `2 × Rs. 12 = Rs. 24`.
4. Manual total: `Rs. 10 + Rs. 60 + Rs. 24 = Rs. 94`.

Parking fees remain separate from charging fees in the domain model; the
manual total intentionally combines both displayed charges.

## OOP evaluation

- **Encapsulation:** ticket state, vehicle state, slot occupancy, and daily collection are private. Public operations validate inputs and prevent invalid transitions.
- **Inheritance:** `Car`, `ElectricCar`, `Motorcycle`, and `Truck` specialize `Vehicle`.
- **Polymorphism:** `Vehicle.calculateFee` invokes each concrete vehicle's rate behavior; the same entry/exit workflow handles all vehicle types.
- **Abstraction:** callers use `enterVehicle` and `exitVehicle` without knowing how floors, slots, tickets, and fee calculations are coordinated.

## Active-ticket storage alternatives

### Array

An array of capacity `n` uses O(n) storage. Finding a registration during exit is O(n), insertion can scan for a free position in O(n), and deletion is O(n) unless a separate index is maintained. It is simple and efficient for a fixed small capacity, but resizing is inconvenient.

### `ArrayList<ParkingTicket>`

An `ArrayList` also uses O(n) storage. Appending is amortized O(1), but searching by registration for exit is O(n), and removal is O(n) because later elements shift. It is flexible when active ticket count changes.

### `HashMap<String, ParkingTicket>`

The implementation uses a `HashMap` keyed by normalized registration. Storage is O(n), and expected insertion, lookup, and removal are O(1). Worst-case hash behavior can degrade, and the map does not itself preserve ordering. For registration-based exit processing, it is the strongest fit.

The map is not a replacement for the physical slot scan: slot allocation still examines floors and slots, so allocation is O(f × s) in the worst case for `f` floors and `s` slots per floor.

`parking.test.LookupBenchmark` runs 1,000 lookups over 100,000 registrations
using each structure and prints machine-dependent timings. The expected result
is that the array and `ArrayList` use linear scans, while the `HashMap` uses
expected constant-time key lookup.

## Design limitation

The in-memory system loses active tickets and daily collection when the JVM stops. A production system would persist tickets and transactions in a database and use a transaction/unique constraint for duplicate registrations across multiple application instances. The current design is intentionally dependency-free for assignment testing.

## Evidence-based recommendation

Use `HashMap<String, ParkingTicket>` for active ticket lookup because exit is keyed by registration and expected lookup/removal is O(1). Keep slot allocation in the lot/floor model, where the physical search remains O(f × s). Add persistence and database constraints when the application becomes multi-process or production-facing.
