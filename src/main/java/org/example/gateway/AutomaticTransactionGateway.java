package org.example.gateway;

import org.example.model.domain.AutomaticTransaction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface AutomaticTransactionGateway {
    List<AutomaticTransaction> retrieve();
    List<AutomaticTransaction> retrieveByOrgUnitId(UUID orgUnitId);
    Optional<AutomaticTransaction> retrieveById(UUID id);
    Map<AutomaticTransaction, LocalDateTime> getLatestDateByAutomaticTransaction();
    AutomaticTransaction upsert(AutomaticTransaction input);
    void delete(UUID id);
    void updateLatestTransactionDate(UUID transactionId);
}
