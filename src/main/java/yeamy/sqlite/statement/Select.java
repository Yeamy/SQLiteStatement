package yeamy.sqlite.statement;

import java.util.*;

public class Select extends Searchable<Select> {
    final Object from;
    String alias;
    private final LinkedHashSet<Object> columns = new LinkedHashSet<>();
    private LinkedList<Join> joins;
    private ArrayList<Object> groupBy;
    private Clause where;
    private Clause having;
    private boolean distinct = false;

    /**
     * @param from table to query
     */
    public Select(String from) {
        this.from = from;
    }

    /**
     * search / union as a table must with alias
     */
    public Select(Searchable<?> from, String alias) {
        this.from = from;
        this.alias = alias;
    }

    /**
     * distinct the result
     */
    public void distinct() {
        this.distinct = true;
    }

    /**
     * add column
     */
    public Select add(String column) {
        columns.add(column);
        return this;
    }

    /**
     * add column
     *
     * @param table can be from or join
     * @param name  column name
     * @see Column
     */
    public Select add(String table, String name) {
        columns.add(new Column(table, name));
        return this;
    }

    /**
     * add column
     */
    public Select add(AbstractColumn<?> column) {
        columns.add(column);
        return this;
    }

    /**
     * add column
     */
    public Select addAll(Collection<AbstractColumn<?>> columns) {
        this.columns.addAll(columns);
        return this;
    }

    /**
     * add column
     */
    public Select addAll(AbstractColumn<?>... column) {
        Collections.addAll(columns, column);
        return this;
    }

    /**
     * add column
     */
    public Select addAll(String... column) {
        Collections.addAll(columns, column);
        return this;
    }

    /**
     * join table
     *
     * @param join the join statement
     * @see InnerJoin
     * @see LeftJoin
     * @see CrossJoin
     */
    public On join(Join join) {
        if (joins == null) {
            joins = new LinkedList<>();
        }
        joins.add(join);
        return new On(this, join);
    }

    public Select crossJoin(String src, Column pattern) {
        Column column = (from instanceof String)//
                ? new Column((String) from, src).tableAs(alias)//
                : new Column((Searchable<?>) from, alias, src);
        return join(new CrossJoin(pattern.table, pattern.tableAlias))//
                .on(Clause.equals(column, pattern));
    }

    public Select innerJoin(String src, Column pattern) {
        Column column = (from instanceof String)//
                ? new Column((String) from, src).tableAs(alias)//
                : new Column((Searchable<?>) from, alias, src);
        return join(new InnerJoin(pattern.table, pattern.tableAlias))//
                .on(Clause.equals(column, pattern));
    }

    public Select leftJoin(String src, Column pattern) {
        Column column = (from instanceof String)//
                ? new Column((String) from, src).tableAs(alias)//
                : new Column((Searchable<?>) from, alias, src);
        return join(new LeftJoin(pattern.table, pattern.tableAlias))//
                .on(Clause.equals(column, pattern));
    }

    /**
     * set the where clause, set null to remove it.
     * @see Clause
     * @see MultiClause
     */
    public Select where(Clause clause) {
        this.where = clause;
        return this;
    }

    public Select groupBy(String column) {
        if (column != null) {
            if (groupBy == null) {
                groupBy = new ArrayList<>();
            }
            groupBy.add(column);
        }
        return this;
    }

    public Select groupBy(String... columns) {
        if (columns != null) {
            if (groupBy == null) {
                groupBy = new ArrayList<>();
            }
            Collections.addAll(groupBy, columns);
        }
        return this;
    }

    public Select groupBy(AbstractColumn<?> column) {
        if (column != null) {
            if (groupBy == null) {
                groupBy = new ArrayList<>();
            }
            groupBy.add(column);
        }
        return this;
    }

    public Select groupBy(AbstractColumn<?>... columns) {
        if (columns != null) {
            if (groupBy == null) {
                groupBy = new ArrayList<>();
            }
            Collections.addAll(groupBy, columns);
        }
        return this;
    }

    public Select groupBy(List<AbstractColumn<?>> columns) {
        if (columns != null) {
            if (groupBy == null) {
                groupBy = new ArrayList<>();
            }
            groupBy.addAll(columns);
        }
        return this;
    }

    public void having(Clause having) {
        this.having = having;
    }

    @Override
    public void toSQL(StringBuilder sql) {
        sql.append("SELECT ");
        if (distinct) {
            sql.append("DISTINCT ");
        }
        columns(sql);
        from(sql);
        // join on
        if (joins != null) {
            for (Join join : joins) {
                join.toSQL(sql);
            }
        }
        // where
        if (where != null) {
            sql.append(" WHERE ");
            where.toSQL(sql);
        }
        groupByHaving(sql);
        orderBy(sql);
        limit(sql);
    }

    private void columns(StringBuilder sql) {
        boolean f = true;
        for (Object column : columns) {
            if (f) {
                f = false;
            } else {
                sql.append(", ");
            }
            if (column instanceof AbstractColumn<?>) {
                ((AbstractColumn<?>) column).nameInColumn(sql);
            } else {
                SQLString.appendColumn(sql, column.toString());
            }
        }
        if (joins != null) {
            for (Join join : joins) {
                join.columns(sql);
            }
        }
    }

    private void from(StringBuilder sql) {
        if (from == null) {
            return;
        }
        sql.append(" FROM ");
        if (from instanceof String) {
            SQLString.appendTable(sql, (String) from);
        } else {
            SQLString.appendValue(sql, from);
        }
        if (alias != null) {
            sql.append(" AS ");
            SQLString.appendTable(sql, (String) alias);
        }
    }

    private void groupByHaving(StringBuilder sql) {
        if (groupBy == null) {
            return;
        }
        sql.append(" GROUP BY ");
        boolean f = true;
        for (Object li : groupBy) {
            if (f) {
                f = false;
            } else {
                sql.append(", ");
            }
            if (li instanceof AbstractColumn<?>) {
                ((AbstractColumn<?>) li).nameInGroup(sql);
            } else {
                SQLString.appendColumn(sql, li.toString());
            }
        }
        // having
        if (having != null) {
            having.toSQL(sql);
        }
    }

    @Override
    public Union union(Select select) {
        return new Union(this).union(select);
    }

    @Override
    public Union union(String select) {
        return new Union(this).union(select);
    }

    @Override
    public Union unionAll(Select select) {
        return new Union(this).unionAll(select);
    }

    @Override
    public Union unionAll(String select) {
        return new Union(this).unionAll(select);
    }

    Object[] getColumns() {
        Object[] out = new Object[columns.size()];
        return columns.toArray(out);
    }

    public Clause getWhere() {
        return where;
    }

    public boolean removeGroupBy(String column) {
        return groupBy != null && groupBy.remove(column);
    }

    public boolean removeGroupBy(AbstractColumn<?> column) {
        return groupBy != null && groupBy.remove(column);
    }

}