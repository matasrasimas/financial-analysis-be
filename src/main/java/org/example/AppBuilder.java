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
        get(AUTH_PATH, context -> new AuthenticateUserRoute(authUCFactory, new UserDTOB2RConverter()).processRequest(context));
        path(TRANSACTIONS_PATH, this::createTransactionsRoutes);
        path(AUTOMATIC_TRANSACTIONS_PATH, this::createAutomaticTransactionsRoutes);
        path(ORGANIZATIONS_PATH, this::createOrganizationsRoutes);
        path(ORG_UNITS_PATH, this::createOrgUnitsRoutes);
        path(USERS_PATH, this::createUsersRoutes);
    }

    private void createTransactionsRoutes() {
        post(FROM_FILE, new GenerateTransactionsFromFileRoute(authUCFactory, jsonSerializer, exceptionHandler, transactionUCFactory, new TransactionFromFileB2RConverter()));
        get(new RetrieveTransactionsRoute(authUCFactory, transactionUCFactory, jsonSerializer, new TransactionB2RConverter(), exceptionHandler));
        post(new UpsertTransactionsRoute(authUCFactory, jsonSerializer, exceptionHandler, transactionUCFactory, new TransactionUpsertR2BConverter(), new TransactionB2RConverter()));
        path(TRANSACTION_ID_PATH_PARAM, this::createTransactionIdRoutes);
    }

    private void createTransactionIdRoutes() {
        delete(new DeleteTransactionRoute(authUCFactory, jsonSerializer, exceptionHandler, transactionUCFactory));
    }

    private void createAutomaticTransactionsRoutes() {
        get(new RetrieveAutomaticTransactionsRoute(authUCFactory, automaticTransactionUCFactory, jsonSerializer, new AutomaticTransactionB2RConverter(), exceptionHandler));
        post(new UpsertAutomaticTransactionRoute(authUCFactory, jsonSerializer, exceptionHandler, automaticTransactionUCFactory, new AutomaticTransactionR2BConverter(), new AutomaticTransactionB2RConverter()));
        delete(AUTOMATIC_TRANSACTION_ID_PATH_PARAM, new DeleteAutomaticTransactionRoute(authUCFactory, jsonSerializer, exceptionHandler, automaticTransactionUCFactory));
    }

    private void createOrganizationsRoutes() {
        get(new RetrieveOrganizationsRoute(authUCFactory, organizationUCFactory, jsonSerializer, new OrganizationB2RConverter(), exceptionHandler));
        post(new CreateOrganizationRoute(authUCFactory, jsonSerializer, exceptionHandler, organizationUCFactory, new OrganizationCreateR2BConverter(), new OrgUnitB2RConverter()));
        put(new UpdateOrganizationRoute(authUCFactory, jsonSerializer, exceptionHandler, organizationUCFactory, new OrganizationR2BConverter()));
        path(ORGANIZATION_ID_PATH_PARAM, this::createOrganizationIdRoutes);
    }

    private void createOrganizationIdRoutes() {
        get(ORG_UNITS_PATH, new RetrieveOrganizationOrgUnitsRoute(authUCFactory, jsonSerializer, exceptionHandler, orgUnitUCFactory, new OrgUnitB2RConverter()));
        delete(new DeleteOrganizationRoute(authUCFactory, jsonSerializer, exceptionHandler, organizationUCFactory));
    }

    private void createOrgUnitsRoutes() {
        get(new RetrieveOrgUnitsRoute(authUCFactory, orgUnitUCFactory, jsonSerializer, new OrgUnitB2RConverter(), exceptionHandler));
        post(new UpsertOrgUnitRoute(authUCFactory, jsonSerializer, exceptionHandler, orgUnitUCFactory, new OrgUnitR2BConverter()));
        path(ORG_UNIT_ID_PATH_PARAM, this::createOrgUnitIdRoutes);
    }

    private void createOrgUnitIdRoutes() {
        get(new RetrieveOrgUnitByIdRoute(authUCFactory, jsonSerializer, exceptionHandler, orgUnitUCFactory, new OrgUnitB2RConverter()));
        get(TRANSACTIONS_PATH, new RetrieveOrgUnitTransactionsRoute(authUCFactory, jsonSerializer, exceptionHandler, transactionUCFactory, new TransactionB2RConverter()));
        get(AUTOMATIC_TRANSACTIONS_PATH, new RetrieveOrgUnitAutomaticTransactionsRoute(authUCFactory, jsonSerializer, exceptionHandler, automaticTransactionUCFactory, new AutomaticTransactionB2RConverter()));
        delete(new DeleteOrgUnitRoute(authUCFactory, jsonSerializer, exceptionHandler, orgUnitUCFactory));
    }

    private void createUsersRoutes() {
        get(context -> new RetrieveUsersRoute(userUCFactory, new UserDTOB2RConverter()).processRequest(context));
        post(ctx -> new CreateUserRoute(userUCFactory, new UserCreateR2BConverter()).processRequest(ctx));
        put(new UpdateUserRoute(authUCFactory, jsonSerializer, exceptionHandler, userUCFactory, new UserDTOR2BConverter()));
        path(USER_ID_PATH_PARAM, this::createUserIdRoutes);
    }

    private void createUserIdRoutes() {
        get(ORGANIZATIONS_PATH, new RetrieveUserOrganizationRoute(authUCFactory, jsonSerializer, exceptionHandler, organizationUCFactory, new OrganizationB2RConverter()));
        delete(new DeleteUserRoute(authUCFactory, jsonSerializer, exceptionHandler, userUCFactory));
    }
}
