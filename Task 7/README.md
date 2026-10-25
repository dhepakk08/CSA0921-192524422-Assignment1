# Task 7 – Override and Specialise Behaviour (Smart Parking Lot)

**Bloom's level:** K4 – Analyse  |  **Language:** Java 17+ (tested on 21)

## 1. Files

| File | Role |
|---|---|
| `Vehicle.java` | Abstract superclass; abstract `calculateFee`, final fields/methods, `toString`/`equals`/`hashCode` |
| `Car.java`, `Motorcycle.java`, `Truck.java` | Override `calculateFee()` (and `category()`) |
| `ElectricCar.java` | Overrides `calculateFee()` using `super.calculateFee()` and `toString()` |
| `ParkingTicket.java` | A **final class** (immutable receipt) |
| `SwitchVehicle.java` | **Alternative design**: `switch` on a type field |
| `Task7Demo.java` | Demonstrates everything + a micro-benchmark |

**Run:**
```bash
javac *.java
java Task7Demo
```

## 2. Behaviour that must differ → `calculateFee()`

Every vehicle has a fee, but the *rule* is different, so it is declared `public abstract double calculateFee(int hours)` in `Vehicle` and **every subclass must supply its own version**.

| Class | Overriding rule | Why a different version is needed |
|---|---|---|
| `Car` | `20 × hours` | Standard rate |
| `Motorcycle` | `10 × hours` | Smaller bay, lower rate |
| `Truck` | `min(600, 50 × hours)` | Different **algorithm**: daily cap (not just a different rate) |
| `ElectricCar` | `Car fee × 0.75 + kWh × 8` | Calls `super.calculateFee()` to reuse Car's rule, then applies EV discount and adds charging cost |

Because the call is resolved at run time (dynamic dispatch), `ParkingTicket` and any `List<Vehicle>` loop simply call `v.calculateFee(h)` and the correct version runs. No `if/switch` on type is needed.

Output (10 hours):
```
Car[reg=TN01AB1234, owner=Asha]                    fee =   200.00
Motorcycle[reg=TN22CD2222, owner=Kiran]            fee =   100.00
Truck[reg=TN33EF3333, owner=Meena]                 fee =   500.00
ElectricCar[reg=TN01EV9999, owner=Ravi]{kWh=20.0}  fee =   310.00
Truck, 20 hours (capped)  : 600.0
```

## 3. `@Override` and the rules of overriding

`@Override` makes the compiler verify that the method really overrides something. A typo such as `calculatefee(int)` or `calculateFee(long)` would silently create a new *overload* without it; with it, compilation fails.

| Rule | Requirement | Example in this project |
|---|---|---|
| **Signature** | Same name and same parameter types | `calculateFee(int)` in every subclass |
| **Return type** | Same, or a **covariant** subtype for reference types (primitives must match exactly) | `Vehicle.copyFor()` returns `Vehicle`; `Car.copyFor()` returns `Car` |
| **Access** | Same or **wider**, never narrower | `Vehicle.category()` is `protected`; `Car` widens to `public`, `Motorcycle` keeps `protected` |
| **Exceptions** | May not throw broader checked exceptions | none thrown |
| **Not overridable** | `final`, `static`, `private` methods | `getRegNo()` is `final`; `static`/`private` are *hidden*, not overridden |

Verified compile errors (see section 8) show these rules enforced by the compiler.

## 4. Where `final` is used and why

| Kind | Where | Reason |
|---|---|---|
| **final variable** | `regNo`, `ownerName` (instance fields) | Identity must not change after construction; also makes `hashCode` stable so objects in a `HashSet` are never "lost" |
| **final variable (constant)** | `GST_RATE`, `Car.RATE`, `Truck.OVERNIGHT_CAP` | Named constants; no magic numbers; cannot be reassigned |
| **final method** | `getRegNo()`, `getOwnerName()`, `billableHours()`, `equals()`, `hashCode()` | A shared rule (minimum 1 hour, equality by reg. number) must be identical for every subclass; stops a subclass breaking the `equals`/`hashCode` contract |
| **final class** | `ParkingTicket` | It is an immutable receipt; a subclass could add mutable state or alter `fee`. (`String` is the standard-library example.) |

Note that `calculateFee` is deliberately **not** final, since it exists to be overridden. `Car` is not final because `ElectricCar` extends it.

## 5. `toString()`, `equals()` and `hashCode()`

```java
@Override public String toString() { return category() + "[reg=" + regNo + ", owner=" + ownerName + "]"; }

@Override public final boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Vehicle)) return false;           // false for null and for non-vehicles
    return regNo.equals(((Vehicle) o).regNo);
}
@Override public final int hashCode() { return regNo.hashCode(); }
```

