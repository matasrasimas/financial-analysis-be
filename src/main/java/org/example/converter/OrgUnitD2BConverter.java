package org.example.converter;

import org.example.model.boundary.BoundaryOrgUnit;
import org.example.model.domain.OrgUnit;

public class OrgUnitD2BConverter extends Converter<OrgUnit, BoundaryOrgUnit> {
    @Override
    protected BoundaryOrgUnit convert(OrgUnit input) {
        return new BoundaryOrgUnit(
                input.id(),
                input.orgId(),
                input.title(),
                input.code(),
                input.address()
        );
    }
}
