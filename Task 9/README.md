# Task 9 – Introduce Abstraction (Smart Parking Lot)

**Bloom's level:** K4 – Analyse  |  **Language:** Java 17+ (tested on 21)

Abstraction separates **what** the system does (the contract) from **how** it is done (the implementation).

## 1. Files

| File | Kind | Role |
|---|---|---|
| `Vehicle.java` | **abstract class** | State (`reg`), constructor, `equals/hashCode`, fixed fee algorithm; abstract `rate()` |
| `Payable.java` | **interface** | Capability "can be billed" – abstract `amountDue`, default `receipt`/`label`, static `total` |
| `Chargeable.java` | **interface** | Capability "can be charged per kWh" – abstract `charging`, default `chargingWithTax`, static `totalCharging` |
| `TwoWheeler`, `Car`, `ElectricCar`, `ElectricBike` | classes | Concrete vehicles |
| `MonthlyPermit.java` | class | **Not a vehicle**, but `Payable` |
| `AbstractionDemo.java` | demo | Runs everything above |
| `alternative/` | project | Swapped design: `VehicleI` (interface) + `ChargeableBase` (abstract class) |

**Run**
```bash
javac *.java && java AbstractionDemo
cd alternative && javac *.java && java AltDemo
```

## 2. What is abstract, and what is an interface?

### 2.1 Abstract class: `Vehicle`

| Member | Abstract? | Reason |
|---|---|---|
| `rate()` | **abstract** | The only thing that really differs per vehicle; every concrete subclass *must* supply it. Kept `protected` because it is an internal detail, not part of the public contract. |
| `calculate(int minutes)` | concrete | The *algorithm* (ceiling of hours × rate) is the same for all. This is the **template method** pattern: fixed steps in the base class, one varying step (`rate()`) deferred. |
| `getReg()`, `equals()`, `hashCode()` | concrete | Identity belongs to every vehicle and must be written once. |

`Vehicle` cannot be instantiated: `new Vehicle("X")` gives `error: Vehicle is abstract; cannot be instantiated`. That is correct, because "a vehicle that is no particular kind" has no rate.

### 2.2 Interfaces: `Payable` and `Chargeable`

| Interface | Capability | Implemented by |
|---|---|---|
| `Payable` | can produce an amount due | `Vehicle` (so all vehicles) **and** `MonthlyPermit` |
| `Chargeable` | can be charged per kWh | `ElectricCar`, `ElectricBike` only |

## 3. Why an abstract class in one place and an interface in another

| Question | Abstract class (`Vehicle`) | Interface (`Payable`, `Chargeable`) |
|---|---|---|
| Relationship | **is-a** (a Car *is a* Vehicle) | **can-do** (a car *can be* charged) |
| Holds state / fields | **Yes** (`reg`) | No (only constants) |
| Constructor | **Yes** (`super(reg)`) | No |
| Protected / private members | Yes | Members are public (private helpers only from Java 9) |
| Inheritance limit | **One** parent class | **Many** interfaces |
| Spans unrelated classes | No | **Yes** (`MonthlyPermit` is not a vehicle) |

* **`Vehicle` is abstract** because all vehicles share *data* (the registration number), a *constructor*, *identity* (`equals`) and *one algorithm*. Only a class can hold those.
* **`Chargeable` is an interface** because it is a *capability* that only some vehicles have, in *different branches* of the tree (`ElectricCar` under `Car`, `ElectricBike` under `TwoWheeler`). A shared abstract class could not serve both, since each already has a different parent.
* **`Payable` is an interface** because billing is not limited to vehicles: a `MonthlyPermit` is billed too. The code `Payable.total(list, minutes)` therefore handles vehicles and permits in one list.

## 4. How a class gets multiple behaviours

Java classes have single inheritance, but a class may implement many interfaces:

```java
public class ElectricCar extends Car implements Chargeable { ... }
```
`ElectricCar` is simultaneously a `Vehicle`, a `Car`, `Payable` (inherited through `Vehicle`) and `Chargeable`. Program output:

```
Vehicle    : true
Car        : true
Payable    : true
Chargeable : true
TwoWheeler : false
```
Each interface is a separate "view" of the object, so code can depend on only the capability it needs (`Collection<? extends Chargeable>`) and ignore everything else.

## 5. Where default and static methods in an interface are useful

| Feature | Example here | Why it is useful |
|---|---|---|
| **default method** | `Payable.receipt(minutes)`, `Payable.label()`, `Chargeable.chargingWithTax(kWh)` | Gives all implementers shared behaviour built from the abstract method, with no duplicated code. It also lets you **add a method to an existing interface later without breaking** classes that already implement it. `Vehicle` can override `label()` to include the plate. |
| **static method** | `Payable.total(items, minutes)`, `Chargeable.totalCharging(items, kWh)` | A utility that belongs with the contract but not to any one object. It is called on the interface name, and no separate `Utils` class is needed. |
| **constant** | `GST_RATE`, `RATE_PER_KWH` | Implicitly `public static final`: one shared copy, with no per-object space. |

