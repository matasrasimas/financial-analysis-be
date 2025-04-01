package org.example.gateway.postgres;

import org.example.gateway.OrgUnitGateway;
import org.example.generated.jooq.tables.records.OrgUnitsRecord;
import org.example.model.domain.OrgUnit;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.example.generated.jooq.Tables.ORG_UNITS;

public class PostgresOrgUnitGateway implements OrgUnitGateway {
    private final DSLContext dslContext;

    public PostgresOrgUnitGateway(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<OrgUnit> retrieve() {
        return dslContext.selectFrom(ORG_UNITS)
                .fetch(this::buildOrgUnit);
    }


    @Override
    public Optional<OrgUnit> retrieveById(UUID id) {
        return dslContext.selectFrom(ORG_UNITS)
                .where(ORG_UNITS.ID.eq(id))
                .fetchOptional(this::buildOrgUnit);
    }

    @Override
    public void upsert(OrgUnit input) {
        dslContext.insertInto(ORG_UNITS)
                .set(ORG_UNITS.ID, input.id())
                .set(ORG_UNITS.ORGANIZATION_ID, input.orgId())
                .set(ORG_UNITS.TITLE, input.title())
                .set(ORG_UNITS.CODE, input.code().orElse(null))
                .set(ORG_UNITS.ADDRESS, input.address().orElse(null))
                .onDuplicateKeyUpdate()
                .set(ORG_UNITS.ORGANIZATION_ID, input.orgId())
                .set(ORG_UNITS.TITLE, input.title())
                .set(ORG_UNITS.CODE, input.code().orElse(null))
                .set(ORG_UNITS.ADDRESS, input.address().orElse(null))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(ORG_UNITS)
                .where(ORG_UNITS.ID.eq(id))
                .execute();
    }

    private OrgUnit buildOrgUnit(OrgUnitsRecord record) {
        return new OrgUnit(
                record.getId(),
                record.getOrganizationId(),
                record.getTitle(),
                Optional.ofNullable(record.getCode()),
                Optional.ofNullable(record.getAddress())
        );
    }
}
