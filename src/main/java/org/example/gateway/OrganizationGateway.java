package org.example.gateway;

import org.example.model.domain.Organization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationGateway {
    List<Organization> retrieve();
    Optional<Organization> retrieveById(UUID id);
    void upsert(Organization input);
    void delete(UUID id);
}
