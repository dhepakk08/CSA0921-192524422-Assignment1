public abstract class Vehicle {
    private final String reg;

    protected Vehicle(String reg) { this.reg = reg; }

    public String getReg() { return reg; }

    /** Hourly rate - the behaviour that differs per subclass (overridden). */
    protected abstract double rate();

    /** Every started hour is charged in full. */
    public double calculate(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("minutes must be >= 0");
        // TODO 1 (done): ceiling of minutes/60 using integer arithmetic only
        int hours = (minutes + 59) / 60;          // 130 -> 189/60 = 3 ; 120 -> 179/60 = 2 ; 121 -> 180/60 = 3
        return hours * rate();                     // rate() is resolved at RUN TIME (dynamic dispatch)
    }

    /** OVERLOAD (compile-time polymorphism): same name, different parameter list. */
    public double calculate(int hours, int minutes) {
        return calculate(hours * 60 + minutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        return reg.equals(((Vehicle) o).reg);
    }

    @Override
    public int hashCode() { return reg.hashCode(); }
}
