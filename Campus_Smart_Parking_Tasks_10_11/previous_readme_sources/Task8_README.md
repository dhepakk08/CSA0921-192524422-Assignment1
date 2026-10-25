# Task 8 – Apply Polymorphism (Smart Parking Lot)

**Bloom's level:** K4 – Analyse  |  **Language:** Java 17+ (tested on 21)

## 1. Files

| File | Role |
|---|---|
| `Chargeable.java` | Interface: `double charging(int kWh)` |
| `Vehicle.java` | Abstract superclass: `abstract rate()`, overloaded `calculate(...)`, `equals`, `hashCode` |
| `TwoWheeler.java`, `Car.java`, `ElectricCar.java` | Subclasses (TODO 2 and TODO 3 completed here) |
| `FeeDemo.java` | **The required main program** (prints the expected output) |
| `PolymorphismDemo.java` | Extra evidence: overloading, upcasting, static-vs-dynamic, traced dispatch, benchmark |
| `InstanceofFeeCalculator.java` | **Alternative design**: `instanceof` + casting |

**Run**
```bash
javac *.java
java FeeDemo              # required program
java PolymorphismDemo     # overloading, trace, alternative, benchmark
```

> **Note on the question text.** The scanned question was partly unreadable, so the starter code was reconstructed: `rate()` is the hourly rate, `charging(int kWh)` is the interface method, and the plates are `TN01A1`, `TN0282`, `TN3C3`. Rates are TwoWheeler 10, Car 30, charging 12 per kWh, and the demo parks for 130 minutes with 70 kWh. If your original paper differs (the scan of the third expected line was unreadable), change those constants.

## 2. The three TODOs

| TODO | Code | Why |
|---|---|---|
| **1** `calculate(int minutes)` | `int hours = (minutes + 59) / 60;`<br>`return hours * rate();` | Integer ceiling without `Math.ceil` or doubles: adding `d-1` before dividing by `d` rounds up. 130 → 189/60 = **3**; 120 → 179/60 = 2; 121 → 180/60 = 3. |
| **2** `Car` | `public Car(String reg) { super(reg); }`<br>`@Override protected double rate() { return 30; }` | `Vehicle` has no no-arg constructor, so `super(reg)` is mandatory. |
| **3** `ElectricCar` | `@Override public double charging(int kWh) { return kWh * 12.0; }` | Implements the interface. `rate()` is inherited from `Car`. |

## 3. Output (verified)

```
TwoWheeler TN01A1 -> 30.0
Car TN0282 -> 90.0
ElectricCar TN3C3 -> 930.0
true
```

Hand check, 130 min = 3 billable hours:
* TwoWheeler: 3 × 10 = **30.0**
* Car: 3 × 30 = **90.0**
* ElectricCar: 3 × 30 + 70 × 12 = 90 + 840 = **930.0**

(The first two lines match the digits that were readable in the scan, `30.0` and `90.0`. The third value in the scan was unreadable; 930.0 is what the stated rules produce.)

## 4. Why the last line prints `true`, and is it correct?

`new Car("TN0282").equals(new ElectricCar("TN0282"))` calls `Vehicle.equals`, which:
1. returns `true` immediately if the references are the same object (they are not);
2. checks `o instanceof Vehicle` – an `ElectricCar` **is a** `Vehicle` (via `Car`), so this passes;
3. compares only `reg` – both are `"TN0282"` → **true**.

The class of the object is never compared, so a `Car` and an `ElectricCar` with the same plate are equal. The same logic gives `hashCode` equality, because it also uses only `reg`.

**Decision: this is the correct behaviour for a parking system.** A registration plate identifies one physical vehicle, so it should be the vehicle's identity. If the lot records `TN0282` as a `Car` and later as an `ElectricCar` (for example a data-entry correction), they must not be treated as two vehicles in a `HashSet` or `HashMap`, or the same car could hold two slots. This is the same rule as in Task 7. Equality stays **symmetric** (`a.equals(b) == b.equals(a)`) because the rule lives in one place and subclasses do not override it. If you had used `getClass() != o.getClass()` instead, the result would be `false`, which would be correct only for a system where "same plate, different type" is a different record.

