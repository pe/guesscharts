package guesscharts;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Parses http://www.hitparade.ch.
 */
public class SwissChartsParser<T extends ChartEntry> extends ChartsParser<T> {
	private static final String HITPARADE = "http://www.hitparade.ch";
	private static final String JAHRES_HITPARADE = HITPARADE + "/charts/jahreshitparade/";
	private static final String HITPARADE_COVER = HITPARADE + "/cdimag/";
	private static final String HITPARADE_AUDIO = "http://streamd.hitparade.ch/audio/";

	private static final Pattern SONG_ID = Pattern.compile("playAudio2\\('(\\d{7})'\\);return false;");
	private static final Pattern ONCLICK_LINK = Pattern.compile("location.href='(\\S+)';");
	private static final Pattern LAST_URL_PART = Pattern.compile(".*/(\\S+)");

	private Elements elements;

	public SwissChartsParser(Class<T> chartEntry) {
		super(chartEntry);
	}

	@Override
	protected int numberOfChartEntries(int year) {
		try {
			Document doc = Jsoup.connect(JAHRES_HITPARADE + year).get();
			elements = doc.select("tr[xonclick^=location.href='/song/]");

			return elements.size();
		} catch (IOException e) {
			throw new RuntimeException("No chart entries found for " + year, e);
		}
	}

	@Override
	protected String artist(int year, int position) {
		return elements.get(position).select("td:eq(2)").text();
	}

	@Override
	protected String title(int year, int position) {
		return elements.get(position).select("td:eq(3)").text();
	}

	@Override
	protected String detailURL(int year, int position) {
		String onclick = elements.get(position).select("td:eq(2)").attr("onclick");
		return HITPARADE + firstGroupMatch(onclick, ONCLICK_LINK).replace("\\", "");
	}

	@Override
	protected String songURL(int year, int position) {
		String onclick = elements.get(position).select("td:eq(4) a").attr("onclick");
		String songId = firstGroupMatch(onclick, SONG_ID);
		String parentId = songId.substring(0, 3) + "0000";
		return HITPARADE_AUDIO + parentId + "/" + songId + ".mp3";
	}

	@Override
	protected String coverURL(int year, int position) {
		String imgSrc = elements.get(position).select("td:eq(1) img").attr("src");
		return HITPARADE_COVER + firstGroupMatch(imgSrc, LAST_URL_PART);
	}

	/**
	 * @return the first result of {@link Matcher#group(int)}. <code>null</code> in all other cases (
	 *         <code>pattern</code> doesn't match <code>string</code>, no group found)
	 */
	private static String firstGroupMatch(String string, Pattern pattern) {
		Matcher matcher = pattern.matcher(string);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	@Override
	public List<Integer> selectablePositions() {
		return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 30, 40, 50, 60, 70, 80, 90, 100);
	}

	private static final int FIRST_SWISS_CHART_YEAR = 1968;

	@Override
	public List<Integer> selectableYears() {
		return selectableYearsInternal(FIRST_SWISS_CHART_YEAR);
	}
}
