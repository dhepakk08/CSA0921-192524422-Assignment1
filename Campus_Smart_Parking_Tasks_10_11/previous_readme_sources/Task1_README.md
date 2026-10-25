# Task 1 - Understanding the Problem and Identifying Objects

## Aim

This task analyses the basic requirements of a campus smart-parking
system and compares parallel arrays with objects for storing vehicle data.

## System Requirements

The system must record vehicle entry and exit, assign suitable parking
slots, issue tickets and calculate fees. It should support cars,
two-wheelers, electric vehicles and valid monthly passes.

The main inputs are the registration number, vehicle type, entry and exit
times, slot details, parking rates and pass information. The outputs are
the allocated slot, ticket details, parking fee and updated slot status.

## Main Classes

| Class | Responsibility |
|---|---|
| `ParkingLot` | Manages floors and parking slots |
| `ParkingSlot` | Stores slot details and availability |
| `Vehicle` | Stores registration number and vehicle type |
| `ParkingTicket` | Stores ticket and entry details |
| `PricingPolicy` | Calculates parking fees |
| `MonthlyPass` | Checks pass validity |

Important methods include `allocateSlot()`, `releaseSlot()`,
`findAvailableSlot()`, `calculateFee()` and `isValid()`.

## Important Rules

- A slot cannot be assigned to more than one vehicle at a time.
- Every ticket must have a unique number.
- A vehicle must receive a suitable available slot.
- A slot becomes available after the vehicle exits.
- Fees depend on vehicle type and parking duration.
- Valid student or staff passes may provide free parking.
- EV slots and charging fees must be handled separately.

## Parallel Arrays and Objects

I tested two simple Java programs. The first stores registration numbers,
vehicle types and entry hours in separate arrays. It works for small data,
but all arrays must stay in sync, and adding a new detail requires another
array.

The second program uses a `Vehicle` object to keep related data together.
New details can be added as fields without managing more parallel arrays.
Both examples use linear search. The programs now display the number of
records checked, the number of comparisons, and the search time in
nanoseconds. They also display the storage method and its space structure.
The timing can change between runs because of the computer and Java runtime,
so it should be treated as an observation rather than a fixed result.

For the current three-record example, both programs should make the same
number of comparisons. The main difference is the data structure: parallel
arrays store each field separately, while objects keep the three fields for
one vehicle together. Both use O(n) search time. The object version is easier
to extend, while the array version has less object-management overhead.

Both programs search for `TN02CD5678` and display its vehicle details and
comparison metrics. The output screenshots can be kept with their programs.

## Conclusion

Parallel arrays are acceptable for a small example, but a `Vehicle` class
is the better choice for this system. The measured search work is similar,
but objects keep related information together and are easier to extend.