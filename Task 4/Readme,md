# Task 4 – Design Classes, Objects and Constructors

## 1. Aim

To design classes for the Campus Smart-Parking System and demonstrate object creation, constructors, constructor chaining, the `this` keyword, and memory management.

## 2. Programs Implemented

### Program 1: Class Design and Constructors

**Folder:** `Program1_ClassDesignConstructors/`

This program implements the `Vehicle`, `ParkingSlot`, `ParkingTicket`, `PricingPolicy`, `MonthlyPass`, and `ParkingLot` classes.

* **Default constructor:** Creates a vehicle with default values.
* **Parameterized constructor:** Initializes objects with supplied values.
* **Constructor overloading:** Provides multiple ways to initialize objects.
* **`this` keyword:** Refers to the current object and distinguishes instance variables from parameters.
* **Constructor chaining:** Reuses initialization logic by calling another constructor.
* **Copy constructor:** Creates a separate `Vehicle` object with the same field values as an existing vehicle.

A parking ticket is initialized with its ticket number, vehicle, and entry time. A ticket should not be copied to issue a new ticket with the same identity.

**Output:** Displays vehicle objects, ticket details, parking-slot status, pricing information, monthly-pass validity, and parking-lot information.

*Screenshot 4.1: Insert the output of Program 1.*

### Program 2: Constructor-Based Object Creation

**Folder:** `Program2_ConstructorApproach/`

This program creates a `ParkingTicket` using a parameterized constructor. The ticket number, vehicle, and entry time are supplied during object creation. Validation prevents the construction of a ticket with missing required values.

**Output:** Displays the initialized ticket details.

*Screenshot 4.2: Insert the output of Program 2.*

### Program 3: Setter-Based Object Creation

**Folder:** `Program3_SetterApproach/`

This program creates a ticket using a default constructor and initializes it through setter methods. The `isComplete()` method checks whether all required fields have been assigned.

**Output:** Initially, the ticket is incomplete. After the setter methods are called, the ticket becomes complete and its details are displayed.

*Screenshot 4.3: Insert the output of Program 3.*

## 3. Object Memory and Life Cycle

Reference variables refer to objects, which are generally allocated on the JVM heap. Local reference variables are commonly associated with method stack frames, subject to JVM implementation and optimization.

When a ticket is no longer needed, it becomes eligible for garbage collection if no reachable references point to it. Garbage collection does not necessarily occur immediately.

## 4. Alternative Concept and Complexity Analysis

Let `n` be the number of `ParkingTicket` objects created. Assume each ticket has a fixed number of fields and validation takes constant time.

| Approach                                | Time Complexity | Space Complexity |
| --------------------------------------- | --------------- | ---------------- |
| Parameterized constructor               | O(n)            | O(n)             |
| Default constructor followed by setters | O(n)            | O(n)             |

Both approaches have the same asymptotic complexity. The main difference is initialization control.

Parameterized constructors allow required values to be validated during object creation, reducing the risk of partially initialized objects. Setter-based creation provides flexibility but allows an incomplete object to exist until all required fields are assigned.

The O(n) space complexity represents the memory required for `n` retained ticket objects, excluding other referenced objects.

## 5. Results

All three programs demonstrate the required object-creation concepts. The first demonstrates class design and constructors, the second shows constructor-based ticket initialization, and the third demonstrates the incomplete state possible with setter-based initialization.

## 6. Conclusion

Constructors help initialize objects consistently, while constructor chaining reduces repeated code. The `this` keyword clarifies field assignments and constructor calls. Parameterized constructors are suitable for objects with mandatory fields, whereas setters are useful when values need to be assigned or modified after creation.
