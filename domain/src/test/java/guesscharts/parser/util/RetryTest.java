package guesscharts.parser.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

class RetryTest {
   @Test
   void exceptionThrownWhenMaxAttemptsSmallerThan1() {
      int maxAttempts = 0;

      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Retry(maxAttempts));
   }

   @Test
   void returningEntryFromUnderlyingCallable() {
      int maxAttempts = 1;
      String expected = "return value";
      Callable<String> returnExpected = () -> expected;

      String actual = new Retry(maxAttempts).run(returnExpected);

      assertThat(actual).isEqualTo(expected);
   }

   @Test
   void exceptionOfFirstCallSuppressedWhenTryingTwice() {
      int maxAttempts = 2;
      Callable<Void> throwException = ThrowException.forNCalls(1);

      assertThatNoException().isThrownBy(() -> new Retry(maxAttempts).run(throwException));
   }

   @Test
   void exceptionOfSecondCallThrownWhenTryingTwice() {
      int maxAttempts = 2;
      Callable<Void> throwException = ThrowException.forNCalls(2);

      assertThatExceptionOfType(Exception.class).isThrownBy(() -> new Retry(maxAttempts).run(throwException));
   }

   private static class ThrowException implements Callable<Void> {
      private final int numberOfCalls;
      private int callCount = 0;

      private ThrowException(int numberOfCalls) {
         this.numberOfCalls = numberOfCalls;
      }

      static ThrowException forNCalls(int numberOfCalls) {
         return new ThrowException(numberOfCalls);
      }

      @Override
      public Void call() throws Exception {
         callCount++;
         if (callCount <= numberOfCalls) {
            throw new Exception();
         } else {
            return null;
         }
      }
   }
}
