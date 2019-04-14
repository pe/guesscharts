package guesscharts.java_fx;

import guesscharts.parser.ChartsEntry;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

class ChartsEntryProperty {
	public final IntegerProperty year = new SimpleIntegerProperty();
	public final IntegerProperty position = new SimpleIntegerProperty();
	public final StringProperty artist = new SimpleStringProperty();
	public final StringProperty title = new SimpleStringProperty();
	public final StringProperty moreDetails = new SimpleStringProperty();
	public final StringProperty audio = new SimpleStringProperty();
	public final StringProperty cover = new SimpleStringProperty();

	public void update(ChartsEntry chartsEntry) {
		year.set(chartsEntry.year);
		position.set(chartsEntry.position);
		artist.set(chartsEntry.artist);
		title.set(chartsEntry.title);
		moreDetails.set(chartsEntry.moreDetails);
		audio.set(chartsEntry.audio);
		cover.set(chartsEntry.cover);
	}
}
