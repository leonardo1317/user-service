package io.github.leonardofrs.user_service.infrastructure.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionalExecutorTest {

  private TransactionalExecutor transactionalExecutor;

  @BeforeEach
  void setUp() {
    transactionalExecutor = new TransactionalExecutor();
  }

  @Test
  @DisplayName("should execute the delegate function with the given input")
  void shouldExecuteDelegate() {
    Function<String, String> delegate = s -> "user-" + s;
    String input = "id";

    String result = transactionalExecutor.execute(delegate, input);

    assertThat(result).isEqualTo("user-id");
  }

  @Test
  @DisplayName("should throw NullPointerException if delegate is null")
  void shouldThrowIfDelegateIsNull() {
    String input = "Test";

    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> transactionalExecutor.execute(null, input)
    );

    assertEquals("delegate cannot be null", exception.getMessage());
  }


  @Test
  @DisplayName("should throw NullPointerException if input is null")
  void shouldThrowIfInputIsNull() {
    Function<String, String> delegate = String::toUpperCase;

    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> transactionalExecutor.execute(delegate, null)
    );

    assertEquals("input cannot be null", exception.getMessage());
  }

  @Test
  @DisplayName("should execute delegate with Integer input and return Integer output")
  void shouldWorkWithOtherTypes() {
    Function<Integer, Integer> delegate = x -> x * 2;
    Integer input = 5;

    Integer result = transactionalExecutor.execute(delegate, input);

    assertThat(result).isEqualTo(10);
  }
}