## 5. Compile-time polymorphism (overloading)

`Vehicle` has two overloads of `calculate`:

```java
public double calculate(int minutes)
public double calculate(int hours, int minutes)   // delegates to the first
```

**How the compiler selects one (all decided before the program runs):**
1. Same name, so it collects every `calculate` in the declared type.
2. Keeps those whose number of parameters matches the call.
3. Among those, picks the one whose parameter types match best: exact match first, then widening (`byte → short → int → long → float → double`), then boxing, then varargs.
4. If none or more than one fits equally, it is a compile error (ambiguity).

| Call | Selected | Reason |
|---|---|---|
| `v.calculate(130)` | `calculate(int)` | 1 argument |
| `v.calculate(2, 10)` | `calculate(int,int)` | 2 arguments |
| `byte b = 90; v.calculate(b)` | `calculate(int)` | `byte` widened to `int` |

The return type alone cannot distinguish overloads.

**Overloading uses the DECLARED type, never the actual object:**
```java
static String admit(Vehicle v) { ... }
static String admit(Car c)     { ... }
Vehicle asVehicle = new Car("TN0282");
admit(asVehicle)   // prints admit(Vehicle) – the compiler only sees the declared type
```

## 6. Upcasting: one array, many types

```java
Vehicle[] parked = { new TwoWheeler("TN01A1"), new Car("TN0282"), new ElectricCar("TN3C3") };
```
A subclass object can be assigned to a superclass reference (**upcasting**), automatically and safely, since every `Car` *is a* `Vehicle`. The array's element type is `Vehicle` (the declared type), but each slot refers to an object of a different actual type. Through the reference you can only call methods that `Vehicle` declares; `charging()` is invisible (that is why the code checks `instanceof Chargeable` and casts down).

## 7. Run-time polymorphism (dynamic method dispatch)

The call `x.calculate(130)` is written once, but `rate()` inside it runs different code depending on the **actual** object. The JVM chooses at run time by looking for the method starting from the object's real class and moving up through its parents.

### Traced call: `Vehicle x = new ElectricCar("TN3C3"); x.calculate(130);`

| Step | When | What happens |
|---|---|---|
| 1 | compile time | **Declared type** = `Vehicle`. The compiler only checks that `Vehicle` has `calculate(int)`. |
| 2 | run time | **Actual object type** = `ElectricCar`. |
| 3 | run time | `calculate(int)` is looked up: `ElectricCar`? no. `Car`? no. `Vehicle`? **yes** (not overridden), so its body runs. |
| 4 | run time | Inside it, `rate()` is a virtual call on `this`, whose actual type is still `ElectricCar`. |
| 5 | run time | `rate()` is looked up: `ElectricCar`? no (not overridden). `Car`? **yes** → returns **30**. |
| 6 | run time | hours = (130+59)/60 = 3, fee = 3 × 30 = **90.0** (then +840 charging in `FeeDemo`). |

Same call with a `TwoWheeler`: step 5 finds `rate()` in `TwoWheeler` → 3 × 10 = 30. The program prints this trace itself (section 4 of `PolymorphismDemo`), using reflection to find the declaring class.

### Static vs dynamic selection

| | Overloading | Overriding |
|---|---|---|
| Chosen by | **Compiler** | **JVM at run time** |
| Based on | Declared (static) types of arguments | Actual type of the object |
| Also called | Compile-time / static / early binding | Run-time / dynamic / late binding |
| Here | `calculate(int)` vs `calculate(int,int)` | `rate()` in `TwoWheeler` vs `Car` |

## 8. Why this design avoids long `if-else` chains that test the vehicle type

