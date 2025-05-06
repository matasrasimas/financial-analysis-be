package org.example.converter;

import org.example.model.boundary.BoundaryOrganization;
import org.example.model.rest.RestOrganization;

public class OrganizationB2RConverter extends Converter<BoundaryOrganization, RestOrganization> {
    @Override
    protected RestOrganization convert(BoundaryOrganization input) {
        return new RestOrganization(
                input.id().toString(),
                input.userId().toString(),
                input.title(),
                input.code().orElse(null),
                input.address().orElse(null),
                input.yearlyGoal()
        );
    }
}
