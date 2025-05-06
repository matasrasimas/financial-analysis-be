package org.example.factory.implementation;

import org.example.converter.InvitationB2DConverter;
import org.example.converter.InvitationD2BConverter;
import org.example.factory.InvitationUseCaseFactory;
import org.example.gateway.InvitationGateway;
import org.example.usecase.*;
import org.example.usecase.implementation.*;

public class InvitationUseCaseFactoryImpl implements InvitationUseCaseFactory {
    private final InvitationGateway invitationGateway;
    private final InvitationD2BConverter invitationD2BConverter;
    private final InvitationB2DConverter invitationB2DConverter;

    public InvitationUseCaseFactoryImpl(InvitationGateway invitationGateway,
                                        InvitationD2BConverter invitationD2BConverter,
                                        InvitationB2DConverter invitationB2DConverter) {
        this.invitationGateway = invitationGateway;
        this.invitationD2BConverter = invitationD2BConverter;
        this.invitationB2DConverter = invitationB2DConverter;
    }

    @Override
    public RetrieveSentInvitationsUseCase createRetrieveSentInvitationsUseCase() {
        return new RetrieveSentInvitationsInteractor(invitationGateway, invitationD2BConverter);
    }

    @Override
    public RetrieveReceivedInvitationsUseCase createRetrieveReceivedInvitationsUseCase() {
        return new RetrieveReceivedInvitationsInteractor(invitationGateway, invitationD2BConverter);
    }

    @Override
    public RetrieveInvitationsByOrgIdUseCase createRetrieveInvitationsByOrgIdUseCase() {
        return new RetrieveInvitationsByOrgIdInteractor(invitationGateway, invitationD2BConverter);
    }

    @Override
    public UpsertInvitationUseCase createUpsertInvitationUseCase() {
        return new UpsertInvitationInteractor(invitationGateway, invitationB2DConverter, invitationD2BConverter);
    }

    @Override
    public DeleteInvitationUseCase createDeleteInvitationUseCase() {
        return new DeleteInvitationInteractor(invitationGateway);
    }
}
