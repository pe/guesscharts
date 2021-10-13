package guesscharts.parser.util;

import java.util.concurrent.Callable;

public record Retry(int maxAttempts) {
   public Retry {
      if (maxAttempts < 1) {
         throw new IllegalArgumentException();
      }
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
