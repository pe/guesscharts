package guesscharts.parser.german;

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
class GermanChartsParserTest {
	private final ChartsParser parser = new GermanChartsParser();

	@Test
	void returnsCorrectEntry() {
		assertDoesNotThrow(() -> {
			int year = 1978;
			int position = 1;

			ChartsEntry entry = parser.getEntry(year, position);

			assertThat(entry.year, equalTo(year));
			assertThat(entry.position, equalTo(position));
			assertThat(entry.artist, equalToIgnoringCase("VADER ABRAHAM"));
			assertThat(entry.title, equalToIgnoringCase("DAS LIED DER SCHLÜMPFE"));
			assertThat(entry.cover, equalToIgnoringCase(
					"https://www.offiziellecharts.de/templates/gfktemplate/images/cover/501_s.jpg"));
			assertThat(entry.moreDetails, equalToIgnoringCase("https://www.offiziellecharts.de/titel-details-501"));
			assertThat(entry.audio, equalToIgnoringCase("https://www.offiziellecharts.de/audio/mp3/49112.mp3"));
		});
	}

	@Test
	void throwsExceptionBecausePositionIsNotAvailable() {
		assertThrows(ParsingError.class, () -> {
			int year = 1978;
			int invalidPosition = 51;

			parser.getEntry(year, invalidPosition);
		});
	}

	@Test
	void throwsExceptionBecausePreviewIsNotAvailable() {
		assertThrows(ParsingError.class, () -> {
			int year = 1978;
			int positionWithoutPreview = 10;

			parser.getEntry(year, positionWithoutPreview);
		});
	}
}
