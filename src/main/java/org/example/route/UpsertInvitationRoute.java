package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import org.example.converter.InvitationB2RConverter;
import org.example.converter.InvitationR2BConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.InvitationUseCaseFactory;
import org.example.model.boundary.BoundaryInvitation;
import org.example.model.rest.RestInvitation;
import org.example.serialization.json.JsonSerializer;

public class UpsertInvitationRoute extends AuthedRoute<BoundaryInvitation, RestInvitation> {
    private final InvitationUseCaseFactory invitationUseCaseFactory;
    private final InvitationR2BConverter invitationR2BConverter;
    private final InvitationB2RConverter invitationB2RConverter;

    public UpsertInvitationRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                 JsonSerializer jsonSerializer,
                                 JavalinExceptionHandler exceptionHandler,
                                 InvitationUseCaseFactory invitationUseCaseFactory,
                                 InvitationR2BConverter invitationR2BConverter,
                                 InvitationB2RConverter invitationB2RConverter) {
        super(authenticationUseCaseFactory, jsonSerializer, null, exceptionHandler);
        this.invitationUseCaseFactory = invitationUseCaseFactory;
        this.invitationR2BConverter = invitationR2BConverter;
        this.invitationB2RConverter = invitationB2RConverter;
    }

    @Override
    protected Single<BoundaryInvitation> processAuthedRequest(RequestWrapper request) {
        RestInvitation reqBody = request.deserializeBody(RestInvitation.class);
        BoundaryInvitation boundaryInvitation = invitationR2BConverter.process(reqBody).orElseThrow();

        return invitationUseCaseFactory.createUpsertInvitationUseCase().execute(boundaryInvitation);
    }
}
