/*
 * This file is generated by jOOQ.
 */
package org.example.generated.jooq;


import java.util.Arrays;
import java.util.List;

import org.example.generated.jooq.tables.AutomaticTransactions;
import org.example.generated.jooq.tables.FlywaySchemaHistory;
import org.example.generated.jooq.tables.Invitations;
import org.example.generated.jooq.tables.OrgUnits;
import org.example.generated.jooq.tables.Organizations;
import org.example.generated.jooq.tables.Transactions;
import org.example.generated.jooq.tables.Users;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>automatic_transactions</code>.
     */
    public final AutomaticTransactions AUTOMATIC_TRANSACTIONS = AutomaticTransactions.AUTOMATIC_TRANSACTIONS;

    /**
     * The table <code>flyway_schema_history</code>.
     */
    public final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>invitations</code>.
     */
    public final Invitations INVITATIONS = Invitations.INVITATIONS;

    /**
     * The table <code>org_units</code>.
     */
    public final OrgUnits ORG_UNITS = OrgUnits.ORG_UNITS;

    /**
     * The table <code>organizations</code>.
     */
    public final Organizations ORGANIZATIONS = Organizations.ORGANIZATIONS;

    /**
     * The table <code>transactions</code>.
     */
    public final Transactions TRANSACTIONS = Transactions.TRANSACTIONS;

    /**
     * The table <code>users</code>.
     */
    public final Users USERS = Users.USERS;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            AutomaticTransactions.AUTOMATIC_TRANSACTIONS,
            FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY,
            Invitations.INVITATIONS,
            OrgUnits.ORG_UNITS,
            Organizations.ORGANIZATIONS,
            Transactions.TRANSACTIONS,
            Users.USERS
        );
    }
}
