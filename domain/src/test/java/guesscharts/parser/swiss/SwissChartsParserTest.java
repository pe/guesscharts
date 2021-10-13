package guesscharts.parser.swiss;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import guesscharts.ParsingError;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.ChartsParser;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Tag("integration")
class SwissChartsParserTest {
   private final ChartsParser parser = new SwissChartsParser();

   @Test
   void returnsCorrectEntry() throws IOException {
      int year = 1968;
      int position = 1;

      ChartsEntry entry = parser.entryOf(year, position);

      SoftAssertions softly = new SoftAssertions();
      softly.assertThat(entry.year()).isEqualTo(year);
      softly.assertThat(entry.position()).isEqualTo(position);
      softly.assertThat(entry.artist()).isEqualToIgnoringCase("ROLAND W.");
      softly.assertThat(entry.title()).isEqualToIgnoringCase("MONJA");
      softly.assertThat(entry.cover()).isEqualToIgnoringCase("http://media.hitparade.ch/cover/big/roland_w-monja_s.jpg");
      softly.assertThat(entry.moreDetails()).isEqualToIgnoringCase("http://hitparade.ch/song/Roland-W./Monja-1");
      softly.assertThat(entry.audio()).isEqualToIgnoringCase("http://tools2.hitparade.ch/tools/audio/0000000/0000001.mp3");
      softly.assertAll();
   }

   @Test
   void throwsExceptionBecausePositionIsNotAvailable() {
      assertThatExceptionOfType(ParsingError.class).isThrownBy(() -> {
         int year = 1968;
         int invalidPosition = 11;

         parser.entryOf(year, invalidPosition);
      });
   }
}
