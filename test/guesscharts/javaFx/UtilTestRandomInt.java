package guesscharts.javaFx;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import guesscharts.Util;

class UtilTestRandomInt {
	@Test
	void randomValueBetween0And0Is0() {
		int randomInt = Util.randomInt(0, 0);
		assertThat(randomInt, is(0));
	}

	@Test
	void randomValueBetween1And1Is1() {
		int randomInt = Util.randomInt(1, 1);
		assertThat(randomInt, is(1));
	}

	@Test
	void randomValueBetween0And1Is0or1() {
		int randomInt = Util.randomInt(0, 1);
		assertThat(randomInt, anyOf(is(0), is(1)));
	}

	@Test
	@DisplayName("random value between -1 and -1 is -1")
	void randomValueBetweenNeg1AndNeg1IsNeg1() {
		int randomInt = Util.randomInt(-1, -1);
		assertThat(randomInt, is(-1));
	}

	@Test
	@DisplayName("random value between -2 and -1 is -2 or -1")
	void randomValueBetweenNeg2AndNeg1IsNeg2OrNeg1() {
		int randomInt = Util.randomInt(-2, -1);
		assertThat(randomInt, anyOf(is(-2), is(-1)));
	}

	@Test
	void exceptionThrownWhenStartBiggerThanEnd() {
		assertThrows(IllegalArgumentException.class, () -> Util.randomInt(2, 1));
	}
}
