package guesscharts.parser;

import guesscharts.ParsingError;
import guesscharts.parser.util.Random;

import java.io.IOException;

/**
 * Tries loading random chart entries and retries on errors.
 */
public class RetryingRandomChartsParser {
	private final ChartsParser parser;
	private final int maxTries;
	private final Random random;

	public RetryingRandomChartsParser(ChartsParser parser) {
		this(parser, 10);
	}

	public RetryingRandomChartsParser(ChartsParser parser, int tries) {
		if (tries < 1) {
			throw new IllegalArgumentException();
		}
		this.parser = parser;
		this.maxTries = tries;
		this.random = new Random();
	}

	/**
	 * Tries to load a random {@link ChartsEntry} within the given parameters. Retry
	 * with different random values if a {@link ParsingError} or {@link IOException}
	 * is thrown.
	 */
	public ChartsEntry getRandomEntry(int yearFrom, int yearTo, int positionFrom, int positionTo) {
		for (int tryCount = 1; tryCount <= maxTries; tryCount++) {
			try {
				int position = random.between(positionFrom, positionTo);
				int year = random.between(yearFrom, yearTo);
				return parser.getEntry(year, position);
			} catch (ParsingError | IOException e) {
				if (tryCount < maxTries) {
					System.out.println(e.toString() + " retrying...");
				} else {
					throw new RuntimeException(e);
				}
			}
		}
		throw new IllegalStateException();
	}
}
