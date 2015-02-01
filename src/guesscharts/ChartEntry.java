package guesscharts;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ChartEntry {
	private int year;
	private int position;
	private String artist;
	private String title;
	private String moreDetails;
	private String audio;
	private String cover;

	private final PropertyChangeSupport propertySupport;

	public ChartEntry() {
		propertySupport = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}

	public final int getYear() {
		return year;
	}

	public final void setYear(int year) {
		int oldYear = this.year;
		this.year = year;
		propertySupport.firePropertyChange("year", oldYear, year);
	}

	public final int getPosition() {
		return position;
	}

	public final void setPosition(int position) {
		int oldPosition = this.position;
		this.position = position;
		propertySupport.firePropertyChange("position", oldPosition, position);
	}

	public final String getArtist() {
		return artist;
	}

	public final void setArtist(String artist) {
		String oldArtist = this.artist;
		this.artist = artist;
		propertySupport.firePropertyChange("artist", oldArtist, artist);
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		String oldTitle = this.title;
		this.title = title;
		propertySupport.firePropertyChange("title", oldTitle, title);
	}

	public final String getMoreDetails() {
		return moreDetails;
	}

	public final void setMoreDetails(String moreDetails) {
		String oldMoreDetails = this.moreDetails;
		this.moreDetails = moreDetails;
		propertySupport.firePropertyChange("moreDetails", oldMoreDetails, moreDetails);
	}

	public final String getAudio() {
		return audio;
	}

	public final void setAudio(String audio) {
		String oldAudio = this.audio;
		this.audio = audio;
		propertySupport.firePropertyChange("audio", oldAudio, audio);
	}

	public final String getCover() {
		return cover;
	}

	public final void setCover(String cover) {
		String oldCover = this.cover;
		this.cover = cover;
		propertySupport.firePropertyChange("cover", oldCover, cover);
	}

	public final PropertyChangeSupport getPropertySupport() {
		return propertySupport;
	}
}
