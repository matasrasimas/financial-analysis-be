package org.example.converter;

import org.example.model.boundary.BoundaryOrganization;
import org.example.model.domain.Organization;

public class OrganizationD2BConverter extends Converter<Organization, BoundaryOrganization> {
    @Override
    protected BoundaryOrganization convert(Organization input) {
        return new BoundaryOrganization(
                input.id(),
                input.title(),
                input.code(),
                input.address()
        );
    }
}
