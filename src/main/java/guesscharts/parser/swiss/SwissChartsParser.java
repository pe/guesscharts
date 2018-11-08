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
	private static final String HITPARADE = "https://hitparade.ch";
	private static final String JAHRES_HITPARADE = HITPARADE + "/charts/jahreshitparade/";
	private static final String HITPARADE_COVER = "https://streamd.hitparade.ch/cdimages/";
	private static final String HITPARADE_AUDIO = "http://streamd.hitparade.ch/audio/";

	private static final Pattern SONG_ID = Pattern.compile("playAudioIpad\\('(\\d{7})'\\);return false;");
	private static final Pattern ONCLICK_LINK = Pattern.compile("location.href='(\\S+)';");
	private static final Pattern LAST_URL_PART = Pattern.compile(".*/(\\S+)");

	@Override
	public ChartsEntry getEntry(int year, int position) throws IOException {
		String url = JAHRES_HITPARADE + year;
		Document doc = Jsoup.connect(url).get();
		Elements positions = doc.select("tr.charts[xonclick*=/song/]");

		Element elementAtPosition;
		try {
			elementAtPosition = positions.get(position - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new ParsingError(String.format("Position %s not available at %s", position, url));
		}

		String artist = artist(elementAtPosition);
		String title = title(elementAtPosition);
		String moreDetails = moreDetails(elementAtPosition);
		String audio = audio(elementAtPosition);
		String cover = cover(elementAtPosition);
		return new ChartsEntry(year, position, artist, title, moreDetails, audio, cover);
	}

	private String artist(Element position) {
		// There are two kinds of structures. Some have a link tag in the table data,
		// some don't.
		Element element = position.select("td:eq(2) a").first();
		if (element == null) {
			element = position.select("td:eq(2)").first();
		}
		return element.ownText();
	}

	private String title(Element position) {
		return position.select("td:eq(3)").text();
	}

	private String moreDetails(Element position) {
		String onclick = position.select("td:eq(2)").attr("onclick");
		return HITPARADE + Matcher.firstMatch(onclick, ONCLICK_LINK).replace("\\", "");
	}

	private String audio(Element position) {
		String onclick = position.select("td:eq(4) a").attr("onclick");
		String songId = Matcher.firstMatch(onclick, SONG_ID);
		String parentId = songId.substring(0, 3) + "0000";
		return HITPARADE_AUDIO + parentId + "/" + songId + ".mp3";
	}

	private String cover(Element position) {
		String imgSrc = position.select("td:eq(1) img").attr("src");
		return HITPARADE_COVER + Matcher.firstMatch(imgSrc, LAST_URL_PART);
	}
}
