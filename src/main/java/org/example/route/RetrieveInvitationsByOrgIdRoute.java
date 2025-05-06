package org.example.route;

import io.reactivex.rxjava3.core.Single;
import org.example.converter.InvitationB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.InvitationUseCaseFactory;
import org.example.model.boundary.BoundaryInvitation;
import org.example.model.rest.RestInvitation;
import org.example.serialization.json.JsonSerializer;

import java.util.List;
import java.util.UUID;

import static org.example.common.RouteConstants.ORGANIZATION_ID;

public class RetrieveInvitationsByOrgIdRoute extends AuthedRoute<List<BoundaryInvitation>, List<RestInvitation>> {
    private final InvitationUseCaseFactory invitationUseCaseFactory;
    private final InvitationB2RConverter invitationB2RConverter;


    public RetrieveInvitationsByOrgIdRoute(AuthenticationUseCaseFactory authUCFactory,
                                           JsonSerializer jsonSerializer,
                                           JavalinExceptionHandler exceptionHandler,
                                           InvitationUseCaseFactory invitationUseCaseFactory,
                                           InvitationB2RConverter invitationB2RConverter) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.invitationUseCaseFactory = invitationUseCaseFactory;
        this.invitationB2RConverter = invitationB2RConverter;
    }

    @Override
    protected List<RestInvitation> convert(List<BoundaryInvitation> input) {
        return invitationB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryInvitation>> processAuthedRequest(RequestWrapper request) {
        UUID orgId = UUID.fromString(request.getStringPathParam(ORGANIZATION_ID));
        return invitationUseCaseFactory.createRetrieveInvitationsByOrgIdUseCase().execute(orgId);
    }
}


