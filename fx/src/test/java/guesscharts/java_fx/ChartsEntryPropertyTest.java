package guesscharts.java_fx;

import guesscharts.parser.ChartsEntry;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ChartsEntryPropertyTest {

	@Test
	void updatesAllEntries() {
		ChartsEntryProperty chartsEntryProperty = new ChartsEntryProperty();
		ChartsEntry newChartsEntry = new ChartsEntry(1, 2, "artist", "title", "moreDetails", "audio", "cover");

		chartsEntryProperty.update(newChartsEntry);

		assertThat(chartsEntryProperty.year.get(), equalTo(newChartsEntry.year));
		assertThat(chartsEntryProperty.position.get(), equalTo(newChartsEntry.position));
		assertThat(chartsEntryProperty.artist.get(), equalTo(newChartsEntry.artist));
		assertThat(chartsEntryProperty.title.get(), equalTo(newChartsEntry.title));
		assertThat(chartsEntryProperty.moreDetails.get(), equalTo(newChartsEntry.moreDetails));
		assertThat(chartsEntryProperty.audio.get(), equalTo(newChartsEntry.audio));
		assertThat(chartsEntryProperty.cover.get(), equalTo(newChartsEntry.cover));

	}

}
