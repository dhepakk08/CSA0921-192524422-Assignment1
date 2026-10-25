# Task 3: Design the Control Logic

## 1. Aim

To implement parking-slot allocation and release operations using Java, apply decision-making and looping statements, and analyze the time and space complexity of the algorithms.

## 2. Program Description

The program uses a jagged two-dimensional array to represent a parking facility. Each row represents a floor, and each element represents a parking slot.

An empty slot contains `null`, while an occupied slot stores a vehicle registration number.

The test facility contains two floors with capacities of two slots and one slot, respectively.

## 3. Operations Implemented

### 3.1 Slot Allocation

The `allocate()` method uses nested `for` loops to search the floors and slots sequentially.

* If a free slot is found, the vehicle number is stored.
* The method returns the floor and slot indices.
* If every slot is occupied, the method returns `null`.

### 3.2 Slot Release

The `release()` method validates the floor and slot indices before accessing the array.

* Invalid indices return `false`.
* An already-empty slot returns `false`.
* An occupied slot is cleared and returns `true`.

## 4. Algorithm

### Allocation Algorithm

1. Traverse each floor.
2. Traverse every slot on that floor.
3. Check whether the slot contains `null`.
4. If available, store the vehicle number and return its location.
5. If no free slot exists, return `null`.

### Release Algorithm

1. Validate the floor index.
2. Validate the slot index.
3. Check whether the slot is occupied.
4. If occupied, set its value to `null` and return `true`.
5. Otherwise, return `false`.

## 5. Complexity Analysis

Let `n` represent the total number of parking slots.

| Operation  | Time Complexity | Auxiliary Space |
| ---------- | --------------- | --------------- |
| Allocation | O(n) worst case | O(1)            |
| Release    | O(1)            | O(1)            |

Allocation may examine every slot when the facility is full. Release accesses a specific slot directly after validating its indices.

The parking array itself requires O(n) space and is excluded from the auxiliary-space column.

## 6. Alternative Approach

An alternative is to maintain a stack of available slot positions.

| Feature          | Nested-Loop Search | Free-Slot Stack |
| ---------------- | ------------------ | --------------- |
| Allocation       | O(n) worst case    | O(1) amortized  |
| Release          | O(1)               | O(1) amortized  |
| Additional space | O(1)               | O(n)            |

The stack improves allocation efficiency but requires additional memory and careful management to prevent duplicate entries. The nested-loop approach is simpler and suitable for this implementation.

## 7. Results

The program successfully allocates available slots, rejects allocation when the facility is full, validates release operations, and reuses a released slot.

**Screenshot 3.1:** Insert the output of the parking allocation and release program.

## 8. Conclusion

The program demonstrates decision-making using `if` statements and iteration using nested `for` loops. Allocation uses linear search, while release takes constant time after index validation. A free-slot stack is a possible alternative for improving allocation efficiency.