* **`toString()`** gives a readable description; `category()` is overridden in each subclass, so the output names the right type. `ElectricCar` extends it using `super.toString()`.
* **`equals()`**: two vehicles are equal when their registration number is equal, whatever the owner or class. The constructor normalises the number (upper-case, spaces removed) so `"tn01 ab 1234"` equals `"TN01AB1234"`. `instanceof Vehicle` is used (not `getClass()`) so one registration number is one vehicle across all subclasses; it also keeps `equals` symmetric because it is `final`.
* **`hashCode()`** uses exactly the field `equals` uses. **Contract:** if `a.equals(b)` then `a.hashCode() == b.hashCode()`. Breaking this makes `HashSet`/`HashMap` behave wrongly.
* `equals` also satisfies: reflexive, symmetric, transitive, consistent, and `equals(null)` is false.

Output:
```
car.equals(a)       = true     (same reg, different case/spaces/owner)
car.equals(b)       = true     (same reg, different subclass)
car.equals(bike)    = false
car.equals(null)    = false
hash equal          = true
HashSet size after adding car, a, b, bike = 2  (expected 2)
Lookup with different object, same reg    = Slot A1
```

## 6. Alternative concept – one `calculateFee()` with a `switch` on a type field

Implemented in `SwitchVehicle.java`:

```java
public double calculateFee(int hours) {
    int h = Math.max(1, hours);
    switch (type) {
        case CAR:          return 20.0 * h;
        case MOTORCYCLE:   return 10.0 * h;
        case TRUCK:        return Math.min(600.0, 50.0 * h);
        case ELECTRIC_CAR: return 20.0 * h * 0.75 + kWhCharged * 8.0;
        default: throw new IllegalStateException("Unknown type " + type);
    }
}
```
It returns the same fees as the overriding design (the demo prints `310.0` for both).

### 6.1 Time per fee calculation for *k* vehicle types

| Design | Dispatch mechanism | Cost |
|---|---|---|
| **Overriding** | One virtual call through the vtable | **O(1)**, independent of *k* |
| **`switch` on enum/int** | Compiled to a `tableswitch` (jump table) | **O(1)** |
| **`if / else if` chain** (or `switch` on strings) | Sequential comparisons | **O(k)** worst case, O(k/2) average |

So the `switch` is not slower in Big-O terms when the compiler can build a jump table. The measured micro-benchmark (50 M calls, indicative only) was ≈ 4.5 ns/call (overriding) vs ≈ 4.8 ns/call (switch), i.e. the same order. The real difference is **maintainability**, not speed.

### 6.2 Classes that change when a new type (e.g. `Bus`) is added

| | Overriding | `switch` |
|---|---|---|
| New classes | **1** (`Bus`) | 0 |
| Existing classes edited | **0** | **≥ 2 places**: the `Type` enum and `calculateFee()`, plus every *other* `switch(type)` in the program (discounts, reports, display) – each one is a class that must be found and changed |
| Risk | Compiler forces `Bus` to implement the abstract method | Forgetting one case compiles and fails at run time (`default` branch) |
| Principle | Open/Closed Principle respected | Violated: class is modified, not extended |
| Class size | Each class small and focused | One method grows with *k*; all rules coupled in one class |
| Type-specific fields | Held only by the subclass that needs them | All fields (e.g. `kWhCharged`) sit in every object, wasting space and risking misuse |

### 6.3 Verdict

Both run in O(1), but overriding wins for extensibility: adding a type touches **one new class and zero existing ones**, whereas the switch design modifies existing code every time. The switch is acceptable only for a small, fixed set of types that will never grow.

## 7. Full demo output (abridged)

```
=== 2. Covariant return type ===
Car[reg=TN01AB1234, owner=NewOwner]

=== 4. final class ParkingTicket ===
Ticket{Car[reg=TN01AB1234, owner=Asha], 3h, fee incl. GST = 70.80}
Ticket{ElectricCar[reg=TN01EV9999, owner=Ravi]{kWh=20.0}, 3h, fee incl. GST = 241.90}

=== 5. Switch alternative gives identical fees ===
switch EV fee(10h) = 310.0 | override EV fee(10h) = 310.0
```
Check: Car 3 h = 20 × 3 = 60; × 1.18 GST = 70.80. EV 3 h = 60 × 0.75 + 20 × 8 = 205; × 1.18 = 241.90.

## 8. Compile-time evidence that the rules are enforced

| Attempt | Compiler message |
|---|---|
| `class Bad1 extends ParkingTicket` | `cannot inherit from final ParkingTicket` |
| Subclass overriding the final `equals(Object)` | `equals(Object) in Bad2 cannot override equals(Object) in Vehicle` |
| Overriding `public calculateFee` with package-private access (narrower) | `calculateFee(int) in Bad3 cannot override calculateFee(int) in Vehicle` |
