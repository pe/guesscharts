package guesscharts.parser.german;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import guesscharts.ParsingError;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.ChartsParser;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
class GermanChartsParserTest {
   private final ChartsParser parser = new GermanChartsParser();

   @Test
   void returnsCorrectEntry() {
      assertThatNoException().isThrownBy(() -> {
         int year = 1978;
         int position = 1;

         ChartsEntry entry = parser.entryOf(year, position);

         SoftAssertions softly = new SoftAssertions();
         softly.assertThat(entry.year()).isEqualTo(year);
         softly.assertThat(entry.position()).isEqualTo(position);
         softly.assertThat(entry.artist()).isEqualToIgnoringCase("VADER ABRAHAM");
         softly.assertThat(entry.title()).isEqualToIgnoringCase("DAS LIED DER SCHLÜMPFE");
         softly.assertThat(entry.cover())
               .isEqualToIgnoringCase("https://www.offiziellecharts.de/templates/gfktemplate/images/cover/501_s.jpg");
         softly.assertThat(entry.moreDetails()).isEqualToIgnoringCase("https://www.offiziellecharts.de/titel-details-501");
         softly.assertThat(entry.audio()).isEqualToIgnoringCase("https://www.offiziellecharts.de/audio/mp3/49112.mp3");
         softly.assertAll();
      });
   }

   @Test
   void throwsExceptionBecausePositionIsNotAvailable() {
      assertThatExceptionOfType(ParsingError.class).isThrownBy(() -> {
         int year = 1978;
         int invalidPosition = 51;

         parser.entryOf(year, invalidPosition);
      });
   }

   @Test
   void throwsExceptionBecausePreviewIsNotAvailable() {
      assertThatExceptionOfType(ParsingError.class).isThrownBy(() -> {
         int year = 1978;
         int positionWithoutPreview = 10;

         parser.entryOf(year, positionWithoutPreview);
      });
   }
}
