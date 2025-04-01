package org.example.converter;

import org.example.model.boundary.BoundaryAutomaticTransaction;
import org.example.model.rest.RestAutomaticTransaction;

import java.util.Optional;
import java.util.UUID;

public class AutomaticTransactionR2BConverter extends  Converter<RestAutomaticTransaction, BoundaryAutomaticTransaction> {
    @Override
    protected BoundaryAutomaticTransaction convert(RestAutomaticTransaction input) {
        return new BoundaryAutomaticTransaction(
                Optional.ofNullable(input.id()).map(UUID::fromString).orElse(UUID.randomUUID()),
                input.amount(),
                input.title(),
                Optional.ofNullable(input.description()),
                input.latestTransactionDate(),
                input.durationMinutes(),
                input.durationUnit()
        );
    }
}
