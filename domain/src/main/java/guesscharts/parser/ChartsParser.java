package guesscharts.parser;

import guesscharts.ParsingError;

import java.io.IOException;

/**
 * Parses a charts website.
 */
public interface ChartsParser {
	/**
	 * Gets the {@link ChartsEntry} for the given year and position.
	 *
	 * @param year the year
	 * @param position the position (1 based)
	 * @throws ParsingError on missing data for this entry (e.g. this year has no such position, this entry has no
	 * audio)
	 */
	ChartsEntry getEntry(int year, int position) throws IOException;
}
