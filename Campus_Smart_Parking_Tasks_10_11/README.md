# Tasks 10–11: Campus Smart-Parking System (Java 17+)

This is a **revised integration** based on the supplied Tasks 1–9 README descriptions, **not a copy of the earlier source code** (only READMEs were provided). It preserves the stated nine-floor 670-slot layout, `Vehicle` hierarchy, tickets, `ParkingFloor`, `ParkingSlot`, monthly passes, interfaces, and array-based active ticket storage.

## Run from project root

```sh
sh build.sh
java -cp build parking.app.Main
sh run_tests.sh
java -cp build parking.test.LookupBenchmark
```

On Windows PowerShell without `sh`, run:
```powershell
New-Item -ItemType Directory -Force build
javac -d build (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object FullName)
java -cp build parking.test.ParkingTests
java -cp build parking.app.Main
```

## Folder guide

- `src/parking/model`: immutable identities, vehicle inheritance, floor/slot, ticket, monthly pass, `Payable`, `Chargeable`.
- `src/parking/service`: fee policy and 9-floor car-park operations; `ParkingTicket[]` active tickets.
- `src/parking/app`: example application and vehicle factory with unknown-type validation.
- `src/parking/test`: deterministic assertions and reproducible lookup microbenchmark; write to console only.
- `TASK10_Answer.md` and `TASK11_Answer.md`: full rubric answers with validation/analysis/test evidence.
- `README_for_each_file`: one dedicated guide per source/script/report.
- `previous_readme_sources`: copies of supplied earlier README files for context.

**Excluded:** console output logs, screenshots, compiled `.class` files and the temporary `build/` folder.

## Fee rules and historical reconciliation

The prior task READMEs use distinct illustrative rules: Task 6 Car 20/EV 15, Task 7 Car 20, Motorcycle 10, Truck 50 capped at 600, EV discount plus energy 8/kWh; Tasks 8–9 Car 30, TwoWheeler 10, energy 12/kWh. These **cannot all be applied simultaneously**. For the integrated assessment this project chooses **Task 8–9** hourly and charging rates (Car/EV Car 30, TwoWheeler/EV Bike 10, charging 12/kWh), plus Task 7's Truck 50/h capped at 600 per stay. No GST is applied to exit charges (earlier examples vary on GST); 0 minutes incurs zero parking charge, billable hours are rounded up. Valid student/staff monthly passes waive **parking**, not energy fees. Each floor's final slot is EV-only, while EVs may also use normal spaces. Charges use whole rupees and deterministic minute values; test run represents a single billing day.

The previous sources mention `MonthlyPermit` in Task 9; this integrated version uses `MonthlyPass` consistent with Tasks 1 and 4. The Task 9 non-vehicle `Payable` demonstration is represented by the interface rather than monthly permit billing in this system.
