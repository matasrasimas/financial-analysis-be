package org.example.gateway;

import org.example.model.domain.OrgUnit;
import org.example.model.domain.Organization;
import org.example.model.domain.OrganizationCreate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationGateway {
    List<Organization> retrieve();
    Optional<Organization> retrieveById(UUID id);
    Optional<Organization> retrieveByUserId(UUID userId);
    OrgUnit create(UUID userId, OrganizationCreate input);
    void update(Organization input);
    void delete(UUID id);
}
