package guesscharts;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	private Util() {
	}

	/**
	 * @return a random number between <code>start</code> and <code>end</code> (inclusive).
	 */
	public static int randomInt(int start, int end) {
		if (start == end) {
			return start;
		}
		return new Random().nextInt(end - start) + start;
	}

	/**
	 * @return the first result of {@link Matcher#group(int)}. <code>null</code> if no match was found.
	 */
	public static String firstMatch(String string, Pattern patternWithGroup) {
		Matcher matcher = patternWithGroup.matcher(string);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
}
