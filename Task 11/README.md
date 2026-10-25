# Task 11 – Implement, Test and Defend the Design

## Run the application

From the `Task11_Implementation_Testing` directory:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem .\src -Recurse -Filter "*.java").FullName
java -cp out parking.app.Main
```

## Display all Task 11 outputs with prompts

After compiling, run the following command to display the vehicle scenario,
automated tests, Rs. 94 scenario, and lookup benchmark in one terminal output:

```powershell
java -cp out parking.app.Task11TerminalDemo
```

## Run automated tests

```powershell
java -cp out parking.test.ParkingSystemTest
```

## Run the lookup benchmark

```powershell
java -cp out parking.test.LookupBenchmark
```

The benchmark compares registration lookup using an array, an `ArrayList`, and
a `HashMap`. Its timing values vary by machine; the expected complexity result
is linear lookup for the first two structures and expected constant-time lookup
for the `HashMap`.

The project uses a dependency-free test runner so it can be compiled with the JDK alone. Delete `out` after testing if compiled files should not be retained.

## Features

- Vehicle entry creates a validated ticket and allocates the first available slot.
- Vehicle exit closes the ticket, releases the slot, calculates the fee, and adds it to daily collection.
- Duplicate active registrations, unknown vehicle types, invalid slot numbers, negative times, and invalid values are rejected.
- Car, motorcycle, electric-car, and truck fee rules are preserved from Task 10.

See [TASK11_Explanation.md](TASK11_Explanation.md) for the test table, manual collection calculation, OOP evaluation, storage alternatives, and design limitation.
