package com.flexksx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Shows what forgetting {@code static} on a builder costs you.
 *
 * <p>{@link User.Builder} is static nested. {@link LeakyUser.Builder} is inner, so every instance
 * of it needs an enclosing {@link LeakyUser}: the object the builder was supposed to produce.
 */
class BuilderLeakTest {

  @Test
  void staticBuilderStartsWithoutARecord() throws Exception {
    assertEquals(0, User.Builder.class.getDeclaredConstructor().getParameterCount());
  }

  @Test
  void innerBuilderCannotStartWithoutARecord() {
    assertThrows(NoSuchMethodException.class, LeakyUser.Builder.class::getDeclaredConstructor);

    Class<?>[] onlyConstructor =
        LeakyUser.Builder.class.getDeclaredConstructors()[0].getParameterTypes();
    assertEquals(List.of(LeakyUser.class), List.of(onlyConstructor));
  }
}
