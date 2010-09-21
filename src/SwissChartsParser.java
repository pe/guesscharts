import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class SwissChartsParser extends ChartsParser {
	private static final int FIRST_CHART_YEAR = 1968;

	private static final String HITPARADE = "http://www.hitparade.ch/";
	private static final String HITPARADE_AUDIO = "http://stream.hitparade.ch/audio/";

	private String song;
	private URI songDetailsLink;
	private InputStream mp3;
	private String songURL;

	@Override
	public void getNewSong(int yearStart, int yearEnd, int positionStart, int positionEnd) {
		int errors = 0;
		while (true) {
			try {
				int yearRange = yearEnd - yearStart;
				int year = (int) (Math.random() * yearRange + yearStart);
				System.out.println(year);

				NodeList chartEntries = getChartEntriesOfYear(year);
				if (chartEntries == null) {
					throw new RuntimeException("Error while reading the charts for " + year);
				}

				int positionRange = Math.min(positionEnd, chartEntries.size()) - positionStart + 1;
				int position = (int) (Math.random() * positionRange + positionStart);
				System.out.println(position);

				TagNode element = (TagNode) chartEntries.elementAt(position - 1);
				String onclick = element.getAttribute("onclick");
				songDetailsLink = new URI(HITPARADE + onclick.substring(15, onclick.length() - 2));
				System.out.println(songDetailsLink);

				NodeList page = getSongPage(songDetailsLink);

				String songName = page.elementAt(2).getChildren().elementAt(0).getChildren().elementAt(0).getChildren()
						.elementAt(0).toHtml().replace(" (SONG)", "");
				song = "<HTML><BODY>" + songName + " <br> Year: " + year + " <br>Position:  " + position
						+ "</BODY></HTML>";
				songURL = page.toHtml();
				mp3 = getMp3(songURL);

				return;
			} catch (Exception e) {
				e.printStackTrace();

				errors++;
				if (errors > 5) {
					throw new RuntimeException(e);
				} else {
					continue;
				}
			}
		}

	}

	private NodeList getChartEntriesOfYear(int year) throws ParserException {
		NodeFilter filter = new AndFilter(new NodeClassFilter(TableRow.class), new NodeFilter() {
			public boolean accept(Node node) {
				String onclick = ((TagNode) node).getAttribute("onclick");
				if (onclick != null && onclick.endsWith("&cat=s';")) {
					return true;
				} else {
					return false;
				}
			}
		});

		String url = HITPARADE + "year.asp?key=" + year;
		Parser parser = new Parser(url);
		return parser.extractAllNodesThatMatch(filter);
	}

	private NodeList getSongPage(URI url) throws IOException, ParserException {
		NodeFilter filter = new AndFilter(new NodeClassFilter(TableColumn.class), new NodeFilter() {
			public boolean accept(Node node) {
				String onMouseOver = ((TagNode) node).getAttribute("bgcolor");
				if (onMouseOver != null && onMouseOver.equals("#EFEFEF")) {
					return true;
				} else {
					return false;
				}
			}
		});
		Parser parser = new Parser(url.toString());
		NodeList nodes = parser.extractAllNodesThatMatch(filter);
		return nodes.elementAt(0).getChildren();
	}

	public InputStream getMp3(String songPage) throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(songPage));
		String inputLine;
		while ((inputLine = reader.readLine()) != null) {
			if (inputLine.contains("'flashvars'")) {
				String substring = inputLine.substring(44, 59);
				URL mp3Url = new URL(HITPARADE_AUDIO + substring + ".mp3");
				System.out.println(mp3Url);
				return mp3Url.openStream();
			}
		}
		return null;
	}

	@Override
	public String getSong() {
		return song;
	}

	@Override
	public String getSongURL() {
		return songURL;
	}

	@Override
	public URI getSongDetailsLink() {
		return songDetailsLink;
	}

	@Override
	public InputStream getMp3() {
		return mp3;
	}

	@Override
	public Integer[] getPositions() {
		return new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
	}

	@Override
	public Integer[] getYears() {
		int year = Calendar.getInstance().get(Calendar.YEAR) - FIRST_CHART_YEAR;
		Integer[] years = new Integer[year];
		for (int i = 0; i < year; i++) {
			years[i] = FIRST_CHART_YEAR + i;
		}
		return years;
	}

}
