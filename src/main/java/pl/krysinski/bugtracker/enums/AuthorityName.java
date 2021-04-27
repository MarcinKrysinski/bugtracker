package pl.krysinski.bugtracker.enums;

public enum AuthorityName {
    ROLE_USERS_TAB("Przeglądanie użytkowników"),
    ROLE_CREATE_USER("Tworzenie nowych użytkowników");

    private final String description;

    AuthorityName(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
