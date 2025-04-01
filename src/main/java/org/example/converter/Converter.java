package org.example.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Converter<I, O> {
    public Converter() {
    }

    protected abstract O convert(I var1);

    public Optional<O> process(I input) {
        return Objects.isNull(input) ? Optional.empty() : Optional.ofNullable(this.convert(input));
    }

    public List<O> process(List<I> input) {
        return Objects.isNull(input) ? Collections.emptyList() : (List)this.processCollection(input).collect(Collectors.toList());
    }

    public Set<O> process(Set<I> input) {
        return Objects.isNull(input) ? Collections.emptySet() : (Set)this.processCollection(input).collect(Collectors.toSet());
    }

    private Stream<O> processCollection(Collection<I> input) {
        return input.stream().map(this::process).filter(Optional::isPresent).map(Optional::get);
    }
}
