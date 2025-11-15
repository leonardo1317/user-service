package io.github.leonardofrs.user_service.infrastructure.transaction;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalExecutor {

  @Transactional
  public <T, R> R execute(Function<T, R> delegate, T input) {
    requireNonNull(delegate, "delegate must not be null");
    requireNonNull(input, "input must not be null");
    return delegate.apply(input);
  }
}
