package org.example.gateway.postgres;

import org.example.gateway.TransactionGateway;
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
                .set(TRANSACTIONS.AMOUNT, BigDecimal.valueOf(input.amount()))
                .set(TRANSACTIONS.TITLE, input.title())
                .set(TRANSACTIONS.DESCRIPTION, input.description().orElse(null))
                .set(TRANSACTIONS.CREATED_AT, input.createdAt())
                .onDuplicateKeyUpdate()
                .set(TRANSACTIONS.AMOUNT, BigDecimal.valueOf(input.amount()))
                .set(TRANSACTIONS.TITLE, input.title())
                .set(TRANSACTIONS.DESCRIPTION, input.description().orElse(null))
                .set(TRANSACTIONS.CREATED_AT, input.createdAt())
                .execute();

        return new Transaction(
                input.id(),
                input.orgUnitId(),
                input.amount(),
                input.title(),
                input.description(),
                input.createdAt()
        );
    }

    @Override
    public List<Transaction> upsert(List<TransactionUpsert> input) {
        input.forEach(transactionUpsert -> dslContext.insertInto(TRANSACTIONS)
                .set(TRANSACTIONS.ID, transactionUpsert.id())
                .set(TRANSACTIONS.ORG_UNIT_ID, transactionUpsert.orgUnitId())
                .set(TRANSACTIONS.AMOUNT, BigDecimal.valueOf(transactionUpsert.amount()))
                .set(TRANSACTIONS.TITLE, transactionUpsert.title())
                .set(TRANSACTIONS.DESCRIPTION, transactionUpsert.description().orElse(null))
                .set(TRANSACTIONS.CREATED_AT, transactionUpsert.createdAt())
                .onDuplicateKeyUpdate()
                .set(TRANSACTIONS.AMOUNT, BigDecimal.valueOf(transactionUpsert.amount()))
                .set(TRANSACTIONS.TITLE, transactionUpsert.title())
                .set(TRANSACTIONS.DESCRIPTION, transactionUpsert.description().orElse(null))
                .set(TRANSACTIONS.CREATED_AT, transactionUpsert.createdAt())
                .execute()
        );

        return input.stream()
                .map(transactionUpsert ->
                        new Transaction(
                                transactionUpsert.id(),
                                transactionUpsert.orgUnitId(),
                                transactionUpsert.amount(),
                                transactionUpsert.title(),
                                transactionUpsert.description(),
                                transactionUpsert.createdAt()))
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
                record.getAmount().doubleValue(),
                record.getTitle(),
                Optional.ofNullable(record.getDescription()),
                record.getCreatedAt()
        );
    }
}
