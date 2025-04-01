package org.example.gateway;

import org.example.model.domain.Transaction;
import org.example.model.domain.TransactionUpsert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionGateway {
    List<Transaction> retrieve();
    Optional<Transaction> retrieveById(UUID id);
    void upsert(TransactionUpsert input);
    void delete(UUID transactionId);
}
