package guesscharts;

import java.util.ArrayList;
import java.util.Calendar;
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
		// Some subclasses require numberOfChartEntriesOfYear to be called before the *OfPosition methods.
		int year = randomInt(yearFrom, yearTo);
		int position = randomInt(positionFrom, Math.min(positionTo, numberOfChartEntries(year))) - 1;

		chartEntry.setYear(year);
		chartEntry.setPosition(position);
		chartEntry.setArtist(artist(year, position));
		chartEntry.setTitle(title(year, position));
		chartEntry.setMoreDetails(detailURL(year, position));
		chartEntry.setAudio(songURL(year, position));
		chartEntry.setCover(coverURL(year, position));
	}

	public T chartEntry() {
		return chartEntry;
	}

	public abstract List<Integer> selectableYears();

	public abstract List<Integer> selectablePositions();

	protected List<Integer> selectableYearsInternal(int firstYear) {
		int yearCount = Calendar.getInstance().get(Calendar.YEAR) - firstYear;
		List<Integer> years = new ArrayList<Integer>(yearCount);
		for (int i = 0; i < yearCount; i++) {
			years.add(firstYear + i);
		}
		return years;
	}

	/**
	 * @return the number of chart positions of <code>year</code>.
	 */
	protected abstract int numberOfChartEntries(int year);

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
	private int randomInt(int start, int end) {
		int range = end - start + 1;
		return (int) (Math.random() * range + start);
	}
}