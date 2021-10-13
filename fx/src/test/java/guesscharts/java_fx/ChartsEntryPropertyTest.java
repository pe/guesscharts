package guesscharts.java_fx;

import guesscharts.parser.ChartsEntry;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class ChartsEntryPropertyTest {

   @Test
   void updatesAllEntries() {
      ChartsEntryProperty chartsEntryProperty = new ChartsEntryProperty();
      ChartsEntry newChartsEntry = new ChartsEntry(1, 2, "artist", "title", "moreDetails", "audio", "cover");

      chartsEntryProperty.update(newChartsEntry);

      SoftAssertions softly = new SoftAssertions();
      softly.assertThat(chartsEntryProperty.year.get()).isEqualTo(newChartsEntry.year);
      softly.assertThat(chartsEntryProperty.position.get()).isEqualTo(newChartsEntry.position);
      softly.assertThat(chartsEntryProperty.artist.get()).isEqualTo(newChartsEntry.artist);
      softly.assertThat(chartsEntryProperty.title.get()).isEqualTo(newChartsEntry.title);
      softly.assertThat(chartsEntryProperty.moreDetails.get()).isEqualTo(newChartsEntry.moreDetails);
      softly.assertThat(chartsEntryProperty.audio.get()).isEqualTo(newChartsEntry.audio);
      softly.assertThat(chartsEntryProperty.cover.get()).isEqualTo(newChartsEntry.cover);
      softly.assertAll();
   }
}
