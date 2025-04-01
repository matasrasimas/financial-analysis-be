/*
 * This file is generated by jOOQ.
 */
package org.example.generated.jooq.tables;


import java.util.Collection;
import java.util.UUID;

import org.example.generated.jooq.DefaultSchema;
import org.example.generated.jooq.Keys;
import org.example.generated.jooq.tables.OrgUnits.OrgUnitsPath;
import org.example.generated.jooq.tables.records.OrganizationsRecord;
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
public class Organizations extends TableImpl<OrganizationsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>organizations</code>
     */
    public static final Organizations ORGANIZATIONS = new Organizations();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OrganizationsRecord> getRecordType() {
        return OrganizationsRecord.class;
    }

    /**
     * The column <code>organizations.id</code>.
     */
    public final TableField<OrganizationsRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>organizations.title</code>.
     */
    public final TableField<OrganizationsRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>organizations.code</code>.
     */
    public final TableField<OrganizationsRecord, String> CODE = createField(DSL.name("code"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>organizations.address</code>.
     */
    public final TableField<OrganizationsRecord, String> ADDRESS = createField(DSL.name("address"), SQLDataType.CLOB, this, "");

    private Organizations(Name alias, Table<OrganizationsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Organizations(Name alias, Table<OrganizationsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>organizations</code> table reference
     */
    public Organizations(String alias) {
        this(DSL.name(alias), ORGANIZATIONS);
    }

    /**
     * Create an aliased <code>organizations</code> table reference
     */
    public Organizations(Name alias) {
        this(alias, ORGANIZATIONS);
    }

    /**
     * Create a <code>organizations</code> table reference
     */
    public Organizations() {
        this(DSL.name("organizations"), null);
    }

    public <O extends Record> Organizations(Table<O> path, ForeignKey<O, OrganizationsRecord> childPath, InverseForeignKey<O, OrganizationsRecord> parentPath) {
        super(path, childPath, parentPath, ORGANIZATIONS);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class OrganizationsPath extends Organizations implements Path<OrganizationsRecord> {
        public <O extends Record> OrganizationsPath(Table<O> path, ForeignKey<O, OrganizationsRecord> childPath, InverseForeignKey<O, OrganizationsRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private OrganizationsPath(Name alias, Table<OrganizationsRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public OrganizationsPath as(String alias) {
            return new OrganizationsPath(DSL.name(alias), this);
        }

        @Override
        public OrganizationsPath as(Name alias) {
            return new OrganizationsPath(alias, this);
        }

        @Override
        public OrganizationsPath as(Table<?> alias) {
            return new OrganizationsPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<OrganizationsRecord> getPrimaryKey() {
        return Keys.ORGANIZATIONS_PKEY;
    }

    private transient OrgUnitsPath _orgUnits;

    /**
     * Get the implicit to-many join path to the <code>public.org_units</code>
     * table
     */
    public OrgUnitsPath orgUnits() {
        if (_orgUnits == null)
            _orgUnits = new OrgUnitsPath(this, null, Keys.ORG_UNITS__ORG_UNITS_ORGANIZATION_ID_FKEY.getInverseKey());

        return _orgUnits;
    }

    @Override
    public Organizations as(String alias) {
        return new Organizations(DSL.name(alias), this);
    }

    @Override
    public Organizations as(Name alias) {
        return new Organizations(alias, this);
    }

    @Override
    public Organizations as(Table<?> alias) {
        return new Organizations(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Organizations rename(String name) {
        return new Organizations(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Organizations rename(Name name) {
        return new Organizations(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Organizations rename(Table<?> name) {
        return new Organizations(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organizations where(Condition condition) {
        return new Organizations(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organizations where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organizations where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organizations where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Organizations where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Organizations where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Organizations where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Organizations where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organizations whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Organizations whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
