package guesscharts;

public class ChartsEntrySelector {
	private final ChartsParser parser;

	public ChartsEntrySelector(ChartsParser parser) {
		this.parser = parser;
	}

	/**
	 * Calculate a random year and position within the given parameters.
	 * 
	 * @param yearFrom
	 *            the lowest year (inclusive)
	 * @param yearTo
	 *            the highest year (inclusive)
	 * @param positionFrom
	 *            the lowest position (1 based, inclusive)
	 * @param positionTo
	 *            the highest position (1 based, inclusive)
	 * @return a year and a position (1 based)
	 */
	public ChartsEntry randomEntry(int yearFrom, int yearTo, int positionFrom, int positionTo) {
		int year = randomYear(yearFrom, yearTo);
		int position = randomPosition(positionFrom, positionTo, year);

		int position0Based = position - 1;
		String artist = parser.artist(year, position0Based);
		String title = parser.title(year, position0Based);
		String detailURL = parser.detailURL(year, position0Based);
		String songURL = parser.songURL(year, position0Based);
		String coverURL = parser.coverURL(year, position0Based);

		return new ChartsEntry(year, position, artist, title, detailURL, songURL, coverURL);
	}

	private int randomYear(int yearFrom, int yearTo) {
		return Util.randomInt(yearFrom, yearTo);
	}

	private int randomPosition(int positionFrom, int positionTo, int year) {
		int highestAvailablePosition = parser.highestPosition(year);
		int highestPossiblePosition = Math.min(highestAvailablePosition, positionTo);
		return Util.randomInt(positionFrom, highestPossiblePosition);
	}
}