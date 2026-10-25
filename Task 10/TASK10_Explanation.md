# Task 10 Explanation

## Encapsulation and packages

Encapsulation keeps an object's state private and makes every state change pass through a valid operation. The model classes use `private` fields, so callers cannot assign an empty registration number, negative battery capacity, or an occupied slot directly. Public getters expose safe observations, while setters validate and normalize mutable values.

The package split makes responsibilities visible:

- `parking.model` owns vehicles, slots, and tickets.
- `parking.service` owns floors, lots, and pricing.
- `parking.app` coordinates the use case.

## Access modifiers

| Modifier | Example | Purpose |
|---|---|---|
| `private` | `Vehicle.registrationNumber` | Hides mutable representation from every other class. |
| `protected` | `Vehicle.hourlyRate()` | Lets vehicle subclasses provide their rate without making the hook part of the public API. |
| `public` | `ParkingLot.park()` | Exposes operations intended for callers in another package. |
| default/package-private | `ParkingFloor.slotAt()` | Restricts a helper to classes in `parking.service`. |

`ParkingTicket` is immutable: its fields are private and final, and its constructor validates every required value. `Vehicle.equals` and `hashCode` use the registration number as the identity, matching the earlier tasks.

## Validated accessors

Validation rejects null, blank, negative, zero, and unknown values at the boundary. This prevents invalid state from spreading into fee calculations. `Objects.requireNonNull` is used where null is never meaningful. The program deliberately catches one validation exception to demonstrate the rejection without hiding unexpected errors.

## Wrapper classes and autoboxing

`Main` uses `Integer boxedMinutes = 130` and `Double boxedFee = car.calculateFee(boxedMinutes)`. The compiler automatically boxes `int` into `Integer`, unboxes `Integer` when calling the method, and boxes the resulting `double` into `Double`.

Wrappers are useful in collections and APIs that permit null, but they have risks:

1. An `Integer` may be null, causing a `NullPointerException` during unboxing.
2. Repeated boxing can create extra objects and memory pressure in large loops.
3. `==` compares references for wrappers, not numeric values; use `.equals` or primitive comparison.
4. A null wrapper does not represent a valid parking duration, so validation is still required.

## Alternative: public fields

An alternative design would expose fields directly:

```java
public String registrationNumber;
public double hourlyRate;
```

This is shorter but breaks invariants. Any caller could assign a blank plate, negative rate, or inconsistent vehicle type, and the class could not enforce validation or change its internal representation safely. Public fields also increase coupling between every caller and the storage layout. The normal read/write operation is O(1), but the design cost is loss of control, not a performance improvement. In this implementation, registration and owner accessors are O(1), slot lookup is O(s) for `s` slots on a floor, and lot parking is O(f*s) in the worst case for `f` floors.

## Expected output

```text
=== TASK 10: ENCAPSULATION ===
Lot: North Gate
Car slot: 1
Occupied slots: 3
Ticket owner: Asha
Car fee (130 minutes): 90.00
Electric car fee + charging: 330.00
Truck fee (20 hours capped): 600.00
Autoboxing example: Integer -> int, double -> Double = 90.0
Validation rejected invalid battery capacity.
```
