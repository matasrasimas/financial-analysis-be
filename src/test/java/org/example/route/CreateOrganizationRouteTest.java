package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.OrganizationB2RConverter;
import org.example.converter.OrganizationCreateR2BConverter;
import org.example.factory.OrganizationUseCaseFactory;
import org.example.model.boundary.BoundaryOrganization;
import org.example.model.boundary.BoundaryOrganizationCreate;
import org.example.model.rest.RestOrganizationCreate;
import org.example.usecase.CreateOrganizationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CreateOrganizationRouteTest {
    @Mock
    private OrganizationUseCaseFactory orgUCF;
    @Mock
    private CreateOrganizationUseCase useCase;
    @Mock
    private OrganizationCreateR2BConverter organizationCreateR2BConverter;
    @Mock
    private OrganizationB2RConverter organizationB2RConverter;
    @Mock
    private RequestWrapper request;

    private CreateOrganizationRoute route;

    @BeforeEach
    void setUp() {
        route = new CreateOrganizationRoute(null, null, null, orgUCF, organizationCreateR2BConverter, organizationB2RConverter);
    }

    @Test
    void upsertViaUseCase() {
        UUID requestorId = UUID.randomUUID();
        when(request.getRequestorId()).thenReturn(requestorId.toString());
        RestOrganizationCreate restOrganizationCreate = mock(RestOrganizationCreate.class);
        when(request.deserializeBody(RestOrganizationCreate.class)).thenReturn(restOrganizationCreate);
        BoundaryOrganizationCreate boundaryOrganizationCreate = mock(BoundaryOrganizationCreate.class);
        BoundaryOrganization boundaryOrganization = mock(BoundaryOrganization.class);
        when(organizationCreateR2BConverter.process(restOrganizationCreate)).thenReturn(Optional.of(boundaryOrganizationCreate));
        when(orgUCF.createCreateOrganizationUseCase()).thenReturn(useCase);
        when(useCase.execute(requestorId, boundaryOrganizationCreate)).thenReturn(Single.just(boundaryOrganization));

        route.processAuthedRequest(request).test()
                .assertComplete()
                .assertValue(boundaryOrganization);
    }
}
