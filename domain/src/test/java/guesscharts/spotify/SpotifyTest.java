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
      String expectedPreviewUrl = "https://p.scdn.co/mp3-preview/4ba756bc1ed5ed7b4c1e281acd30866fa492d0c7";

      assertThatNoException().isThrownBy(() -> {
         String previewUrl = new Spotify().getPreviewUrl(spotifyId);

         assertThat(previewUrl).contains(expectedPreviewUrl);
      });
   }
}
