package org.example.converter;

import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.rest.RestOrgUnit;

public class OrgUnitB2RConverter extends Converter<BoundaryOrgUnit, RestOrgUnit> {
    @Override
    protected RestOrgUnit convert(BoundaryOrgUnit input) {
        return new RestOrgUnit(
                input.id().toString(),
                input.orgId().toString(),
                input.title(),
                input.code().orElse(null),
                input.address().orElse(null)
        );
    }
}
