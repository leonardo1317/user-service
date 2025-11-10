package io.github.leonardofrs.user_service.infrastructure.mapper;

import java.util.Collection;
import java.util.List;

public interface Mapper<S, T> {

  T map(S source);

  default List<T> map(Collection<S> sources) {
    return sources.stream().map(this::map).toList();
  }
}
