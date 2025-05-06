package org.example.gateway.postgres;

import org.example.gateway.OrganizationGateway;
import org.example.generated.jooq.tables.records.OrganizationsRecord;
import org.example.model.domain.OrgUnit;
import org.example.model.domain.Organization;
import org.example.model.domain.OrganizationCreate;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.example.generated.jooq.Tables.ORGANIZATIONS;
import static org.example.generated.jooq.Tables.ORG_UNITS;

public class PostgresOrganizationGateway implements OrganizationGateway {
    private final DSLContext dslContext;

    public PostgresOrganizationGateway(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<Organization> retrieve() {
        return dslContext.selectFrom(ORGANIZATIONS)
                .fetch(this::buildOrganization);
    }

    @Override
    public Optional<Organization> retrieveById(UUID id) {
        return dslContext.selectFrom(ORGANIZATIONS)
                .where(ORGANIZATIONS.ID.eq(id))
                .fetchOptional(this::buildOrganization);
    }

    @Override
    public List<Organization> retrieveByUserId(UUID userId) {
        return dslContext.selectFrom(ORGANIZATIONS)
                .where(ORGANIZATIONS.USER_ID.eq(userId))
                .fetch(this::buildOrganization);
    }

    @Override
    public Organization create(UUID userId, OrganizationCreate input) {
        UUID orgId = UUID.randomUUID();
        dslContext.insertInto(ORGANIZATIONS)
                .set(ORGANIZATIONS.ID, orgId)
                .set(ORGANIZATIONS.USER_ID, userId)
                .set(ORGANIZATIONS.TITLE, input.title())
                .set(ORGANIZATIONS.CODE, input.code().orElse(null))
                .set(ORGANIZATIONS.ADDRESS, input.address().orElse(null))
                .set(ORGANIZATIONS.YEARLY_GOAL, BigDecimal.valueOf(1500000))
                .execute();

        UUID orgUnitId = UUID.randomUUID();
        dslContext.insertInto(ORG_UNITS)
                .set(ORG_UNITS.ID, orgUnitId)
                .set(ORG_UNITS.ORGANIZATION_ID, orgId)
                .set(ORG_UNITS.TITLE, input.title())
                .set(ORG_UNITS.CODE, input.code().orElse(null))
                .set(ORG_UNITS.ADDRESS, input.address().orElse(null))
                .execute();

        return new Organization(
                orgId,
                userId,
                input.title(),
                input.code(),
                input.address(),
                1500000
        );
    }

    @Override
    public void update(Organization input) {
        dslContext.update(ORGANIZATIONS)
                .set(ORGANIZATIONS.USER_ID, input.userId())
                .set(ORGANIZATIONS.TITLE, input.title())
                .set(ORGANIZATIONS.CODE, input.code().orElse(null))
                .set(ORGANIZATIONS.ADDRESS, input.address().orElse(null))
                .set(ORGANIZATIONS.YEARLY_GOAL, BigDecimal.valueOf(input.yearlyGoal()))
                .where(ORGANIZATIONS.ID.eq(input.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(ORGANIZATIONS)
                .where(ORGANIZATIONS.ID.eq(id))
                .execute();
    }

    private Organization buildOrganization(OrganizationsRecord record) {
        return new Organization(
                record.getId(),
                record.getUserId(),
                record.getTitle(),
                Optional.ofNullable(record.getCode()),
                Optional.ofNullable(record.getAddress()),
                record.getYearlyGoal().doubleValue()
        );
    }
}
