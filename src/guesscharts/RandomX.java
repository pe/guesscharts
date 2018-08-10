package guesscharts;

import java.util.Random;

public class RandomX {
	private final Random random;

	public RandomX() {
		this(new Random());
	}

	public RandomX(Random random) {
		this.random = random;
	}

	/**
	 * @return a pseudorandom, uniformly distributed {@code int} value between {@code start} and {@code end} (inclusive)
	 */
	public int nextInt(int start, int end) {
		if (start == end) {
			return start;
		}
		return random.nextInt(end - start + 1) + start; // + 1 because the bound is exclusive
	}
}
