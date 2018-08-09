package guesscharts.javaFx;

import guesscharts.ChartsEntry;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;

public class PropertyChartEntry {
	public final IntegerProperty year = new ReadOnlyIntegerWrapper();
	public final IntegerProperty position = new ReadOnlyIntegerWrapper();
	public final StringProperty artist = new ReadOnlyStringWrapper();
	public final StringProperty title = new ReadOnlyStringWrapper();
	public final StringProperty moreDetails = new ReadOnlyStringWrapper();
	public final StringProperty audio = new ReadOnlyStringWrapper();
	public final StringProperty cover = new ReadOnlyStringWrapper();

	public void update(ChartsEntry chartEntry) {
		year.set(chartEntry.year);
		position.set(chartEntry.position);
		artist.set(chartEntry.artist);
		title.set(chartEntry.title);
		moreDetails.set(chartEntry.detailURL);
		audio.set(chartEntry.songURL);
		cover.set(chartEntry.coverURL);
	}
}
