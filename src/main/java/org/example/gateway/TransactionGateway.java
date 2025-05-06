package org.example.gateway;

import org.example.model.domain.Transaction;
import org.example.model.domain.TransactionUpsert;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionGateway {
    List<Transaction> retrieve();
    List<Transaction> retrieveByOrgUnitId(UUID orgUnitId, LocalDate from, LocalDate to);
    List<Transaction> retrieveByOrgId(UUID orgId);
    List<Transaction> retrieveByOrgIdAndDate(UUID orgId, LocalDate from, LocalDate to);
    Optional<Transaction> retrieveById(UUID id);
    Transaction upsert(TransactionUpsert input);
    List<Transaction> upsert(List<TransactionUpsert> input);
    void delete(UUID transactionId);
}
