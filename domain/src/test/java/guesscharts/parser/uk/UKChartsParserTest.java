package guesscharts.parser.uk;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import guesscharts.ParsingError;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.ChartsParser;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.IOException;

@Tag("integration")
@EnabledIf("guesscharts.spotify.Spotify#isConfigured")
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
        softly.assertThat(entry.cover()).contains("https://m.media-amazon.com/images/I/511lj9QcmBL._SL500_.jpg");
        softly.assertThat(entry.moreDetails()).isEqualToIgnoringCase("http://www.officialcharts.com/songs/shayne-ward-thats-my-goal/");
        softly.assertThat(entry.audio()).contains("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview");
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
