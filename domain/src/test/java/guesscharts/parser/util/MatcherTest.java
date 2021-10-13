package guesscharts.parser.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

class MatcherTest {
   @Test
   void returnsFirstMatch() {
      String containsThePatternTwice = "a1a2";
      Pattern matchingPattern = Pattern.compile("(a.)");

      String firstMatch = Matcher.firstMatch(containsThePatternTwice, matchingPattern);

      assertThat(firstMatch).isEqualTo("a1");
   }

   @Test
   void throwsExceptionIfNotMatching() {
      String notMatchingString = "a";
      Pattern notMatchingPattern = Pattern.compile("b");

      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> Matcher.firstMatch(notMatchingString, notMatchingPattern));
   }
}
