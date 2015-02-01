package guesscharts;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserUtils;

/**
 * Parses http://www.hitparade.ch.
 */
public class SwissChartsParser extends ChartsParser {
	private static final int FIRST_SWISS_CHART_YEAR = 1968;

	private static final String HITPARADE = "http://www.hitparade.ch";
	private static final String JAHRES_HITPARADE = HITPARADE + "/charts/jahreshitparade/";
	private static final String HITPARADE_AUDIO = "http://streamd.hitparade.ch/audio/";

	private static final Pattern SONG_ID = Pattern.compile("playAudio2\\('(\\d{7})'\\);return false;");
	private static final Pattern ONCLICK_LINK = Pattern.compile("location.href='(\\S+)';");

	private String songDescription;
	private URI songDetailsLink;
	private URL songURL;

	public void nextSong(int yearStart, int yearEnd, int positionStart, int positionEnd) {
		int errors = 0;
		while (true) {
			try {
				int year = randomInt(yearStart, yearEnd);
				System.out.println(year);
				NodeList chartEntriesTable = fetchChartsOfYear(year);
				int position = randomInt(positionStart, Math.min(positionEnd, chartEntriesTable.size()));
				System.out.println(position);
				TableColumn[] columns = fetchColumnsOfPosition(chartEntriesTable, position);

				String artist = columns[2].toPlainTextString().trim();
				String title = columns[3].toPlainTextString().trim();
				songDescription = "<HTML><BODY>" + artist + " – " + title + " <br> Year: " + year + " <br>Position:  "
						+ position + "</BODY></HTML>";

				String link = firstGroupMatch(columns[2].getAttribute("onclick"), ONCLICK_LINK);
				songDetailsLink = new URI(HITPARADE + link.replace("\\", ""));

				String songId = firstGroupMatch(getLinkTag(columns[4]).getAttribute("onclick"), SONG_ID);
				String parentId = songId.substring(0, 3) + "0000";
				songURL = new URL(HITPARADE_AUDIO + parentId + "/" + songId + ".mp3");
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

	private NodeList fetchChartsOfYear(int year) throws ParserException {
		Parser parser = new Parser(JAHRES_HITPARADE + year);
		NodeFilter filter = new AndFilter(new NodeClassFilter(TableRow.class), new NodeFilter() {
			public boolean accept(Node node) {
				String xonclick = ((TagNode) node).getAttribute("xonclick");
				return xonclick != null && xonclick.startsWith("location.href='/song/");
			}
		});
		NodeList chartEntries = parser.extractAllNodesThatMatch(filter);
		if (chartEntries.size() < 1) {
			throw new RuntimeException("No chart entries found for " + year);
		}
		return chartEntries;
	}

	private TableColumn[] fetchColumnsOfPosition(NodeList chartEntriesTable, int position) {
		TableRow row = (TableRow) chartEntriesTable.elementAt(position - 1);
		TableColumn[] columns = row.getColumns();
		if (columns.length != 6) {
			throw new RuntimeException("The table must have exactly 6 columns.");
		}
		return columns;
	}

	/**
	 * @return the first LinkTag found in <code>node</code>.
	 * @throws RuntimeException
	 *             if none or more than one LinkTag's where found
	 */
	private LinkTag getLinkTag(Node node) {
		Node[] links = ParserUtils.findTypeInNode(node, LinkTag.class);
		if (links.length == 1) {
			return (LinkTag) links[0];
		}
		throw new RuntimeException(links.length + " links in the node instead of 1.");
	}

	/**
	 * @return the result of {@link Matcher#group(int)}. <code>null</code> in all other cases (<code>pattern</code>
	 *         doesn't match <code>string</code>, no group found)
	 */
	private String firstGroupMatch(String string, Pattern pattern) {
		Matcher matcher = pattern.matcher(string);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	/**
	 * @return a random int between <code>start</code> and <code>end</code> (including).
	 */
	private int randomInt(int start, int end) {
		int range = end - start + 1;
		return (int) (Math.random() * range + start);
	}

	@Override
	public String getSongDescription() {
		return songDescription;
	}

	@Override
	public URL getSongURL() {
		return songURL;
	}

	@Override
	public URI getSongDetailsLink() {
		return songDetailsLink;
	}

	@Override
	public Integer[] getPositions() {
		return new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
	}

	@Override
	public Integer[] getYears() {
		int year = Calendar.getInstance().get(Calendar.YEAR) - FIRST_SWISS_CHART_YEAR;
		Integer[] years = new Integer[year];
		for (int i = 0; i < year; i++) {
			years[i] = FIRST_SWISS_CHART_YEAR + i;
		}
		return years;
	}
}
