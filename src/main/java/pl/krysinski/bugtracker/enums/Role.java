package pl.krysinski.bugtracker.enums;

public enum Role {
    USER("User"),
    ADMIN("Admin");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
