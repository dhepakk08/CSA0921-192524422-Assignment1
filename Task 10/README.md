# Task 10 – Encapsulation through Packages and Access Modifiers

## Run

From the `Task10_Encapsulation` directory:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem -Recurse -Filter *.java)
java -cp out parking.app.Main
```

Delete the optional `out` directory after testing if compiled files should not be retained.

## Design

- `parking.model` contains validated domain objects.
- `parking.service` contains floor, lot, and pricing services.
- `parking.app` contains the application entry point.
- Mutable state is private and changed through validated public methods.
- `protected` is used for subclass-only rate behavior.
- Package-private `slotAt` demonstrates default access inside `parking.service`.
- Public methods expose the stable API between packages.

## Preserved fee rules

Motorcycle: Rs. 10/hour; car and electric car: Rs. 30/hour; truck: Rs. 50/hour capped at 12 hours; electric charging: Rs. 12/kWh. Durations use ceiling-to-the-next-hour billing with a minimum of one hour.
