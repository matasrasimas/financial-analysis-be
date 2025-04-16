package org.example.converter;

import org.example.model.boundary.BoundaryOrganizationCreate;
import org.example.model.domain.OrganizationCreate;

public class OrganizationCreateB2DConverter extends Converter<BoundaryOrganizationCreate, OrganizationCreate> {
    @Override
    protected OrganizationCreate convert(BoundaryOrganizationCreate input) {
        return new OrganizationCreate(
                input.title(),
                input.code(),
                input.address()
        );
    }
}
