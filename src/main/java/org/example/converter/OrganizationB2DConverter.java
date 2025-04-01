package org.example.converter;

import org.example.model.boundary.BoundaryOrganization;
import org.example.model.domain.Organization;

public class OrganizationB2DConverter extends Converter<BoundaryOrganization, Organization> {
    @Override
    protected Organization convert(BoundaryOrganization input) {
        return new Organization(
                input.id(),
                input.title(),
                input.code(),
                input.address()
        );
    }
}
