package org.example.gateway.postgres;

import org.example.gateway.AutomaticTransactionGateway;
import org.example.generated.jooq.enums.DurationEnum;
import org.example.generated.jooq.tables.records.AutomaticTransactionsRecord;
import org.example.model.domain.AutomaticTransaction;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.example.generated.jooq.Tables.AUTOMATIC_TRANSACTIONS;

public class PostgresAutomaticTransactionGateway implements AutomaticTransactionGateway {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
    public List<AutomaticTransaction> retrieveByOrgUnitId(UUID orgUnitId) {
        return dslContext.selectFrom(AUTOMATIC_TRANSACTIONS)
                .where(AUTOMATIC_TRANSACTIONS.ORG_UNIT_ID.eq(orgUnitId))
                .fetch(this::buildAutomaticTransaction);
    }

    @Override
    public Optional<AutomaticTransaction> retrieveById(UUID id) {
        return dslContext.selectFrom(AUTOMATIC_TRANSACTIONS)
                .where(AUTOMATIC_TRANSACTIONS.ID.eq(id))
                .fetchOptional(this::buildAutomaticTransaction);
    }

    @Override
    public Map<AutomaticTransaction, LocalDateTime> getLatestDateByAutomaticTransaction() {
        return dslContext.selectFrom(AUTOMATIC_TRANSACTIONS)
                .fetchMap(
                        this::buildAutomaticTransaction,
                        record -> record.getLatestTransactionDate()
                );
    }

    @Override
    public AutomaticTransaction upsert(AutomaticTransaction input) {
        dslContext.insertInto(AUTOMATIC_TRANSACTIONS)
                .set(AUTOMATIC_TRANSACTIONS.ID, input.id())
                .set(AUTOMATIC_TRANSACTIONS.ORG_UNIT_ID, input.orgUnitId())
                .set(AUTOMATIC_TRANSACTIONS.AMOUNT, BigDecimal.valueOf(input.amount()))
                .set(AUTOMATIC_TRANSACTIONS.TITLE, input.title())
                .set(AUTOMATIC_TRANSACTIONS.DESCRIPTION, input.description().orElse(null))
                .set(AUTOMATIC_TRANSACTIONS.LATEST_TRANSACTION_DATE, LocalDateTime.now())
                .set(AUTOMATIC_TRANSACTIONS.DURATION, input.duration())
                .set(AUTOMATIC_TRANSACTIONS.DURATION_UNIT, mapToDurationEnum(input.durationUnit()))
                .onDuplicateKeyUpdate()
                .set(AUTOMATIC_TRANSACTIONS.AMOUNT, BigDecimal.valueOf(input.amount()))
                .set(AUTOMATIC_TRANSACTIONS.TITLE, input.title())
                .set(AUTOMATIC_TRANSACTIONS.DESCRIPTION, input.description().orElse(null))
                .set(AUTOMATIC_TRANSACTIONS.DURATION, input.duration())
                .set(AUTOMATIC_TRANSACTIONS.DURATION_UNIT, mapToDurationEnum(input.durationUnit()))
                .execute();

        return input;
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(AUTOMATIC_TRANSACTIONS)
                .where(AUTOMATIC_TRANSACTIONS.ID.eq(id))
                .execute();
    }

    @Override
    public void updateLatestTransactionDate(UUID transactionId) {
        dslContext.update(AUTOMATIC_TRANSACTIONS)
                .set(AUTOMATIC_TRANSACTIONS.LATEST_TRANSACTION_DATE, LocalDateTime.now())
                .where(AUTOMATIC_TRANSACTIONS.ID.eq(transactionId))
                .execute();
    }

    private AutomaticTransaction buildAutomaticTransaction(AutomaticTransactionsRecord record) {
        long durationMinutes = switch (record.getDurationUnit()) {
            case MINUTES -> record.getDuration();
            case HOURS -> record.getDuration() * 60;
            case DAYS -> record.getDuration() * 1440;
        };
        return new AutomaticTransaction(
                record.getId(),
                record.getOrgUnitId(),
                record.getAmount().doubleValue(),
                record.getTitle(),
                Optional.ofNullable(record.getDescription()),
                record.getDuration(),
                mapToDurationUnit(record.getDurationUnit()),
                record.getLatestTransactionDate().plusMinutes(durationMinutes).format(DATE_FORMATTER)
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
