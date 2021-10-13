package guesscharts.parser.swiss;

import guesscharts.ParsingError;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.ChartsParser;
import guesscharts.parser.util.Matcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Parses https://www.hitparade.ch.
 */
public class SwissChartsParser implements ChartsParser {
   private static final String HITPARADE = "http://hitparade.ch";
   private static final String JAHRES_HITPARADE = HITPARADE + "/charts/jahreshitparade/";

   private static final Pattern SONG_ID = Pattern.compile("-(\\d+)$");
   private static final String HITPARADE_AUDIO = "http://tools2.hitparade.ch/tools/audio/";

   private static final Pattern COVER_URL = Pattern.compile("background:url\\('(\\S+)'\\)");

   @Override
   public ChartsEntry getEntry(int year, int position) throws IOException {
      String url = JAHRES_HITPARADE + year;
      Document doc = Jsoup.connect(url).get();
      Elements positions = doc.select("div.chart_title a");

      Element elementAtPosition;
      try {
         elementAtPosition = positions.get(position - 1);
      } catch (IndexOutOfBoundsException e) {
         throw new ParsingError(String.format("Position %s not available at %s", position, url));
      }

      String artist = artist(elementAtPosition);
      String title = title(elementAtPosition);
      String moreDetails = moreDetails(elementAtPosition);
      String audio = audio(moreDetails);

      String cover = cover(doc.select("div.chart_cover").get(position - 1));
      return new ChartsEntry(year, position, artist, title, moreDetails, audio, cover);
   }

   private String artist(Element position) {
      return position.selectFirst("b").ownText();
   }

   private String title(Element position) {
      return position.ownText();
   }

   private String moreDetails(Element position) {
      return HITPARADE + position.attr("href");
   }

   private String audio(String moreDetails) {
      String songId = leftPad(Matcher.firstMatch(moreDetails, SONG_ID), "0000000");
      String parentId = songId.substring(0, 3) + "0000";
      return HITPARADE_AUDIO + parentId + "/" + songId + ".mp3";
   }

   private static String leftPad(String text, String pad) {
      return pad.substring(text.length()) + text;
   }

   private String cover(Element position) {
      final String coverUrl = Matcher.firstMatch(position.attr("style"), COVER_URL);
      return coverUrl.replace("https://", "http://").replace("cover/200/", "cover/big/");
   }
}
