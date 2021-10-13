package guesscharts.parser.util;

public class Random {
   private final java.util.Random random;

   public Random() {
      this(new java.util.Random());
   }

   public Random(java.util.Random random) {
      this.random = random;
   }

   /**
    * @return a pseudorandom, uniformly distributed {@code int} value between {@code start} and {@code end} (inclusive)
    */
   public int between(int start, int end) {
      return random.nextInt(end - start + 1) + start; // + 1 because the bound of Java's Random is exclusive
   }
}
