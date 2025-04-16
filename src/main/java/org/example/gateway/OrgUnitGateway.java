package org.example.gateway;

import org.example.model.domain.OrgUnit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrgUnitGateway {
    List<OrgUnit> retrieve();
    List<OrgUnit> retrieveByOrgId(UUID orgId);
    Optional<OrgUnit> retrieveById(UUID id);
    List<String> retrieveOrgUnitIdsByUserId(UUID userId);
    void upsert(OrgUnit input);
    void delete(UUID id);
}
