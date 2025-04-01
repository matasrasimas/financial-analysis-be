package org.example.gateway.postgres;

import org.example.gateway.AutomaticTransactionGateway;
import org.example.generated.jooq.enums.DurationEnum;
import org.example.generated.jooq.tables.records.AutomaticTransactionsRecord;
import org.example.model.domain.AutomaticTransaction;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.example.generated.jooq.Tables.AUTOMATIC_TRANSACTIONS;

public class PostgresAutomaticTransactionGateway implements AutomaticTransactionGateway {
    private final DSLContext dslContext;

    public PostgresAutomaticTransactionGateway(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<AutomaticTransaction> retrieve() {
        return dslContext.selectFrom(AUTOMATIC_TRANSACTIONS)
                .fetch(this::buildAutomaticTransaction);
    }

    @Override
    public Optional<AutomaticTransaction> retrieveById(UUID id) {
        return Optional.empty();
    }

    @Override
    public void upsert(AutomaticTransaction input) {
        dslContext.insertInto(AUTOMATIC_TRANSACTIONS)
                .set(AUTOMATIC_TRANSACTIONS.ID, input.id())
                .set(AUTOMATIC_TRANSACTIONS.AMOUNT, BigDecimal.valueOf(input.amount()))
                .set(AUTOMATIC_TRANSACTIONS.TITLE, input.title())
                .set(AUTOMATIC_TRANSACTIONS.DESCRIPTION, input.description().orElse(null))
                .set(AUTOMATIC_TRANSACTIONS.LATEST_TRANSACTION_DATE, LocalDateTime.ofInstant(input.latestTransactionDate(), ZoneOffset.UTC))
                .set(AUTOMATIC_TRANSACTIONS.DURATION_MINUTES, input.durationMinutes())
                .set(AUTOMATIC_TRANSACTIONS.DURATION_UNIT, mapToDurationEnum(input.durationUnit()))
                .onDuplicateKeyUpdate()
                .set(AUTOMATIC_TRANSACTIONS.AMOUNT, BigDecimal.valueOf(input.amount()))
                .set(AUTOMATIC_TRANSACTIONS.TITLE, input.title())
                .set(AUTOMATIC_TRANSACTIONS.DESCRIPTION, input.description().orElse(null))
                .set(AUTOMATIC_TRANSACTIONS.LATEST_TRANSACTION_DATE, LocalDateTime.ofInstant(input.latestTransactionDate(), ZoneOffset.UTC))
                .set(AUTOMATIC_TRANSACTIONS.DURATION_MINUTES, input.durationMinutes())
                .set(AUTOMATIC_TRANSACTIONS.DURATION_UNIT, mapToDurationEnum(input.durationUnit()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(AUTOMATIC_TRANSACTIONS)
                .where(AUTOMATIC_TRANSACTIONS.ID.eq(id))
                .execute();
    }

    private AutomaticTransaction buildAutomaticTransaction(AutomaticTransactionsRecord record) {
        return new AutomaticTransaction(
                record.getId(),
                record.getAmount().doubleValue(),
                record.getTitle(),
                Optional.ofNullable(record.getDescription()),
                record.getLatestTransactionDate().toInstant(ZoneOffset.UTC),
                record.getDurationMinutes(),
                mapToDurationUnit(record.getDurationUnit())
        );
    }

    private AutomaticTransaction.DurationUnit mapToDurationUnit(DurationEnum input) {
        return switch (input) {
            case MINUTES -> AutomaticTransaction.DurationUnit.MINUTES;
            case HOURS -> AutomaticTransaction.DurationUnit.HOURS;
            case DAYS -> AutomaticTransaction.DurationUnit.DAYS;
        };
    }

    private DurationEnum mapToDurationEnum(AutomaticTransaction.DurationUnit input) {
        return switch (input) {
            case MINUTES -> DurationEnum.MINUTES;
            case HOURS -> DurationEnum.HOURS;
            case DAYS -> DurationEnum.DAYS;
        };
    }
}
