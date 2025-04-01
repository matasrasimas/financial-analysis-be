package org.example.gateway.postgres;

import org.example.gateway.OrganizationGateway;
import org.example.generated.jooq.tables.records.OrganizationsRecord;
import org.example.model.domain.Organization;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.example.generated.jooq.Tables.ORGANIZATIONS;

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
    public void upsert(Organization input) {
        dslContext.insertInto(ORGANIZATIONS)
                .set(ORGANIZATIONS.ID, input.id())
                .set(ORGANIZATIONS.TITLE, input.title())
                .set(ORGANIZATIONS.CODE, input.code().orElse(null))
                .set(ORGANIZATIONS.ADDRESS, input.address().orElse(null))
                .onDuplicateKeyUpdate()
                .set(ORGANIZATIONS.TITLE, input.title())
                .set(ORGANIZATIONS.CODE, input.code().orElse(null))
                .set(ORGANIZATIONS.ADDRESS, input.address().orElse(null))
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
                record.getTitle(),
                Optional.ofNullable(record.getCode()),
                Optional.ofNullable(record.getAddress())
        );
    }
}
