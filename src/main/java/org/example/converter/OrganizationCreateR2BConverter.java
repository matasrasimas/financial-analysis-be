package org.example.converter;

import org.example.model.boundary.BoundaryOrganizationCreate;
import org.example.model.rest.RestOrganizationCreate;

import java.util.Optional;

public class OrganizationCreateR2BConverter extends Converter<RestOrganizationCreate, BoundaryOrganizationCreate> {
    @Override
    protected BoundaryOrganizationCreate convert(RestOrganizationCreate input) {
        return new BoundaryOrganizationCreate(
                input.title(),
                Optional.ofNullable(input.code()),
                Optional.ofNullable(input.address())
        );
    }
}
