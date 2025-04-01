package org.example.gateway.postgres;

import org.example.gateway.UserGateway;
import org.example.generated.jooq.tables.records.UsersRecord;
import org.example.model.domain.UserCreate;
import org.example.model.domain.UserDTO;
import org.example.model.domain.UserLoginDTO;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Record3;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.example.generated.jooq.Tables.USERS;

public class PostgresUserGateway implements UserGateway {
    private final DSLContext dslContext;

    public PostgresUserGateway(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<UserDTO> retrieve() {
        return dslContext.selectFrom(USERS)
                .fetch(this::buildUser);
    }

    @Override
    public Optional<UserDTO> retrieveById(UUID id) {
        return dslContext.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptional(this::buildUser);
    }

    @Override
    public void create(UserCreate input) {
        dslContext.insertInto(USERS)
                .set(USERS.ID, UUID.randomUUID())
                .set(USERS.FIRST_NAME, input.firstName())
                .set(USERS.LAST_NAME, input.lastName())
                .set(USERS.PHONE_NUMBER, input.phoneNumber().orElse(null))
                .set(USERS.EMAIL_ADDRESS, input.email().orElse(null))
                .set(USERS.HASHED_PASSWORD, BCrypt.hashpw(input.password(), BCrypt.gensalt()))
                .execute();
    }

    @Override
    public void update(UserDTO input) {
        dslContext.update(USERS)
                .set(USERS.FIRST_NAME, input.firstName())
                .set(USERS.LAST_NAME, input.lastName())
                .set(USERS.PHONE_NUMBER, input.phoneNumber().orElse(null))
                .set(USERS.EMAIL_ADDRESS, input.email().orElse(null))
                .where(USERS.ID.eq(input.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(USERS)
                .where(USERS.ID.eq(id))
                .execute();
    }

    @Override
    public Optional<String> login(UserLoginDTO login) {
        Optional<Record3<UUID, String, String>> foundUser =
                dslContext.select(USERS.ID, USERS.EMAIL_ADDRESS, USERS.HASHED_PASSWORD)
                        .from(USERS)
                        .where(USERS.EMAIL_ADDRESS.eq(login.email()))
                        .fetchOptional();
        if (foundUser.isEmpty())
            return Optional.empty();
        if (BCrypt.checkpw(login.password(), foundUser.get().value3()))
            return Optional.of(foundUser.get().value1().toString());
        return Optional.empty();
    }

    private UserDTO buildUser(UsersRecord record) {
        return new UserDTO(
                record.getId(),
                record.getFirstName(),
                record.getLastName(),
                Optional.ofNullable(record.getPhoneNumber()),
                Optional.ofNullable(record.getEmailAddress())
        );
    }
}
