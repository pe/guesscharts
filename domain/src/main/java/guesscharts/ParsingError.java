package guesscharts;

/**
 * Thrown when there is an error parsing the charts. Most of the time this means that some data (e.g. the audio preview)
 * of an entry is missing.
 */
public class ParsingError extends RuntimeException {
   public ParsingError(String message) {
      super(message);
   }
}
