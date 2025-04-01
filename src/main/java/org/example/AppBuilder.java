package org.example;

import io.javalin.Javalin;
import org.example.converter.*;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.*;
import org.example.route.*;
import org.example.serialization.json.JsonSerializer;
import org.example.usecase.DeleteAutomaticTransactionRoute;

import static io.javalin.apibuilder.ApiBuilder.*;
import static org.example.common.RouteConstants.*;

public class AppBuilder {
    private final AuthenticationUseCaseFactory authUCFactory;
    private final TransactionUseCaseFactory transactionUCFactory;
    private final JsonSerializer jsonSerializer;
    private final JavalinExceptionHandler exceptionHandler;
    private final AutomaticTransactionUseCaseFactory automaticTransactionUCFactory;
    private final OrganizationUseCaseFactory organizationUCFactory;
    private final OrgUnitUseCaseFactory orgUnitUCFactory;
    private final UserUseCaseFactory userUCFactory;

    public AppBuilder(AuthenticationUseCaseFactory authUCFactory,
                      TransactionUseCaseFactory transactionUCFactory,
                      JsonSerializer jsonSerializer,
                      JavalinExceptionHandler exceptionHandler,
                      AutomaticTransactionUseCaseFactory automaticTransactionUCFactory,
                      OrganizationUseCaseFactory organizationUCFactory,
                      OrgUnitUseCaseFactory orgUnitUCFactory,
                      UserUseCaseFactory userUCFactory) {
        this.authUCFactory = authUCFactory;
        this.transactionUCFactory = transactionUCFactory;
        this.jsonSerializer = jsonSerializer;
        this.exceptionHandler = exceptionHandler;
        this.automaticTransactionUCFactory = automaticTransactionUCFactory;
        this.organizationUCFactory = organizationUCFactory;
        this.orgUnitUCFactory = orgUnitUCFactory;
        this.userUCFactory = userUCFactory;
    }

    public Javalin build() {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> {
                it.allowCredentials = true;
                it.reflectClientOrigin = true;
            }));
            config.http.defaultContentType = "application/json";

            config.router.apiBuilder(() -> path(API_PATH, this::setApiRoutes));
        });

        app.start(8080);
        return app;
    }

    private void setApiRoutes() {
        post(LOGIN_PATH, context -> new LoginRoute(authUCFactory, new UserLoginR2BConverter()).processRequest(context));
        path(TRANSACTIONS_PATH, this::createTransactionsRoutes);
        path(AUTOMATIC_TRANSACTIONS_PATH, this::createAutomaticTransactionsRoutes);
        path(ORGANIZATIONS_PATH, this::createOrganizationsRoutes);
        path(ORG_UNITS_PATH, this::createOrgUnitsRoutes);
        path(USERS_PATH, this::createUsersRoutes);
    }

    private void createTransactionsRoutes() {
        get(new RetrieveTransactionsRoute(authUCFactory, transactionUCFactory, jsonSerializer, new TransactionB2RConverter(), exceptionHandler));
        post(new UpsertTransactionRoute(authUCFactory, jsonSerializer, exceptionHandler, transactionUCFactory, new TransactionUpsertR2BConverter()));
        delete(TRANSACTION_ID_PATH_PARAM, new DeleteTransactionRoute(authUCFactory, jsonSerializer, exceptionHandler, transactionUCFactory));
    }

    private void createAutomaticTransactionsRoutes() {
        get(new RetrieveAutomaticTransactionsRoute(authUCFactory, automaticTransactionUCFactory, jsonSerializer, new AutomaticTransactionB2RConverter(), exceptionHandler));
        post(new UpsertAutomaticTransactionRoute(authUCFactory, jsonSerializer, exceptionHandler, automaticTransactionUCFactory, new AutomaticTransactionR2BConverter()));
        delete(AUTOMATIC_TRANSACTION_ID_PATH_PARAM, new DeleteAutomaticTransactionRoute(authUCFactory, jsonSerializer, exceptionHandler, automaticTransactionUCFactory));
    }

    private void createOrganizationsRoutes() {
        get(new RetrieveOrganizationsRoute(authUCFactory, organizationUCFactory, jsonSerializer, new OrganizationB2RConverter(), exceptionHandler));
        post(new UpsertOrganizationRoute(authUCFactory, jsonSerializer, exceptionHandler, organizationUCFactory, new OrganizationR2BConverter()));
        delete(ORGANIZATION_ID_PATH_PARAM, new DeleteOrganizationRoute(authUCFactory, jsonSerializer, exceptionHandler, organizationUCFactory));
    }

    private void createOrgUnitsRoutes() {
        get(new RetrieveOrgUnitsRoute(authUCFactory, orgUnitUCFactory, jsonSerializer, new OrgUnitB2RConverter(), exceptionHandler));
        post(new UpsertOrgUnitRoute(authUCFactory, jsonSerializer, exceptionHandler, orgUnitUCFactory, new OrgUnitR2BConverter()));
        delete(ORG_UNIT_ID_PATH_PARAM, new DeleteOrgUnitRoute(authUCFactory, jsonSerializer, exceptionHandler, orgUnitUCFactory));
    }

    private void createUsersRoutes() {
        get(new RetrieveUsersRoute(authUCFactory, userUCFactory, jsonSerializer, new UserDTOB2RConverter(), exceptionHandler));
        post(ctx -> new CreateUserRoute(userUCFactory, new UserCreateR2BConverter()).processRequest(ctx));
        put(new UpdateUserRoute(authUCFactory, jsonSerializer, exceptionHandler, userUCFactory, new UserDTOR2BConverter()));
        delete(USER_ID_PATH_PARAM, new DeleteUserRoute(authUCFactory, jsonSerializer, exceptionHandler, userUCFactory));
    }
}
