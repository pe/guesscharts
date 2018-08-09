package guesscharts;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Parses http://www.hitparade.ch.
 */
public class SwissChartsParser extends ChartsParser {
	private static final String HITPARADE = "https://hitparade.ch";
	private static final String JAHRES_HITPARADE = HITPARADE + "/yearurl.asp?key=";
	private static final String HITPARADE_COVER = HITPARADE + "/cdimag/";
	private static final String HITPARADE_AUDIO = "http://streamd.hitparade.ch/audio/";

	private static final Pattern SONG_ID = Pattern.compile("playAudioIpad\\('(\\d{7})'\\);return false;");
	private static final Pattern ONCLICK_LINK = Pattern.compile("location.href='(\\S+)';");
	private static final Pattern LAST_URL_PART = Pattern.compile(".*/(\\S+)");

	private Elements elements;

	@Override
	public int highestPosition(int year) {
		try {
			Document doc = Jsoup.connect(JAHRES_HITPARADE + year).get();
			elements = doc.select("tr[xonclick^=location.href='/song/]");

			return elements.size();
		} catch (IOException e) {
			throw new RuntimeException("No chart entries found for " + year, e);
		}
	}

	@Override
	public String artist(int year, int position) {
		// There are two kinds of structures. Some have a link tag in the table data, some don't.
		Element element = elements.get(position).select("td:eq(2) a").first();
		if (element == null) {
			element = elements.get(position).select("td:eq(2)").first();
		}
		return element.ownText();
	}

	@Override
	public String title(int year, int position) {
		return elements.get(position).select("td:eq(3)").text();
	}

	@Override
	public String detailURL(int year, int position) {
		Element element = elements.get(position);
		String onclick = element.select("td:eq(2)").attr("onclick");
		return HITPARADE + Util.firstMatch(onclick, ONCLICK_LINK).replace("\\", "");
	}

	@Override
	public String songURL(int year, int position) {
		String onclick = elements.get(position).select("td:eq(4) a").attr("onclick");
		String songId = Util.firstMatch(onclick, SONG_ID);
		String parentId = songId.substring(0, 3) + "0000";
		return HITPARADE_AUDIO + parentId + "/" + songId + ".mp3";
	}

	@Override
	public String coverURL(int year, int position) {
		String imgSrc = elements.get(position).select("td:eq(1) img").attr("src");
		return HITPARADE_COVER + Util.firstMatch(imgSrc, LAST_URL_PART);
	}

	@Override
	public List<Integer> availablePositions() {
		return List.of(1, 2, 3, 4, 5, 10, 20, 30, 40, 50, 100);
	}

	public static final int FIRST_YEAR = 1968;

	@Override
	public List<Integer> availableYears() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		return IntStream.range(FIRST_YEAR, currentYear).boxed().collect(Collectors.toList());
	}
}
