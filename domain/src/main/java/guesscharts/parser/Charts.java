package guesscharts.parser;

import java.time.Year;

/**
 * A charts website. Has lowest/highest position, first/last year and a {@link ChartsParser}.
 */
public interface Charts {
    /**
     * @return the lowest available charts position of all years.
     */
    default int lowestPosition() {
        return 1;
    }

    /**
     * @return the highest available charts position of all years (some years may not have a position that high).
     */
    default int highestPosition() {
        return 100;
    }

    /**
     * @return the first year charts data is available (not all years may have charts data).
     */
    int firstYear();

    /**
     * @return the last year charts data is available (not all years may have charts data).
     */
    default int lastYear() {
        return Year.now().minusYears(1).getValue();
    }

    /**
     * @return the ChartsParser for these charts.
     */
    ChartsParser parser();

    /**
     * @return it these charts are set up.
     */
    default boolean isConfigured() {
        return true;
    }
}