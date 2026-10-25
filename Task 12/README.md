# Task 12 – Re-evaluate and Adapt the Design

## Run

From this directory:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem .\src -Recurse -Filter "*.java").FullName
java -cp out parking.app.Main
java -cp out parking.test.Task12Test
```

Remove the temporary `out` directory after testing if compiled files should not be retained.

## Adaptations

- Added `EBike` with a flat Rs. 25 parking fee per session.
- Added `DeliveryVan` with the car rate off-peak and double the car rate during 09:00 inclusive to 17:00 exclusive.
- Added `Visitor`, `Pass`, and `OneDayPass`; a pass is valid only on its configured calendar date.
- Added `PricingRule` and time-aware `PricingPolicy`.
- Replaced uniform floor construction with a jagged capacity array such as `{2, 3, 1, 4, 2}`.
- Preserved vehicle inheritance, validation, ticket lifecycle, slot allocation, HashMap active-ticket lookup, and existing vehicle rates.

See [TASK12_Explanation.md](TASK12_Explanation.md) for impact analysis, design evaluation, test evidence, complexity, and limitations.
