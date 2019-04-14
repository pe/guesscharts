package guesscharts.parser.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RetryTest {
	@Test
	void exceptionThrownWhenMaxAttemptsSmallerThan1() {
		int maxAttempts = 0;

		assertThrows(IllegalArgumentException.class, () -> new Retry(maxAttempts));
	}

	@Test
	void returningEntryFromUnderlyingCallable() {
		int maxAttempts = 1;
		String expected = "return value";
		Callable<String> task = () -> expected;

		String actual = new Retry(maxAttempts).run(task);

		assertThat(actual, is(expected));
	}

	@Test
	void exceptionOfFirstCallSuppressedWhenTryingTwice() {
		int maxAttempts = 2;
		Callable<Void> throwException = ThrowException.forNCalls(1);

		assertDoesNotThrow(() -> new Retry(maxAttempts).run(throwException));
	}

	@Test
	void exceptionOfSecondCallThrownWhenTryingTwice() {
		int maxAttempts = 2;
		Callable<Void> throwException = ThrowException.forNCalls(2);

		assertThrows(Exception.class, () -> new Retry(maxAttempts).run(throwException));
	}

	private static class ThrowException implements Callable<Void> {
		private final int numberOfCalls;
		private int callCount = 0;

		private ThrowException(int numberOfCalls) {
			this.numberOfCalls = numberOfCalls;
		}

		static ThrowException forNCalls(int numberOfCalls) {
			return new ThrowException(numberOfCalls);
		}

		@Override
		public Void call() throws Exception {
			callCount++;
			if (callCount <= numberOfCalls) {
				throw new Exception();
			} else {
				return null;
			}
		}
	}
}
