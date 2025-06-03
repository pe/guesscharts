package guesscharts.parser;

import guesscharts.parser.util.Random;
import guesscharts.parser.util.Retry;

/**
 * Loads random chart entries. Retries on errors.
 */
public class RetryingRandomChartsParser {
    private static final int MAX_RETRY_ATTEMPTS = 10;

    private final Random random = new Random();
    private final Retry retry = new Retry(MAX_RETRY_ATTEMPTS);

    /**
     * Tries to load a random {@link ChartsEntry} within the given parameters. Retry with different random values on
     * error.
     */
    public ChartsEntry getRandomEntry(ChartsParser parser, int yearFrom, int yearTo, int positionFrom, int positionTo) {
        return retry.run(() -> {
            int position = random.between(positionFrom, positionTo);
            int year = random.between(yearFrom, yearTo);
            return parser.entryOf(year, position);
        });
    }
}
