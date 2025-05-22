package guesscharts.parser.uk;

import guesscharts.ParsingError;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.ChartsParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Parses http://www.officialcharts.com.
 */
public class UKChartsParser implements ChartsParser {
    private static final String CHARTS = "http://www.officialcharts.com";
    private static final String END_OF_YEAR_CHARTS = CHARTS + "/charts/end-of-year-singles-chart/";

    @Override
    public ChartsEntry entryOf(int year, int position) throws IOException {
        String url = END_OF_YEAR_CHARTS + year + "0110/37501/";
        Document doc = Jsoup.connect(url).get();
        Elements positions = doc.select("div.chart-item-content");

        Element elementAtPosition;
        try {
            elementAtPosition = positions.get(position - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new ParsingError(String.format("Position %02d not available at %s", position, url));
        }

        String artist = elementAtPosition.selectFirst("a.chart-artist").text();
        String title = elementAtPosition.selectFirst("a.chart-name").ownText();
        String moreDetails = CHARTS + elementAtPosition.selectFirst("a.chart-name").attr("href");
        String cover = elementAtPosition.selectFirst("div.chart-image img").attr("src").replaceFirst("small", "large");
        String audio;
        try {
            audio = elementAtPosition.selectFirst("audio").attr("src");
        } catch (Exception e) {
            throw new ParsingError(String.format("%s has no preview link", elementAtPosition.text()));
        }

        return new ChartsEntry(year, position, artist, title, moreDetails, audio, cover);
    }
}
