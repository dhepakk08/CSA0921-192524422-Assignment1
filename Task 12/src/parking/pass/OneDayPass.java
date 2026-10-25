package parking.pass;

import parking.model.Visitor;

import java.time.LocalDate;
import java.util.Objects;

public final class OneDayPass implements Pass {
    private final String passNumber;
    private final Visitor visitor;
    private final LocalDate validDate;

    public OneDayPass(String passNumber, Visitor visitor, LocalDate validDate) {
        if (passNumber == null || passNumber.isBlank()) {
            throw new IllegalArgumentException("Pass number is required.");
        }
        this.passNumber = passNumber.trim();
        this.visitor = Objects.requireNonNull(visitor, "Visitor is required.");
        this.validDate = Objects.requireNonNull(validDate, "Valid date is required.");
    }

    public String getPassNumber() {
        return passNumber;
    }

    public LocalDate getValidDate() {
        return validDate;
    }

    @Override
    public boolean isValidOn(LocalDate date) {
        return validDate.equals(date);
    }

    @Override
    public Visitor getVisitor() {
        return visitor;
    }
}
