package guesscharts.parser;

/**
 * The data of a charts entry.
 */
public record ChartsEntry(int year, int position, String artist, String title, String moreDetails,
                          String audio, String cover) {

   @Override
   public String toString() {
      return String.format("%d/%02d %s by %s", year, position, title, artist);
   }
}
