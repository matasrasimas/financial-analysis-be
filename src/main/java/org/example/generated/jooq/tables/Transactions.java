/*
 * This file is generated by jOOQ.
 */
package org.example.generated.jooq.tables;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.example.generated.jooq.DefaultSchema;
import org.example.generated.jooq.Keys;
import org.example.generated.jooq.tables.OrgUnits.OrgUnitsPath;
import org.example.generated.jooq.tables.records.TransactionsRecord;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Transactions extends TableImpl<TransactionsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>transactions</code>
     */
    public static final Transactions TRANSACTIONS = new Transactions();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TransactionsRecord> getRecordType() {
        return TransactionsRecord.class;
    }

    /**
     * The column <code>transactions.id</code>.
     */
    public final TableField<TransactionsRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>transactions.org_unit_id</code>.
     */
    public final TableField<TransactionsRecord, UUID> ORG_UNIT_ID = createField(DSL.name("org_unit_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>transactions.amount</code>.
     */
    public final TableField<TransactionsRecord, BigDecimal> AMOUNT = createField(DSL.name("amount"), SQLDataType.NUMERIC(10, 2).nullable(false), this, "");

    /**
     * The column <code>transactions.title</code>.
     */
    public final TableField<TransactionsRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>transactions.description</code>.
     */
    public final TableField<TransactionsRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>transactions.created_at</code>.
     */
    public final TableField<TransactionsRecord, LocalDate> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATE.nullable(false), this, "");

    private Transactions(Name alias, Table<TransactionsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Transactions(Name alias, Table<TransactionsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>transactions</code> table reference
     */
    public Transactions(String alias) {
        this(DSL.name(alias), TRANSACTIONS);
    }

    /**
     * Create an aliased <code>transactions</code> table reference
     */
    public Transactions(Name alias) {
        this(alias, TRANSACTIONS);
    }

    /**
     * Create a <code>transactions</code> table reference
     */
    public Transactions() {
        this(DSL.name("transactions"), null);
    }

    public <O extends Record> Transactions(Table<O> path, ForeignKey<O, TransactionsRecord> childPath, InverseForeignKey<O, TransactionsRecord> parentPath) {
        super(path, childPath, parentPath, TRANSACTIONS);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class TransactionsPath extends Transactions implements Path<TransactionsRecord> {
        public <O extends Record> TransactionsPath(Table<O> path, ForeignKey<O, TransactionsRecord> childPath, InverseForeignKey<O, TransactionsRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private TransactionsPath(Name alias, Table<TransactionsRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public TransactionsPath as(String alias) {
            return new TransactionsPath(DSL.name(alias), this);
        }

        @Override
        public TransactionsPath as(Name alias) {
            return new TransactionsPath(alias, this);
        }

        @Override
        public TransactionsPath as(Table<?> alias) {
            return new TransactionsPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<TransactionsRecord> getPrimaryKey() {
        return Keys.TRANSACTIONS_PKEY;
    }

    @Override
    public List<ForeignKey<TransactionsRecord, ?>> getReferences() {
        return Arrays.asList(Keys.TRANSACTIONS__TRANSACTIONS_ORG_UNIT_ID_FKEY);
    }

    private transient OrgUnitsPath _orgUnits;

    /**
     * Get the implicit join path to the <code>public.org_units</code> table.
     */
    public OrgUnitsPath orgUnits() {
        if (_orgUnits == null)
            _orgUnits = new OrgUnitsPath(this, Keys.TRANSACTIONS__TRANSACTIONS_ORG_UNIT_ID_FKEY, null);

        return _orgUnits;
    }

    @Override
    public Transactions as(String alias) {
        return new Transactions(DSL.name(alias), this);
    }

    @Override
    public Transactions as(Name alias) {
        return new Transactions(alias, this);
    }

    @Override
    public Transactions as(Table<?> alias) {
        return new Transactions(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Transactions rename(String name) {
        return new Transactions(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Transactions rename(Name name) {
        return new Transactions(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Transactions rename(Table<?> name) {
        return new Transactions(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Transactions where(Condition condition) {
        return new Transactions(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Transactions where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Transactions where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Transactions where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Transactions where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Transactions where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Transactions where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Transactions where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Transactions whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Transactions whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
