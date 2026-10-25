package parking.model;

public final class Visitor {
    private final String visitorId;
    private final String name;

    public Visitor(String visitorId, String name) {
        if (visitorId == null || visitorId.isBlank()) {
            throw new IllegalArgumentException("Visitor ID is required.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Visitor name is required.");
        }
        this.visitorId = visitorId.trim();
        this.name = name.trim();
    }

    public String getVisitorId() {
        return visitorId;
    }

    public String getName() {
        return name;
    }
}
