package guesscharts;
import java.net.URI;
import java.net.URL;

/**
 * Implement this Interface for specific chart sites.
 */
public abstract class ChartsParser {
	/**
	 * @return a link to more details about the current song. <code>null</code> if {@link #nextSong(int, int, int, int)}
	 *         wasn't called yet.
	 */
	public abstract URI getSongDetailsLink();

	/**
	 * @return a description of the current song. <code>null</code> if {@link #nextSong(int, int, int, int)} wasn't
	 *         called yet.
	 */
	public abstract String getSongDescription();

	/**
	 * @return the URL of the current song. <code>null</code> if {@link #nextSong(int, int, int, int)} wasn't called
	 *         yet.
	 */
	public abstract URL getSongURL();

	/**
	 * Randomly choose an other song between <code>yearStart</code> to <code>yearEnd</code> and
	 * <code>positionStart</code> to <code>positionEnd</code>.
	 * 
	 * @param yearStart
	 * @param yearEnd
	 * @param positionStart
	 * @param positionEnd
	 */
	public abstract void nextSong(int yearStart, int yearEnd, int positionStart, int positionEnd);

	/**
	 * @return an Array of years to choose from
	 */
	public abstract Integer[] getYears();

	/**
	 * @return an Array of chart positions to choose from
	 */
	public abstract Integer[] getPositions();
}