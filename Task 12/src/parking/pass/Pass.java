package parking.pass;

import parking.model.Visitor;

import java.time.LocalDate;

public interface Pass {
    boolean isValidOn(LocalDate date);
    Visitor getVisitor();
}
