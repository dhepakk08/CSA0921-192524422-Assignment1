# Task 5 — Apply Static Members and the Object Life Cycle

## Objective
Demonstrate static and instance members, a static method, object passing and returning, and the life cycle of a `ParkingTicket`.

## Program 1 — Static Ticket Counter and Occupancy
`Program1_StaticMembers/Main.java`

- `ParkingTicket.nextTicketNumber` is static, so all ticket objects share the counter within this running application.
- `ParkingFacility.occupiedSlots` is an instance variable, so each facility tracks its own occupancy.
- `ParkingTicket.isValidVehicleNumber()` is static because it validates a supplied value without depending on a particular ticket object.
- A static block is unnecessary here because the counter can be initialized at its declaration.
- Facility prefixes (`N-` and `S-`) make each ticket's facility clear.

### Complexity
A shared counter uses O(1) storage. One occupancy field per facility uses O(k) storage for k facilities. Retaining n ticket objects requires O(n) storage, excluding referenced data.

## Program 2 — Object Passing, Returning and Life Cycle
`Program2_ObjectLifeCycle/Main.java`

- `createTicket()` creates a ticket and returns its reference.
- `processTicket()` receives a reference to a ticket.
- Java passes the reference by value. A method can use the reference to access the object, but reassigning its parameter does not replace the caller's reference.
- A ticket does not automatically disappear when a vehicle exits; it may be retained for billing, history, audits or disputes.
- An object becomes eligible for garbage collection when no reachable references to it remain. Setting a variable to `null` does not guarantee immediate collection.

### Complexity
Creating and processing one ticket takes O(1) time for the fixed operations shown. Retaining n tickets requires O(n) object storage.

## Compile and run
Run each program from its own folder because both programs use the class name `Main`.

```bash
javac Main.java
java Main
```

`expected-output.txt` contains output captured from the executed program. Add your own console screenshot in the location indicated by `SCREENSHOT_PLACEHOLDER.txt`.

## Design note
A static counter is shared only within the relevant class-loading context. A production multi-process or multi-server system needs coordinated ID allocation if global uniqueness is required.
