package org.example.gateway.postgres;

import org.example.gateway.TransactionGateway;
import org.example.generated.jooq.tables.records.TransactionsRecord;
import org.example.model.domain.Transaction;
import org.example.model.domain.TransactionUpsert;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<Transaction> retrieveById(UUID id) {
        return dslContext.selectFrom(TRANSACTIONS)
                .where(TRANSACTIONS.ID.eq(id))
                .fetchOptional(this::buildTransaction);
    }

    @Override
    public void upsert(TransactionUpsert input) {
        dslContext.insertInto(TRANSACTIONS)
                .set(TRANSACTIONS.ID, input.id())
                .set(TRANSACTIONS.AMOUNT, BigDecimal.valueOf(input.amount()))
                .set(TRANSACTIONS.TITLE, input.title())
                .set(TRANSACTIONS.DESCRIPTION, input.description().orElse(null))
                .set(TRANSACTIONS.CREATEDAT, LocalDateTime.now())
                .onDuplicateKeyUpdate()
                .set(TRANSACTIONS.AMOUNT, BigDecimal.valueOf(input.amount()))
                .set(TRANSACTIONS.TITLE, input.title())
                .set(TRANSACTIONS.DESCRIPTION, input.description().orElse(null))
                .set(TRANSACTIONS.CREATEDAT, LocalDateTime.now())
                .execute();

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
                record.getAmount().doubleValue(),
                record.getTitle(),
                Optional.ofNullable(record.getDescription()),
                record.getCreatedat().toInstant(ZoneOffset.UTC)
        );
    }
}
