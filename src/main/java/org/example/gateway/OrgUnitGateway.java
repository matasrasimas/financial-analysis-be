package org.example.gateway;

import org.example.model.domain.OrgUnit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrgUnitGateway {
    List<OrgUnit> retrieve();
    Optional<OrgUnit> retrieveById(UUID id);
    void upsert(OrgUnit input);
    void delete(UUID id);
}
