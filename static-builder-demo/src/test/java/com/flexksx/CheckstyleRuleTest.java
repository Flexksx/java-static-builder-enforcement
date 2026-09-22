package com.flexksx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Checks the rule "a nested class named Builder must be static" with Checkstyle. */
class CheckstyleRuleTest {

  private static final String CONFIGURATION = "../checkstyle.xml";
  private static final Path SOURCE_DIRECTORY = Path.of("src/main/java/com/flexksx");

  @Test
  void staticNestedBuilderPassesTheRule() throws Exception {
    assertEquals(0, violationsIn("User.java"));
  }

  @Test
  void innerBuilderFailsTheRule() throws Exception {
    assertEquals(1, violationsIn("LeakyUser.java"));
  }

  private static int violationsIn(String fileName) throws Exception {
    Checker checker = new Checker();
    checker.setModuleClassLoader(Checker.class.getClassLoader());
    checker.configure(
        ConfigurationLoader.loadConfiguration(
            CONFIGURATION, new PropertiesExpander(System.getProperties())));
    try {
      return checker.process(List.of(SOURCE_DIRECTORY.resolve(fileName).toFile()));
    } finally {
      checker.destroy();
    }
  }
}