Limits: a default method cannot use instance fields (the interface has none), and it cannot override `equals`, `hashCode` or `toString`.

Output (excerpt):
```
Car TN0282             Rs   106.20 (incl. 18% GST)      <- default receipt()
Permit Dr. Rao         Rs  1770.00 (incl. 18% GST)      <- same default method, non-vehicle
Total (static Payable.total) = 1740.0
ElectricCar  charging(70) =  840.00   with tax (default method) =   991.20
```

## 6. Alternative: `Vehicle` as an interface (with default methods), `Chargeable` as an abstract class

Implemented in `alternative/`: `VehicleI` (interface, default `calculate`), `ChargeableBase` (abstract class holding `ratePerKWh`), `CarAlt`, `ElectricCarAlt`.

### 6.1 What can no longer be expressed (verified with the compiler)

| Need | Result in the swapped design | Compiler message |
|---|---|---|
| **Constructor** in `Vehicle` | Impossible in an interface | `error: <identifier> expected` |
| **Instance state** (`reg`) in `Vehicle` | An interface field is a constant; it cannot be changed or be per-object | `cannot assign a value to static final variable count` |
| **`equals` / `hashCode`** shared by all vehicles | Cannot be a default method | `default method equals in interface BadV overrides a member of java.lang.Object` |
| **`protected rate()`** | All interface members are public | `modifier protected not allowed here`; implementers get `rate() in N7 cannot implement rate() in VehicleI` if they try to narrow access |
| **Multiple inheritance of classes** (`ChargeableBase` **and** `CarAlt`) | Not allowed | `error: '{' expected` for `extends A, B` |
| **`ElectricCarAlt` is-a `CarAlt`** | Lost: it must `extend ChargeableBase`, which uses up the only parent slot | demo prints `ev instanceof CarAlt = false` |
| Subclasses must call the base constructor | `ChargeableBase` now requires it | `constructor ChargeableBase ... cannot be applied to given types` |

What **still works**: sharing the fee algorithm as a `default calculate()`, and a class implementing several interfaces. The fees come out identical (90.0 and 840.0).

### 6.2 Consequences for maintenance

* Every implementer repeats `private final String reg`, `getReg()`, `equals()` and `hashCode()` (see `CarAlt` and `ElectricCarAlt`).
* `ElectricCarAlt` also copies `rate() = 30` from `CarAlt`. If the car rate changes, two places must be edited.
* `rate()` becomes public, so internal detail leaks into the API.
* Because an abstract class is now the *capability*, every future capability (e.g. `Payable`) cannot also be a class, so the design breaks as soon as a second capability with state is needed.

### 6.3 Time per method call

| | Main design | Swapped design |
|---|---|---|
| Call through the abstract class | `invokevirtual`: vtable index, **O(1)** | – |
| Call through an interface | `invokeinterface`: look up the interface method in the class's table, **O(1)** after JIT inline caching (the plain lookup may scan the implemented interfaces, so it grows with the number of interfaces a class implements, a small constant) | the same |
| Default method | an ordinary interface call, no extra cost | the same |

**Measured** (50 M calls, indicative only): abstract-class call ≈ 5.5 ns, interface call ≈ 5.8 ns, which is within noise. So the choice should be made on design grounds, not speed. In the swapped design, `rate()` is called through the interface instead of the class, which has the same negligible effect.

### 6.4 Space per object (64-bit HotSpot, compressed pointers, approximate)

| Object | Main design | Swapped design |
|---|---|---|
| `Car` / `CarAlt` | header 12 + `reg` 4 = **16 B** | header 12 + `reg` 4 = **16 B** |
| `ElectricCar` / `ElectricCarAlt` | header 12 + `reg` 4 = **16 B**; the `RATE_PER_KWH` constant is stored once (static) | header 12 + `reg` 4 + `ratePerKWh` 8 = 24 B → **24 B** (**+8 B, 50% bigger**) |

* Implementing an interface adds **nothing** to an object; interface data is per-class, not per-object.
* The only extra per-object space in the swapped design comes from the **instance field** in `ChargeableBase`. For *n* electric vehicles that is 8n extra bytes, O(n) overall but a small constant.
* The duplicated `reg` fields are not extra space (each object still has exactly one), but they are extra **code**.

### 6.5 Verdict

Keep `Vehicle` **abstract** (it needs state, a constructor and identity) and `Chargeable` an **interface** (a stateless capability shared across branches). The swapped design loses constructors, shared state, `equals/hashCode`, protected access and the `ElectricCar is-a Car` relationship, with no gain in call speed and a larger object.

## 7. Evidence checklist

| Required evidence | Where |
|---|---|
| Justified abstract class + reasoning | Sections 2.1 and 3 |
| Justified interfaces + reasoning | Sections 2.2 and 3 |
| Multiple behaviours via interfaces | Section 4; `ElectricCar`, `MonthlyPermit` |
| Default and static methods | Section 5; `Payable.java`, `Chargeable.java` |
| What the alternative cannot express | Section 6.1 (compiler messages) |
| Time and space analysis | Sections 6.3 and 6.4 |
