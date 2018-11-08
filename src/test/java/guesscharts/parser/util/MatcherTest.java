package guesscharts.parser.util;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatcherTest {
	@Test
	void returnsFirstMatch() {
		String containsThePatternTwice = "a1a2";
		Pattern matchingPattern = Pattern.compile("(a.)");

		String firstMatch = Matcher.firstMatch(containsThePatternTwice, matchingPattern);

		assertThat(firstMatch, is("a1"));
	}

	@Test
	void throwsExceptionIfNotMatching() {
		String notMatchingString = "a";
		Pattern notMatchingPattern = Pattern.compile("b");

		assertThrows(NoSuchElementException.class, () -> {
			Matcher.firstMatch(notMatchingString, notMatchingPattern);
		});
	}
}
