package guesscharts;

import java.util.List;

public abstract class ChartsParser<T extends ChartEntry> {
	private T chartEntry;

	public ChartsParser(Class<T> chartEntryClass) {
		try {
			// T.newInstance() and no chartEntryClass parameter would be nice. Damn type erasure.
			this.chartEntry = chartEntryClass.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public void nextSong(int yearFrom, int yearTo, int positionFrom, int positionTo) {
		int year;
		int position;
		while (true) {
			year = randomInt(yearFrom, yearTo);
			int highestPositionOfYear = highestPosition(year);
			if (positionFrom > highestPositionOfYear) {
				System.out.printf("Only %d positions in %d. None between %d and %d. Reshuffling.%n",
						highestPositionOfYear, year, positionFrom, positionTo);
			} else {
				int highestPossiblePosition = Math.min(highestPositionOfYear, positionTo);
				position = randomInt(positionFrom, highestPossiblePosition);
				break;
			}
		}

		int position0Based = position-1;
		chartEntry.setYear(year);
		chartEntry.setPosition(position);
		chartEntry.setArtist(artist(year, position0Based));
		chartEntry.setTitle(title(year, position0Based));
		chartEntry.setMoreDetails(detailURL(year, position0Based));
		chartEntry.setAudio(songURL(year, position0Based));
		chartEntry.setCover(coverURL(year, position0Based));
	}

	public T chartEntry() {
		return chartEntry;
	}

	public abstract List<Integer> selectableYears();

	public abstract List<Integer> selectablePositions();


	/**
	 * @return the highest chart position of <code>year</code>.
	 */
	protected abstract int highestPosition(int year);

	/**
	 * @param position
	 *            the chart position (0 based)
	 * @param year
	 *            the year
	 * @returns URL of the album cover
	 */
	protected abstract String coverURL(int year, int position);

	/**
	 * @param position
	 *            the chart position (0 based)
	 * @param year
	 *            the year
	 * @returns URL of the audio file to play
	 */
	protected abstract String songURL(int year, int position);

	/**
	 * @param position
	 *            the chart position (0 based)
	 * @param year
	 *            the year
	 * @returns URL with more details about chart position
	 */
	protected abstract String detailURL(int year, int position);

	/**
	 * @param position
	 *            the chart position (0 based)
	 * @param year
	 *            the year
	 * @returns song title
	 */
	protected abstract String title(int year, int position);

	/**
	 * @param position
	 *            the chart position (0 based)
	 * @param year
	 *            the year
	 * @returns artist name
	 */
	protected abstract String artist(int year, int position);

	/**
	 * @return a random int between <code>start</code> and <code>end</code> (including start & end).
	 */
}