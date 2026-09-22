package com.flexksx;

/**
 * Correct example for the demo.
 *
 * <p>{@code Builder} is a static nested class. It holds no reference to a
 * {@code User}, so you can build one from nothing.
 */
public record User(String id,
                   String email,
                   String firstName,
                   String lastName
) {
    public User {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String email;
        private String firstName;
        private String lastName;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public User build() {
            return new User(id, email, firstName, lastName);
        }
    }
}
