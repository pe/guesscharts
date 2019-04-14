package guesscharts.java_fx;

import guesscharts.parser.ChartsEntry;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ChartsEntryPropertyTest {

	@Test
	void updatesAllEntries() {
		ChartsEntryProperty chartsEntryProperty = new ChartsEntryProperty();
		ChartsEntry chartsEntry = new ChartsEntry(1, 2, "artist", "title", "moreDetails", "audio", "cover");

		chartsEntryProperty.update(chartsEntry);

		assertThat(chartsEntryProperty.year.get(), equalTo(chartsEntry.year));
		assertThat(chartsEntryProperty.position.get(), equalTo(chartsEntry.position));
		assertThat(chartsEntryProperty.artist.get(), equalTo(chartsEntry.artist));
		assertThat(chartsEntryProperty.title.get(), equalTo(chartsEntry.title));
		assertThat(chartsEntryProperty.moreDetails.get(), equalTo(chartsEntry.moreDetails));
		assertThat(chartsEntryProperty.audio.get(), equalTo(chartsEntry.audio));
		assertThat(chartsEntryProperty.cover.get(), equalTo(chartsEntry.cover));

	}

}
