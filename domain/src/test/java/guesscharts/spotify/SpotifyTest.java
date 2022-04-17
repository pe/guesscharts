package guesscharts.spotify;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@Tag("integration")
@EnabledIf("guesscharts.spotify.Spotify#isConfigured")
class SpotifyTest {
   @Test
   void testPreview() {
      String spotifyId = "6rqhFgbbKwnb9MLmUQDhG6";
      String expectedPreviewUrl = "https://p.scdn.co/mp3-preview/";

      assertThatNoException().isThrownBy(() -> {
         String previewUrl = new Spotify().getPreviewUrl(spotifyId);

         assertThat(previewUrl).contains(expectedPreviewUrl);
      });
   }
}
