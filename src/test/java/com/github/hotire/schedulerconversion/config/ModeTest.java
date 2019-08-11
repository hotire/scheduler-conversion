package com.github.hotire.schedulerconversion.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Arrays;
import org.junit.Test;

public class ModeTest {

  @Test
  public void lookup_include() {
    // Given
    final String modeName = "include";

    // When
    final Mode mode = Mode.lookup(modeName);

    // Then
    assertThat(mode).isEqualTo(Mode.INCLUDE);
  }

  @Test
  public void lookup_exclude() {
    // Given
    final String modeName = "exclude";

    // When
    final Mode mode = Mode.valueOf(modeName);

    // Then
    assertThat(mode).isEqualTo(Mode.EXCLUDE);
  }

  @Test
  public void lookup_no_enum_constant() {
    // Given
    final String modeName = "";

    Arrays.stream(Mode.values())
      .filter(mode -> mode.name().equals(modeName))
      .forEach(mode -> fail(modeName + " is enum constant"));

    // When
    final Mode mode = Mode.lookup(modeName);

    // Then
    assertThat(mode).isEqualTo(Mode.INCLUDE);
  }
}