package guesscharts;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import guesscharts.Util;

class UtilTestFirstGroupMatch {
	@Test
	void returnsFirstMatch() {
		String containsThePatternTwice = "a1a2";
		Pattern matchingPattern = Pattern.compile("(a.)");

		String firstMatch = Util.firstMatch(containsThePatternTwice, matchingPattern);

		assertThat(firstMatch, is("a1"));
	}

	@Test
	void returnsNullIfNotMatching() {
		String notMatchingString = "a";
		Pattern notMatchingPattern = Pattern.compile("b");

		String firstMatch = Util.firstMatch(notMatchingString, notMatchingPattern);

		assertThat(firstMatch, is(nullValue()));
	}
}
