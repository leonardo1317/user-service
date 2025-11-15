package io.github.leonardofrs.user_service.infrastructure.mapper;

import static java.util.Objects.requireNonNullElse;

import io.jsonwebtoken.lang.Collections;
import java.util.Collection;
import java.util.List;

public interface Mapper<S, T> {

  T map(S source);

  default List<T> map(Collection<S> sources) {
    Collection<S> safeSources = requireNonNullElse(sources, Collections.emptyList());
    return safeSources.stream().map(this::map).toList();
  }
}
