package org.example.converter;

import org.example.model.boundary.BoundaryOrganization;
import org.example.model.rest.RestOrganization;

import java.util.Optional;
import java.util.UUID;

public class OrganizationR2BConverter extends Converter<RestOrganization, BoundaryOrganization> {
    @Override
    protected BoundaryOrganization convert(RestOrganization input) {
        return new BoundaryOrganization(
                UUID.fromString(input.id()),
                UUID.fromString(input.userId()),
                input.title(),
                Optional.ofNullable(input.code()),
                Optional.ofNullable(input.address())
        );
    }
}
