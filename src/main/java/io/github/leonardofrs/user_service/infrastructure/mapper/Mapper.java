package io.github.leonardofrs.user_service.infrastructure.mapper;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;

public interface Mapper<S, T> {

  T map(S source);

  default List<T> map(Collection<S> sources) {
    requireNonNull(sources);
    return sources.stream().map(this::map).toList();
  }
}
