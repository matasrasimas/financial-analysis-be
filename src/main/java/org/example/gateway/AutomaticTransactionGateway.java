package org.example.gateway;

import org.example.model.domain.AutomaticTransaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutomaticTransactionGateway {
    List<AutomaticTransaction> retrieve();
    Optional<AutomaticTransaction> retrieveById(UUID id);
    void upsert(AutomaticTransaction input);
    void delete(UUID id);
}
