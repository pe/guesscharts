package guesscharts.spotify;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Tag("integration")
class SpotifyTest {
	@Test
	void testPreview() {
		String spotifyId = "6rqhFgbbKwnb9MLmUQDhG6";
		String expectedPreviewUrl = "https://p.scdn.co/mp3-preview/4ba756bc1ed5ed7b4c1e281acd30866fa492d0c7";

		assertDoesNotThrow(() -> {
			String previewUrl = new Spotify().getPreviewUrl(spotifyId);

			assertThat(previewUrl, containsString(expectedPreviewUrl));
		});
	}
}
