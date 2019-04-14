package guesscharts.parser.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class RandomTest {
	// Always returns 1
	private static final java.util.Random RNG = new java.util.Random(0);

	@Test
	void randomValueBetween1And1Is1() {
		int random = new Random().between(1, 1);

		assertThat(random, equalTo(1));
	}

	@Test
	void randomValueBetween0And1Is1() {
		int random = new Random(RNG).between(0, 1);

		assertThat(random, equalTo(1));
	}

	@Test
	@DisplayName("random value between -2 and -1 is -1")
	void randomValueBetweenNeg2AndNeg1IsNeg1() {
		int random = new Random(RNG).between(-2, -1);

		assertThat(random, equalTo(-1));
	}

	@Test
	void exceptionThrownWhenStartBiggerThanEnd() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Random().between(2, 1));
	}
}
