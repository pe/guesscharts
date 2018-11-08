package guesscharts.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RetryingRandomChartsParserTest {
	private static final int A_YEAR = 0;
	private static final int A_POSITION = 0;
	private static final ChartsEntry A_ENTRY = new ChartsEntry(A_YEAR, A_POSITION, "", "", "", "", "");

	@Test
	void exceptionThrownWhenTriesSmallerThan1() {
		int tries = 0;
		ChartsParser parser = new NoopParser();

		assertThrows(IllegalArgumentException.class, () -> {
			new RetryingRandomChartsParser(parser, tries).getRandomEntry(A_YEAR, A_YEAR, A_POSITION, A_POSITION);
		});
	}

	@Test
	void noExceptionThrownOnFirstErroneousCallWhenTryingTwice() {
		int tries = 2;
		int erroneousCalls = 1;
		ChartsParser parser = new ExceptionThrowingParser(erroneousCalls);

		assertDoesNotThrow(() -> {
			new RetryingRandomChartsParser(parser, tries).getRandomEntry(A_YEAR, A_YEAR, A_POSITION, A_POSITION);
		});
	}

	@Test
	void exceptionThrownOnSecondErroneousCallWhenTryingTwice() {
		int tries = 2;
		int erroneousCalls = 2;
		ChartsParser parser = new ExceptionThrowingParser(erroneousCalls);

		assertThrows(RuntimeException.class, () -> {
			new RetryingRandomChartsParser(parser, tries).getRandomEntry(A_YEAR, A_YEAR, A_POSITION, A_POSITION);
		});
	}

	@Test
	void returningEntryFromUnderlyingParser() {
		int tries = 1;
		ChartsParser parser = new ReturningGivenEntry(A_ENTRY);

		ChartsEntry entry = new RetryingRandomChartsParser(parser, tries).getRandomEntry(A_YEAR, A_YEAR, A_POSITION,
				A_POSITION);

		assertThat(entry, is(sameInstance(A_ENTRY)));
	}

	private class NoopParser implements ChartsParser {
		@Override
		public ChartsEntry getEntry(int year, int position) {
			throw new UnsupportedOperationException();
		}
	}

	private class ExceptionThrowingParser implements ChartsParser {
		private final int erroneousCalls;
		private int callCount = 0;

		ExceptionThrowingParser(int erroneousCalls) {
			this.erroneousCalls = erroneousCalls;
		}

		@Override
		public ChartsEntry getEntry(int year, int position) throws IOException {
			callCount++;
			if (callCount <= erroneousCalls) {
				throw new IOException();
			} else {
				return null;
			}
		}
	}

	private class ReturningGivenEntry implements ChartsParser {
		private final ChartsEntry entry;

		ReturningGivenEntry(ChartsEntry entry) {
			this.entry = entry;
		}

		@Override
		public ChartsEntry getEntry(int year, int position) {
			return entry;
		}
	}
}
