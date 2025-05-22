package guesscharts.parser.german;

import guesscharts.ParsingError;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.ChartsParser;
import guesscharts.parser.util.Matcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Parses https://www.offiziellecharts.de/
 */
public class GermanChartsParser implements ChartsParser {
    private static final String CHARTS = "https://www.offiziellecharts.de";
    private static final String JAHRES_CHARTS = CHARTS + "/charts/single-jahr/for-date-";
    private static final String JSON_URL = CHARTS + "/audio/json/s_%s.json";
    private static final String MP3_URL = CHARTS + "/audio/mp3/%s.mp3";

    private static final Pattern CSS_URL = Pattern.compile("url\\('(\\S+)'\\)");

    @Override
    public ChartsEntry entryOf(int year, int position) throws IOException {
        String url = JAHRES_CHARTS + year;
        Document doc = Jsoup.connect(url).get();
        Elements positions = doc.select("table.chart-table tr");

        Element elementAtPosition;
        try {
            elementAtPosition = positions.get(position - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new ParsingError(String.format("Position %s not available at %s", position, url));
        }

        String artist = elementAtPosition.selectFirst("td.ch-info .wrap .info-artist").text();
        String title = elementAtPosition.selectFirst("td.ch-info .wrap .info-title").text();
        String moreDetails = elementAtPosition.selectFirst("td.ch-pos a").absUrl("href");
        String audio = audio(elementAtPosition);
        String cover = cover(elementAtPosition);
        return new ChartsEntry(year, position, artist, title, moreDetails, audio, cover);
    }

    private String audio(Element elementAtPosition) throws IOException {
        String dataTarget;
        try {
            dataTarget = elementAtPosition.selectFirst("td.ch-info .wrap .play-media .play-audio").attr("data-target");
        } catch (NullPointerException e) {
            throw new ParsingError(String.format("%s has no preview link", elementAtPosition.text()));
        }
        String trackId = getTrackId(dataTarget);
        return String.format(MP3_URL, trackId);
    }

    private String getTrackId(String otherTrackId) throws IOException {
        String url = String.format(JSON_URL, otherTrackId);
        String response = Jsoup.connect(url).method(Method.GET).ignoreContentType(true).execute().body();
        JSONObject firstTrack = new JSONArray(response).getJSONObject(0);
        return firstTrack.getString("track_id");
    }

    private String cover(Element elementAtPosition) {
        String style = elementAtPosition.selectFirst("td.ch-cover span").attr("style");
        return CHARTS + Matcher.firstMatch(style, CSS_URL);
    }
}
