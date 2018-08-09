package guesscharts;

import java.util.List;

public abstract class ChartsParser {
	/**
	 * @param position
	 *            the chart position (0 based)
	 * @param year
	 *            the year
	 * @returns URL of the album cover
	 */
	public abstract String coverURL(int year, int position);

	/**
	 * @param position
	 *            the chart position (0 based)
	 * @param year
	 *            the year
	 * @returns URL of the audio file to play
	 */
	public abstract String songURL(int year, int position);

	/**
	 * @param position
	 *            the chart position (0 based)
	 * @param year
	 *            the year
	 * @returns URL with more details about chart position
	 */
	public abstract String detailURL(int year, int position);

	/**
	 * @param position
	 *            the chart position (0 based)
	 * @param year
	 *            the year
	 * @returns song title
	 */
	public abstract String title(int year, int position);

	/**
	 * @param position
	 *            the chart position (0 based)
	 * @param year
	 *            the year
	 * @returns artist name
	 */
	public abstract String artist(int year, int position);

	/**
	 * @return the highest chart position of the <code>year</code> (1 based).
	 */
	public abstract int highestPosition(int year);

	/**
	 * TODO
	 */
	public abstract List<Integer> availablePositions();

	/**
	 * TODO
	 */
	public abstract List<Integer> availableYears();
}