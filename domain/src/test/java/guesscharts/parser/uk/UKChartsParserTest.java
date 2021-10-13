package guesscharts.parser.uk;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import guesscharts.ParsingError;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.ChartsParser;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Tag("integration")
class UKChartsParserTest {
   private final ChartsParser parser = new UKChartsParser();

   @Test
   void returnsCorrectEntry() throws IOException {
      int year = 2005;
      int position = 2;

      ChartsEntry entry = parser.entryOf(year, position);

      SoftAssertions softly = new SoftAssertions();
      softly.assertThat(entry.year()).isEqualTo(year);
      softly.assertThat(entry.position()).isEqualTo(position);
      softly.assertThat(entry.artist()).isEqualToIgnoringCase("SHAYNE WARD");
      softly.assertThat(entry.title()).isEqualToIgnoringCase("THAT'S MY GOAL");
      softly.assertThat(entry.cover()).contains("http://ecx.images-amazon.com/images/I/61rDSJy3M7L.jpg");
      softly.assertThat(entry.moreDetails()).isEqualToIgnoringCase("http://www.officialcharts.com/search/singles/that's-my-goal/");
      softly.assertThat(entry.audio())
            .contains("https://p.scdn.co/mp3-preview/ce8efc4f406337fa4a96b17e69fd76ea2b750baa?cid=0309a471ed454c91b3d6094cd3d64cd3");
      softly.assertAll();
   }

   @Test
   void throwsExceptionBecausePositionIsNotAvailable() {
      assertThatExceptionOfType(ParsingError.class).isThrownBy(() -> {
         int year = 2005;
         int invalidPosition = 101;

         parser.entryOf(year, invalidPosition);
      });
   }

   @Test
   void throwsExceptionBecausePreviewIsNotAvailable() {
      assertThatExceptionOfType(ParsingError.class).isThrownBy(() -> {
         int year = 2005;
         int positionWithoutPreview = 1;

         parser.entryOf(year, positionWithoutPreview);
      });
   }
}
