package guesscharts.parser.swiss;

import guesscharts.ParsingError;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.ChartsParser;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class SwissChartsParserTest {
	private final ChartsParser parser = new SwissChartsParser();

	@Test
	void returnsCorrectEntry() {
		assertDoesNotThrow(() -> {
			int year = 1968;
			int position = 1;

			ChartsEntry entry = parser.getEntry(year, position);

			assertThat(entry.year, equalTo(year));
			assertThat(entry.position, equalTo(position));
			assertThat(entry.artist, equalToIgnoringCase("ROLAND W."));
			assertThat(entry.title, equalToIgnoringCase("MONJA"));
			assertThat(entry.cover,
					equalToIgnoringCase("https://streamd.hitparade.ch/cdimages/roland_w-monja_s.jpg"));
			assertThat(entry.moreDetails, equalToIgnoringCase("https://hitparade.ch/song/Roland-W./Monja-1"));
			assertThat(entry.audio, equalToIgnoringCase("http://streamd.hitparade.ch/audio/0000000/0000001.mp3"));
		});
	}

	/**
	 * @see SwissChartsParser#artist(org.jsoup.nodes.Element)
	 */
	@Test
	void returnsCorrectArtist() {
		assertDoesNotThrow(() -> {
			int year = 2017;
			int position = 1;

			ChartsEntry entry = parser.getEntry(year, position);

			assertThat(entry.artist, equalToIgnoringCase("Ed Sheeran"));
		});
	}

	@Test
	void throwsExceptionBecausePositionIsNotAvailable() {
		assertThrows(ParsingError.class, () -> {
			int year = 1968;
			int invalidPosition = 11;

			parser.getEntry(year, invalidPosition);
		});
	}
}
