package guesscharts.parser.uk;

import guesscharts.ParsingError;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.ChartsParser;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class UKChartsParserTest {
	private final ChartsParser parser = new UKChartsParser();

	@Test
	void returnsCorrectEntry() {
		assertDoesNotThrow(() -> {
			int year = 2005;
			int position = 2;

			ChartsEntry entry = parser.getEntry(year, position);

			assertThat(entry.year, equalTo(year));
			assertThat(entry.position, equalTo(position));
			assertThat(entry.artist, equalToIgnoringCase("SHAYNE WARD"));
			assertThat(entry.title, equalToIgnoringCase("THAT'S MY GOAL"));
			assertThat(entry.cover, containsString(
					"http://ecx.images-amazon.com/images/I/61rDSJy3M7L.jpg"));
			assertThat(entry.moreDetails,
					equalToIgnoringCase("http://www.officialcharts.com/search/singles/that's-my-goal/"));
			assertThat(entry.audio, containsString(
					"https://p.scdn.co/mp3-preview/470f19624d0f34b1e449348e9234e3767f743598?cid=0309a471ed454c91b3d6094cd3d64cd3"));
		});
	}

	@Test
	void throwsExceptionBecausePositionIsNotAvailable() {
		assertThrows(ParsingError.class, () -> {
			int year = 2005;
			int invalidPosition = 101;

			parser.getEntry(year, invalidPosition);
		});
	}

	@Test
	void throwsExceptionBecausePreviewIsNotAvailable() {
		assertThrows(ParsingError.class, () -> {
			int year = 2005;
			int positionWithoutPreview = 1;

			parser.getEntry(year, positionWithoutPreview);
		});
	}
}
