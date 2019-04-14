package guesscharts.parser;

/**
 * The data of a charts entry.
 */
public class ChartsEntry {
	public final int year;
	public final int position;
	public final String artist;
	public final String title;
	public final String moreDetails;
	public final String audio;
	public final String cover;

	public ChartsEntry(int year, int position, String artist, String title, String moreDetails, String audio,
					   String cover) {
		this.year = year;
		this.position = position;
		this.artist = artist;
		this.title = title;
		this.moreDetails = moreDetails;
		this.audio = audio;
		this.cover = cover;
	}

	@Override
	public String toString() {
		return String.format("%d/%02d %s by %s", year, position, title, artist);
	}
}
