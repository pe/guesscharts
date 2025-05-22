package guesscharts.parser.util;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class Matcher {
    private Matcher() {
    }

    /**
     * Returns the input subsequence captured by the first group of
     * <code>patternWithGroup</code>.
     *
     * @return the first result of {@link java.util.regex.Matcher#group(int)}.
     * @throws IndexOutOfBoundsException If there is no capturing group in the pattern
     * @throws NoSuchElementException    If the pattern doesn't match the input.
     */
    public static String firstMatch(String input, Pattern patternWithGroup) {
        java.util.regex.Matcher matcher = patternWithGroup.matcher(input);
        if (!matcher.find()) {
            throw new NoSuchElementException(patternWithGroup.pattern());
        }
        return matcher.group(1);
    }
}
