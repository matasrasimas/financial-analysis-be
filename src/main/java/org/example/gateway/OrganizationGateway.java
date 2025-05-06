package org.example.gateway;

import org.example.model.domain.OrgUnit;
import org.example.model.domain.Organization;
import org.example.model.domain.OrganizationCreate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationGateway {
    List<Organization> retrieve();
    List<Organization> retrieveByUserId(UUID userId);
    Optional<Organization> retrieveById(UUID id);
    Organization create(UUID userId, OrganizationCreate input);
    void update(Organization input);
    void delete(UUID id);
}
