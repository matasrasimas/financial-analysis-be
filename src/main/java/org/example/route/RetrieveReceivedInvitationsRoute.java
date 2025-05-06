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

import static org.example.common.RouteConstants.USER_ID;

public class RetrieveReceivedInvitationsRoute extends AuthedRoute<List<BoundaryInvitation>, List<RestInvitation>> {
    private final InvitationUseCaseFactory invitationUseCaseFactory;
    private final InvitationB2RConverter invitationB2RConverter;


    public RetrieveReceivedInvitationsRoute(AuthenticationUseCaseFactory authUCFactory,
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
        UUID userId = UUID.fromString(request.getStringPathParam(USER_ID));
        return invitationUseCaseFactory.createRetrieveReceivedInvitationsUseCase().execute(userId);
    }
}

