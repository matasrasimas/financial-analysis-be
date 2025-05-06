package org.example.gateway.postgres;

import org.example.gateway.InvitationGateway;
import org.example.generated.jooq.tables.records.InvitationsRecord;
import org.example.model.domain.Invitation;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

import static org.example.generated.jooq.Tables.INVITATIONS;

public class PostgresInvitationGateway implements InvitationGateway {
    private final DSLContext dslContext;

    public PostgresInvitationGateway(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<Invitation> retrieveSentInvitations(UUID senderId) {
        return dslContext.selectFrom(INVITATIONS)
                .where(INVITATIONS.SENDER_ID.eq(senderId))
                .fetch(this::buildInvitation);
    }

    @Override
    public List<Invitation> retrieveReceivedInvitations(UUID receiverId) {
        return dslContext.selectFrom(INVITATIONS)
                .where(INVITATIONS.RECEIVER_ID.eq(receiverId))
                .fetch(this::buildInvitation);
    }

    @Override
    public List<Invitation> retrieveByOrgId(UUID orgId) {
        return dslContext.selectFrom(INVITATIONS)
                .where(INVITATIONS.ORGANIZATION_ID.eq(orgId))
                .fetch(this::buildInvitation);
    }

    @Override
    public Invitation upsertInvitation(Invitation input) {
        dslContext.insertInto(INVITATIONS)
                .set(INVITATIONS.ID, input.id())
                .set(INVITATIONS.SENDER_ID, input.senderId())
                .set(INVITATIONS.RECEIVER_ID, input.receiverId())
                .set(INVITATIONS.ORGANIZATION_ID, input.organizationId())
                .set(INVITATIONS.CREATED_AT, input.createdAt())
                .set(INVITATIONS.IS_ACCEPTED, input.isAccepted())
                .onDuplicateKeyUpdate()
                .set(INVITATIONS.IS_ACCEPTED, input.isAccepted())
                .execute();
        return input;
    }

    @Override
    public void deleteInvitation(UUID id) {
        dslContext.deleteFrom(INVITATIONS)
                .where(INVITATIONS.ID.eq(id))
                .execute();
    }

    private Invitation buildInvitation(InvitationsRecord record) {
        return new Invitation(
                record.getId(),
                record.getSenderId(),
                record.getReceiverId(),
                record.getOrganizationId(),
                record.getCreatedAt(),
                record.getIsAccepted()
        );
    }
}
