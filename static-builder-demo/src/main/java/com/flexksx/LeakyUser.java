package com.flexksx;

/** Counter-example: {@link User} without the {@code static} keyword on the builder. */
public record LeakyUser(String id, String email, String firstName, String lastName) {
  public LeakyUser {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("id is required");
    }
  }

  public Builder builder() {
    return new Builder();
  }

  public class Builder {
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

    public LeakyUser build() {
      return new LeakyUser(id, email, firstName, lastName);
    }
  }
}
