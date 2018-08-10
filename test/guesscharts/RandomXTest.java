package guesscharts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RandomXTest {
	@Test
	void randomValueBetween1And1Is1() {
		int random = new RandomX().nextInt(1, 1);

		assertThat(random, is(1));
	}

	@Test
	void randomValueBetween0And1Is0() {
		MockedRandom rngRetruning0 = new MockedRandom(0);

		int random = new RandomX(rngRetruning0).nextInt(0, 1);

		assertThat(rngRetruning0.givenBound(), is(2));
		assertThat(random, is(0));
	}

	@Test
	void randomValueBetween0And1Is1() {
		MockedRandom rngReturning1 = new MockedRandom(1);

		int random = new RandomX(rngReturning1).nextInt(0, 1);

		assertThat(rngReturning1.givenBound(), is(2));
		assertThat(random, is(1));
	}

	@Test
	@DisplayName("random value between -2 and -1 is -2")
	void randomValueBetweenNeg2AndNeg1IsNeg2() {
		MockedRandom rngReturning0 = new MockedRandom(0);

		int random = new RandomX(rngReturning0).nextInt(-2, -1);

		assertThat(rngReturning0.givenBound(), is(2));
		assertThat(random, is(-2));
	}

	@Test
	@DisplayName("random value between -2 and -1 is -1")
	void randomValueBetweenNeg2AndNeg1IsNeg1() {
		MockedRandom rngReturning1 = new MockedRandom(1);

		int random = new RandomX(rngReturning1).nextInt(-2, -1);

		assertThat(rngReturning1.givenBound(), is(2));
		assertThat(random, is(-1));
	}

	@Test
	void exceptionThrownWhenStartBiggerThanEnd() {
		assertThrows(IllegalArgumentException.class, () -> new RandomX().nextInt(2, 1));
	}

	private final class MockedRandom extends Random {
		private final int randomValue;
		private int givenBound;

		public MockedRandom(int randomValue) {
			this.randomValue = randomValue;
		}

		@Override
		public int nextInt(int bound) {
			this.givenBound = bound;
			return randomValue;
		}

		protected final int givenBound() {
			return givenBound;
		}
	}
}
