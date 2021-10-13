package guesscharts.parser.util;

import java.util.concurrent.Callable;

public class Retry {
   private final int maxAttempts;

   public Retry(int maxAttempts) {
      if (maxAttempts < 1) {
         throw new IllegalArgumentException();
      }
      this.maxAttempts = maxAttempts;
   }

   public <V> V run(Callable<V> task) {
      int attemptCount = 1;
      while (true) {
         try {
            return task.call();
         } catch (Exception e) {
            if (attemptCount < maxAttempts) {
               System.out.println(e + " retrying...");
            } else {
               throw new RuntimeException(e);
            }
         }
         attemptCount++;
      }
   }
}