* `calculate()` is written **once**, and each class supplies only its own `rate()`; no code asks "what type are you?".
* **Adding a vehicle type** (e.g. `Bus`) means writing one class that overrides `rate()`. `Vehicle`, `FeeDemo` and the other subclasses are untouched (Open/Closed Principle).
* The compiler forces every concrete subclass to implement the abstract `rate()`, so you cannot forget a type.
* Fee logic stays next to the data of its class (cohesion), instead of being spread across the program.

## 9. Alternative concept: `instanceof` + casting

`InstanceofFeeCalculator.fee(...)` does the same job:

```java
if (v instanceof ElectricCar)      return hours * 30 + ((ElectricCar) v).charging(kWh);
else if (v instanceof Car)         return hours * 30;
else if (v instanceof TwoWheeler)  return hours * 10;
else throw new IllegalArgumentException(...);
```
It gives identical fees (section 6 of the demo output).

**Hidden trap – order matters.** `ElectricCar` must be tested **before** `Car`, because every `ElectricCar` is also a `Car`. If the order is reversed the code still compiles and runs, but electric cars never pay charging fees: a silent, costly bug. Dynamic dispatch has no such problem.

### 9.1 Time per call, *k* vehicle types

| | Cost |
|---|---|
| **Dynamic dispatch** | **O(1)**: one virtual-table lookup, whatever *k* is |
| **`instanceof` chain** | up to *k* type checks: **O(k)** worst case, about (k+1)/2 on average if types are equally likely; each check is cheap, but they accumulate |

**Measured** (50 M calls, 3 types, indicative only): dynamic dispatch ≈ 5.9 ns, `instanceof` chain ≈ 2.5 ns. For *k* = 3 the `instanceof` version was actually faster here (the JIT compiler turns the short chain into a few pointer comparisons, and our dispatch makes two calls per fee). So **speed is not the argument for polymorphism**; the asymptotic cost only favours dispatch when *k* is large, and for small *k* the difference is a few nanoseconds either way. Micro-benchmarks vary by JVM and machine.

### 9.2 Code size and maintenance

| | Dynamic dispatch | `instanceof` chain |
|---|---|---|
| Add a new type | **1 new class**, 0 edits to existing code | **1 new branch in every chain** (fee, receipt, discount, report…) – with *m* operations, *m* edits |
| Forgetting a type | Compile error (abstract method) | Compiles; fails at run time (`else`) or is silently wrong |
| Order of tests | Irrelevant | Subclass must precede superclass |
| Casting | None | Cast needed after every check (`ClassCastException` risk if wrong) |
| Code growth | Spread across *k* small classes | One method grows linearly with *k* and *m* |
| Testing | Each class tested alone | One big method, with a test per branch |

**Trade-off to be aware of:** `instanceof` is easier when you add a **new operation** over a fixed set of types (write one new function), whereas polymorphism is easier when you add a **new type** (write one new class). A parking lot gains new vehicle types more often than new operations, and so polymorphism is the better fit. (Modern Java also offers `switch` with pattern matching over `sealed` types, which is a cleaner compile-checked form of the type-test approach, but the same "edit every switch" issue remains.)

### 9.3 A note on `x instanceof Chargeable` in `FeeDemo`

This is a different, legitimate use: testing for an optional **capability** (interface) rather than a concrete type. It does not grow with each new vehicle class. A fully polymorphic alternative is to give `Vehicle` a method such as `double extraCharges(int kWh)` that returns 0 by default and is overridden by `ElectricCar`, which removes the `instanceof` and the cast completely.

## 10. Evidence checklist

| Required evidence | Where |
|---|---|
| Overloading | `Vehicle.calculate(int)` / `calculate(int,int)`; `PolymorphismDemo` section 1 |
| Dynamic dispatch | `rate()` via `FeeDemo`; `PolymorphismDemo` sections 3 and 4 |
| Traced call (declared type, actual type, method run) | Section 7 table above; section 4 of `PolymorphismDemo` output |
| Upcasting | `Vehicle[] parked` |
| Interface | `Chargeable` implemented by `ElectricCar` |
| `equals` decision | Section 4 |
| Alternative and complexity | Section 9 and `InstanceofFeeCalculator.java` |
