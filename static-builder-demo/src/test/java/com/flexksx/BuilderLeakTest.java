package com.flexksx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

/** Showcases a few failure modes of a builder without the {@code static} keyword. */
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
