package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.example.converter.*;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.*;
import org.example.factory.implementation.*;
import org.example.gateway.*;
import org.example.gateway.postgres.*;
import org.example.serialization.json.JsonSerializer;
import org.example.serialization.json.SerializerProvider;
import org.example.usecase.TokenGenerator;
import org.example.usecase.TokenValidator;
import org.example.usecase.implementation.JwtGenerator;
import org.example.usecase.implementation.JwtProcessorBuilder;
import org.example.usecase.implementation.JwtValidator;
import org.flywaydb.core.Flyway;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.ThreadLocalTransactionProvider;

import java.util.concurrent.Executors;

public class Main {
    private static final CompositeDisposable DISPOSABLES = new CompositeDisposable();

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dotenv.get("DATABASE_URL"));
        hikariConfig.setUsername(dotenv.get("DATABASE_USERNAME"));
        hikariConfig.setPassword(dotenv.get("DATABASE_PASSWORD"));
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setIdleTimeout(30000);
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setDriverClassName("org.postgresql.Driver");

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        Configuration jooqConfig = new DefaultConfiguration()
                .set(new ThreadLocalTransactionProvider(new DataSourceConnectionProvider(hikariDataSource)))
                .set(SQLDialect.POSTGRES);
        DSLContext jooqDSLContext = DSL.using(jooqConfig);

        Flyway.configure()
                .locations("org.example.db.migration", "org/example/db/migration")
                .dataSource(hikariDataSource)
                .load()
                .migrate();

        TransactionGateway transactionGateway = new PostgresTransactionGateway(jooqDSLContext);
        AutomaticTransactionGateway automaticTransactionGateway = new PostgresAutomaticTransactionGateway(jooqDSLContext);
        OrganizationGateway organizationGateway = new PostgresOrganizationGateway(jooqDSLContext);
        OrgUnitGateway orgUnitGateway = new PostgresOrgUnitGateway(jooqDSLContext);
        UserGateway userGateway = new PostgresUserGateway(jooqDSLContext);

        TokenGenerator tokenGenerator = new JwtGenerator(dotenv.get("JWT_SECRET_KEY"));
        JwtProcessorBuilder jwtProcessorBuilder = new JwtProcessorBuilder(dotenv.get("JWT_SECRET_KEY"));
        TokenValidator tokenValidator = new JwtValidator(jwtProcessorBuilder.buildProcess());

        AuthenticationUseCaseFactory authUCFactory = new AuthenticationUseCaseFactoryImpl(userGateway, new UserLoginB2DConverter(), tokenGenerator, tokenValidator, new UserDTOD2BConverter(), orgUnitGateway);
        TransactionUseCaseFactory transactionUCFactory = new TransactionUseCaseFactoryImpl(transactionGateway, new TransactionD2BConverter(), new TransactionUpsertB2DConverter());
        AutomaticTransactionUseCaseFactory automaticTransactionUCFactory = new AutomaticTransactionUseCaseFactoryImpl(automaticTransactionGateway, new AutomaticTransactionD2BConverter(), new AutomaticTransactionB2DConverter());
        OrganizationUseCaseFactory organizationUseCaseFactory = new OrganizationUseCaseFactoryImpl(organizationGateway, new OrganizationD2BConverter(), new OrganizationB2DConverter(), new OrganizationCreateB2DConverter(), new OrgUnitD2BConverter());
        OrgUnitUseCaseFactory orgUnitUseCaseFactory = new OrgUnitUseCaseFactoryImpl(orgUnitGateway, new OrgUnitD2BConverter(), new OrgUnitB2DConverter());
        UserUseCaseFactory userUseCaseFactory = new UserUseCaseFactoryImpl(userGateway, new UserDTOD2BConverter(), new UserDTOB2DConverter(), new UserCreateB2DConverter());

        AutomaticProcessUseCaseFactory automaticProcessUseCaseFactory = new AutomaticProcessUseCaseFactoryImpl(
                Schedulers.from(Executors.newFixedThreadPool(1, new NameableThreadFactory("AutomaticTransactionsProcessor"))),
                transactionGateway,
                automaticTransactionGateway
        );
        automaticProcessUseCaseFactory.createProcessAutomaticTransactionsUseCase()
                .execute()
                .subscribe();

        JsonSerializer jsonSerializer = SerializerProvider.getSerializer();
        JavalinExceptionHandler exceptionHandler = new JavalinExceptionHandler();



        AppBuilder appBuilder = new AppBuilder(
                authUCFactory,
                transactionUCFactory,
                jsonSerializer,
                exceptionHandler,
                automaticTransactionUCFactory,
                organizationUseCaseFactory,
                orgUnitUseCaseFactory,
                userUseCaseFactory
        );
        appBuilder.build();
    }
}
