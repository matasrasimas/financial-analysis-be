package org.example.converter;

import java.util.Arrays;
import java.util.Optional;

public class EnumMapper {
    public EnumMapper() {
    }

    public <T extends Enum> Optional<T> map(String input, T... possibleOutputs) {
        return Optional.ofNullable(input).map((i) -> {
            return this.parse(i, possibleOutputs);
        });
    }

    public <T extends Enum> Optional<T> map(Enum input, T... possibleOutputs) {
        return Optional.ofNullable(input).map(Enum::toString).map((i) -> {
            return this.parse(i, possibleOutputs);
        });
    }

    private <T extends Enum> T parse(String input, T[] possibleOutputs) {
        return Arrays.stream(possibleOutputs).filter((enumValue) -> {
            return input.equalsIgnoreCase(enumValue.toString());
        }).findFirst().orElse(null);
    }
}
