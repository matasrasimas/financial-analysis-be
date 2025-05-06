package org.example.gateway.postgres;

import org.example.gateway.TransactionGateway;
import org.example.generated.jooq.tables.records.OrgUnitsRecord;
import org.example.generated.jooq.tables.records.TransactionsRecord;
import org.example.model.domain.Transaction;
import org.example.model.domain.TransactionUpsert;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.example.generated.jooq.Tables.ORG_UNITS;
import static org.example.generated.jooq.Tables.TRANSACTIONS;

public class PostgresTransactionGateway implements TransactionGateway {
    private final DSLContext dslContext;

    public PostgresTransactionGateway(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<Transaction> retrieve() {
        return dslContext.selectFrom(TRANSACTIONS)
                .fetch(this::buildTransaction);
    }

    @Override
    public List<Transaction> retrieveByOrgUnitId(UUID orgUnitId, LocalDate from, LocalDate to) {
        return dslContext.selectFrom(TRANSACTIONS)
                .where(TRANSACTIONS.ORG_UNIT_ID.eq(orgUnitId))
                .and(TRANSACTIONS.CREATED_AT.between(from, to))
                .fetch(this::buildTransaction);
    }

    @Override
    public List<Transaction> retrieveByOrgId(UUID orgId) {
        List<UUID> orgUnitIds = dslContext.selectFrom(ORG_UNITS)
                .where(ORG_UNITS.ORGANIZATION_ID.eq(orgId))
                .fetch(OrgUnitsRecord::getId);
        return dslContext.selectFrom(TRANSACTIONS)
                .where(TRANSACTIONS.ORG_UNIT_ID.in(orgUnitIds))
                .fetch(this::buildTransaction);
    }

    @Override
    public List<Transaction> retrieveByOrgIdAndDate(UUID orgId, LocalDate from, LocalDate to) {
        List<UUID> orgUnitIds = dslContext.selectFrom(ORG_UNITS)
                .where(ORG_UNITS.ORGANIZATION_ID.eq(orgId))
                .fetch(OrgUnitsRecord::getId);
        return dslContext.selectFrom(TRANSACTIONS)
                .where(TRANSACTIONS.ORG_UNIT_ID.in(orgUnitIds))
                .and(TRANSACTIONS.CREATED_AT.between(from, to))
                .fetch(this::buildTransaction);
    }

    @Override
    public Optional<Transaction> retrieveById(UUID id) {
        return dslContext.selectFrom(TRANSACTIONS)
                .where(TRANSACTIONS.ID.eq(id))
                .fetchOptional(this::buildTransaction);
    }

    @Override
    public Transaction upsert(TransactionUpsert input) {
        dslContext.insertInto(TRANSACTIONS)
                .set(TRANSACTIONS.ID, input.id())
                .set(TRANSACTIONS.ORG_UNIT_ID, input.orgUnitId())
                .set(TRANSACTIONS.USER_ID, input.userId())
                .set(TRANSACTIONS.USER_ID, input.userId())
                .set(TRANSACTIONS.AMOUNT, BigDecimal.valueOf(input.amount()))
                .set(TRANSACTIONS.TITLE, input.title())
                .set(TRANSACTIONS.CREATED_AT, input.createdAt())
                .set(TRANSACTIONS.IS_LOCKED, input.isLocked())
                .onDuplicateKeyUpdate()
                .set(TRANSACTIONS.AMOUNT, BigDecimal.valueOf(input.amount()))
                .set(TRANSACTIONS.TITLE, input.title())
                .set(TRANSACTIONS.CREATED_AT, input.createdAt())
                .set(TRANSACTIONS.IS_LOCKED, input.isLocked())
                .execute();

        return new Transaction(
                input.id(),
                input.orgUnitId(),
                null,
                input.amount(),
                input.title(),
                input.createdAt(),
                input.isLocked()
        );
    }

    @Override
    public List<Transaction> upsert(List<TransactionUpsert> input) {
        input.forEach(transactionUpsert -> dslContext.insertInto(TRANSACTIONS)
                        .set(TRANSACTIONS.ID, transactionUpsert.id())
                        .set(TRANSACTIONS.ORG_UNIT_ID, transactionUpsert.orgUnitId())
                        .set(TRANSACTIONS.USER_ID, transactionUpsert.userId())
                        .set(TRANSACTIONS.AMOUNT, BigDecimal.valueOf(transactionUpsert.amount()))
                        .set(TRANSACTIONS.TITLE, transactionUpsert.title())
                        .set(TRANSACTIONS.CREATED_AT, transactionUpsert.createdAt())
                        .set(TRANSACTIONS.IS_LOCKED, transactionUpsert.isLocked())
                        .onDuplicateKeyUpdate()
                        .set(TRANSACTIONS.AMOUNT, BigDecimal.valueOf(transactionUpsert.amount()))
                        .set(TRANSACTIONS.TITLE, transactionUpsert.title())
                        .set(TRANSACTIONS.CREATED_AT, transactionUpsert.createdAt())
                        .set(TRANSACTIONS.IS_LOCKED, transactionUpsert.isLocked())
                        .execute()
        );

        return input.stream()
                .map(transactionUpsert ->
                        new Transaction(
                                transactionUpsert.id(),
                                transactionUpsert.orgUnitId(),
                                transactionUpsert.userId(),
                                transactionUpsert.amount(),
                                transactionUpsert.title(),
                                transactionUpsert.createdAt(),
                                transactionUpsert.isLocked()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID transactionId) {
        dslContext.deleteFrom(TRANSACTIONS)
                .where(TRANSACTIONS.ID.eq(transactionId))
                .execute();
    }

    private Transaction buildTransaction(TransactionsRecord record) {
        return new Transaction(
                record.getId(),
                record.getOrgUnitId(),
                record.getUserId(),
                record.getAmount().doubleValue(),
                record.getTitle(),
                record.getCreatedAt(),
                record.getIsLocked()
        );
    }
}
