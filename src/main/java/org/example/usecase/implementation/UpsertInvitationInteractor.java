package org.example.usecase.implementation;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import org.example.converter.InvitationB2DConverter;
import org.example.converter.InvitationD2BConverter;
import org.example.gateway.InvitationGateway;
import org.example.model.boundary.BoundaryInvitation;
import org.example.usecase.UpsertInvitationUseCase;

public class UpsertInvitationInteractor implements UpsertInvitationUseCase {
    private final InvitationGateway invitationGateway;
    private final InvitationB2DConverter invitationB2DConverter;
    private final InvitationD2BConverter invitationD2BConverter;

    public UpsertInvitationInteractor(InvitationGateway invitationGateway,
                                      InvitationB2DConverter invitationB2DConverter,
                                      InvitationD2BConverter invitationD2BConverter) {
        this.invitationGateway = invitationGateway;
        this.invitationB2DConverter = invitationB2DConverter;
        this.invitationD2BConverter = invitationD2BConverter;
    }

    @Override
    public Single<BoundaryInvitation> execute(BoundaryInvitation input) {
        return Single.fromCallable(() -> invitationD2BConverter.process(invitationGateway.upsertInvitation(invitationB2DConverter.process(input).orElseThrow())).orElseThrow());
    }
}
