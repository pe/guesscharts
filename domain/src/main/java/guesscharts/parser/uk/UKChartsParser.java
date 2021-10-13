package guesscharts.parser.uk;

import guesscharts.ParsingError;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.ChartsParser;
import guesscharts.parser.util.Matcher;
import guesscharts.spotify.Spotify;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Parses http://www.officialcharts.com.
 */
public class UKChartsParser implements ChartsParser {
   private static final String CHARTS = "http://www.officialcharts.com";
   private static final String END_OF_YEAR_CHARTS = CHARTS + "/charts/end-of-year-singles-chart/";
   private static final Pattern SPOTIFY_ID = Pattern.compile("https://open.spotify.com/track/(.*)");

   private final Spotify spotify = new Spotify();

   @Override
   public ChartsEntry entryOf(int year, int position) throws IOException {
      String url = END_OF_YEAR_CHARTS + year + "0110/37501/";
      Document doc = Jsoup.connect(url).get();
      Elements positions = doc.select("div.track");

      Element elementAtPosition;
      try {
         elementAtPosition = positions.get(position - 1);
      } catch (IndexOutOfBoundsException e) {
         throw new ParsingError(String.format("Position %02d not available at %s", position, url));
      }

      String artist = elementAtPosition.selectFirst("div.artist").text();
      String title = elementAtPosition.selectFirst("div.title").text();
      String moreDetails = CHARTS + elementAtPosition.selectFirst("div.title a").attr("href");
      String cover = elementAtPosition.selectFirst("div.cover img").attr("src").replaceFirst("small", "large");

      Element spotifyPlayer = doc.selectFirst(String.format("tr.actions-view-listen-%d", position))
            .selectFirst("a.spotify");
      if (spotifyPlayer == null) {
         throw new ParsingError(String.format("%d-%02d has no preview link", year, position));
      }
      String spotifyId = Matcher.firstMatch(spotifyPlayer.attr("href"), SPOTIFY_ID);
      String audio = spotify.getPreviewUrl(spotifyId);

      return new ChartsEntry(year, position, artist, title, moreDetails, audio, cover);
   }
}
