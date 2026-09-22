package com.flexksx;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

class ArchUnitRuleTest {

  private static final ArchRule BUILDERS_MUST_BE_STATIC =
      classes().that().haveSimpleName("Builder").should().notBeInnerClasses();

  @Test
  void staticNestedBuilderPassesTheRule() {
    BUILDERS_MUST_BE_STATIC.check(importClasses(User.class, User.Builder.class));
  }

  @Test
  void innerBuilderFailsTheRule() {
    JavaClasses leaky = importClasses(LeakyUser.class, LeakyUser.Builder.class);

    AssertionError error =
        assertThrows(AssertionError.class, () -> BUILDERS_MUST_BE_STATIC.check(leaky));
    assertTrue(
        error.getMessage().contains("Class <com.flexksx.LeakyUser$Builder> is an inner class"),
        error.getMessage());
  }

  private static JavaClasses importClasses(Class<?>... classes) {
    return new ClassFileImporter().importClasses(classes);
  }
}
