package org.example.converter;

import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.rest.RestOrgUnit;

import java.util.Optional;
import java.util.UUID;

public class OrgUnitR2BConverter extends Converter<RestOrgUnit, BoundaryOrgUnit> {
    @Override
    protected BoundaryOrgUnit convert(RestOrgUnit input) {
        return new BoundaryOrgUnit(
                Optional.ofNullable(input.id()).map(UUID::fromString).orElse(UUID.randomUUID()),
                UUID.fromString(input.orgId()),
                input.title(),
                Optional.ofNullable(input.code()),
                Optional.ofNullable(input.address())
        );
    }
}
