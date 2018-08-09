package guesscharts;

public class ChartsEntry {
	public final int year;
	public final int position;
	public final String artist;
	public final String title;
	public final String detailURL;
	public final String songURL;
	public final String coverURL;

	public ChartsEntry(int year, int position, String artist, String title, String detailURL, String songURL, String coverURL) {
		this.year = year;
		this.position = position;
		this.artist = artist;
		this.title = title;
		this.detailURL = detailURL;
		this.songURL = songURL;
		this.coverURL = coverURL;
	}
}
