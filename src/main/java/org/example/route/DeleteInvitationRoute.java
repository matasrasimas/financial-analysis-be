package org.example.route;

import io.reactivex.rxjava3.core.Completable;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.InvitationUseCaseFactory;
import org.example.serialization.json.JsonSerializer;

import java.util.UUID;

import static org.example.common.RouteConstants.INVITATION_ID;

public class DeleteInvitationRoute extends WithoutResponseBodyRoute {
    private final InvitationUseCaseFactory invitationUseCaseFactory;

    public DeleteInvitationRoute(AuthenticationUseCaseFactory authenticationUseCaseFactory,
                                 JsonSerializer jsonSerializer,
                                 JavalinExceptionHandler exceptionHandler,
                                 InvitationUseCaseFactory invitationUseCaseFactory) {
        super(authenticationUseCaseFactory, jsonSerializer, exceptionHandler);
        this.invitationUseCaseFactory = invitationUseCaseFactory;
    }

    @Override
    protected Completable processWithoutBody(RequestWrapper request) {
        UUID invitationId = UUID.fromString(request.getStringPathParam(INVITATION_ID));
        return invitationUseCaseFactory.createDeleteInvitationUseCase().execute(invitationId);
    }
}
