package guesscharts;

import java.util.Random;

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
}
