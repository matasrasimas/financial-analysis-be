package org.example.converter;

import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.domain.OrgUnit;

public class OrgUnitB2DConverter extends Converter<BoundaryOrgUnit, OrgUnit> {
    @Override
    protected OrgUnit convert(BoundaryOrgUnit input) {
        return new OrgUnit(
                input.id(),
                input.orgId(),
                input.title(),
                input.code(),
                input.address()
        );
    }
}
