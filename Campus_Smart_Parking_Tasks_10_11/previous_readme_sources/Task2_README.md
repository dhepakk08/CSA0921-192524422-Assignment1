# Task 2: Choose the Data Representation

## 1. Aim

To analyze and compare different data representations for a Campus Smart-Parking System. The task focuses on selecting suitable Java data types, representing parking slots using arrays, understanding type conversion, and comparing the performance and memory requirements of different approaches.

## 2. System Configuration

The parking facility consists of 9 floors with a total capacity of 670 slots.

| Floor | Number of Slots |
|---|---:|
| Floor 1 | 20 |
| Floor 2 | 50 |
| Floor 3 | 50 |
| Floor 4 | 50 |
| Floor 5 | 100 |
| Floor 6 | 100 |
| Floor 7 | 100 |
| Floor 8 | 100 |
| Floor 9 | 100 |
| **Total** | **670** |

The configuration is predefined, while vehicle registration details and search inputs are provided during program execution.

## 3. Programs Implemented

### Program 1: Object-Oriented Smart-Parking System

This program models the parking facility using classes and objects.

The main components include:

- `Vehicle` – stores vehicle registration details and type.
- `ParkingSlot` – represents an individual parking slot.
- `ParkingFloor` – manages the slots on a floor.
- `ParkingTicket` – stores ticket and entry information.
- `PricingPolicy` – calculates parking charges.
- `ParkingLot` – manages the parking floors and slot allocation.
- `Main` – handles user interaction.

The program supports vehicle entry, ticket generation, parking fee calculation, vehicle exit, slot release, and availability checking.

**Approach:** Object-oriented representation.

### Program 2: One-Dimensional Array

This program represents all 670 parking slots using a single `String[]` array.

Each array index represents one parking slot. The program searches for a vehicle registration number and displays its floor, slot number, and search metrics.

The floor and slot number are calculated from the array index using the predefined floor capacities.

**Approach:** One-dimensional array representation.

### Program 3: Two-Dimensional Array

This program represents parking slots using a jagged two-dimensional array, `String[][]`.

Each row represents a floor, and the number of elements in each row corresponds to that floor's capacity.

The program searches for a vehicle registration number and displays its location and search metrics.

**Approach:** Jagged two-dimensional array representation.

## 4. Selection of Data Types

| Data | Suggested Java Data Type | Reason |
|---|---|---|
| Floor number | `int` | Represents a whole number |
| Slot number | `int` | Represents a whole number |
| Vehicle registration number | `String` | Supports letters and digits |
| Entry and exit time | `LocalDateTime` | Stores date and time |
| Parking duration | `Duration` | Represents the time between entry and exit |
| Parking fee | `BigDecimal` | Supports accurate decimal calculations |
| Slot availability | `boolean` | Represents available or occupied status |

Using appropriate data types improves clarity and reduces calculation errors.

## 5. Type Conversion and Billing

Parking duration may be measured in minutes and converted into hours for billing.

A potential problem occurs when integer division is used:

```java
int minutes = 90;
int hours = minutes / 60;