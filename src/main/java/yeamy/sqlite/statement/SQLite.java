package yeamy.sqlite.statement;

import yeamy.sqlite.statement.columninfo.ColumnInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SQLite {

    /**
     * SELECT * FROM <b>table</b> WHERE <b>where</b>;
     *
     * @see Select
     */
    public static String selectAll(String table, Clause where) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        SQLString.appendTable(sql, table);
        sql.append(" WHERE ");
        where.toSQL(sql);
        sql.append(';');
        return sql.toString();
    }

    /**
     * SELECT * FROM <b>table</b> WHERE <b>where</b> LIMIT <b>limit</b>;
     *
     * @see Select
     */
    public static String selectAll(String table, Clause where, int limit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        SQLString.appendTable(sql, table);
        sql.append(" WHERE ");
        where.toSQL(sql);
        sql.append(" LIMIT ").append(limit).append(';');
        return sql.toString();
    }

    /**
     * INSERT INTO <b>table</b> (<b>map.keys</b>) VALUS (<b>map.values</b>);
     *
     * @param table table to insert
     * @param map   column - value, value type support : String, Number, SQLString
     * @return sql insert statement
     * @see Insert
     */
    public static String insert(String table, Map<String, Object> map) {
        StringBuilder sql = new StringBuilder();
        int l = map.size();
        String[] columns = new String[l];
        Object[] values = new Object[l];
        l = 0;
        for (Entry<String, Object> li : map.entrySet()) {
            columns[l] = li.getKey();
            values[l] = li.getValue();
            l++;
        }
        sql.append("INSERT INTO ");
        SQLString.appendTable(sql, table);
        sql.append(" (");
        boolean f = true;
        for (String column : columns) {
            if (f) {
                f = false;
            } else {
                sql.append(", ");
            }
            SQLString.appendColumn(sql, column);
        }
        sql.append(") VALUES (");
        f = true;
        for (Object value : values) {
            if (f) {
                f = false;
            } else {
                sql.append(", ");
            }
            SQLString.appendValue(sql, value);
        }
        sql.append(");");
        return sql.toString();
    }

    /**
     * INSERT INTO <b>table</b> (<b>column</b>) <b>select</b>;
     *
     * @param select query for data.
     * @param table  the destination table.
     * @param column the destination column in table, if empty means same with select.
     * @return sql statement
     */
    public static String selectInto(Select select, String table, List<String> column) {
        String[] array = new String[column.size()];
        column.toArray(array);
        return selectInto(select, table, array);
    }

    /**
     * INSERT INTO <b>table</b> (<b>column</b>) <b>select</b>;
     *
     * @param select query for data.
     * @param table  the destination table.
     * @param column the destination column in table, if null means same with select.
     * @return sql statement
     */
    public static String selectInto(Select select, String table, String... column) {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        SQLString.appendTable(sql, table);
        Object[] columns = (column != null) ? column : select.getColumns();
        if (columns.length == 0) {
            throw new NullPointerException("no column in select");
        }
        Object c1 = columns[0];
        if (columns.length > 1) {
            appendColumns(sql, columns);
        } else if ("*".equals(c1)) {
            sql.append(' ');
        } else if (c1 instanceof Column && "*".equals(((Column) c1).name)) {
            sql.append(' ');
        } else {
            appendColumns(sql, columns);
        }
        select.toSQL(sql);
        return sql.toString();
    }

    private static void appendColumns(StringBuilder sql, Object[] columns) {
        sql.append('(');
        boolean f = true;
        for (Object column : columns) {
            if (f) {
                f = false;
            } else {
                sql.append(", ");
            }
            if (column instanceof String) {
                SQLString.appendColumn(sql, column.toString());
            } else {
                AbstractColumn<?> c = (AbstractColumn<?>) column;
                c.nameInInsert(sql);
            }
        }
        sql.append(") ");
    }

    /**
     * DELETE FROM <b>table</b> WHERE <b>where</b>;
     */
    public static String delete(String table, Clause where) {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        SQLString.appendTable(sql, table);
        sql.append(" WHERE ");
        where.toSQL(sql);
        sql.append(';');
        return sql.toString();
    }

    /**
     * UPDATE <b>table</b> SET <b>map.key</b> = <b>map.value</b>, ... WHERE <b>where</b>;
     *
     * @see Update
     */
    public static String update(String table, Map<String, Object> map, Clause where) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        SQLString.appendTable(sql, table);
        sql.append(" SET ");
        boolean f = true;
        for (Entry<String, Object> li : map.entrySet()) {
            if (f) {
                f = false;
            } else {
                sql.append(", ");
            }
            SQLString.appendColumn(sql, li.getKey());
            sql.append(" = ");
            Object value = li.getValue();
            if (value instanceof AbstractColumn) {
                AbstractColumn<?> col = (AbstractColumn<?>) value;
                col.toSQL(sql);
            } else {
                SQLString.appendValue(sql, value);
            }
        }
        if (where != null) {
            sql.append(" WHERE ");
            where.toSQL(sql);
        }
        sql.append(';');
        return sql.toString();
    }

    /**
     * DROP TABLE <b>table</b>;
     */
    public static String dropTable(String table) {
        StringBuilder sql = new StringBuilder("DROP TABLE ");
        SQLString.appendTable(sql, table);
        sql.append(';');
        return sql.toString();
    }

    public static String dropTable(String db, String table) {
        StringBuilder sql = new StringBuilder("DROP TABLE ");
        SQLString.appendDatabase(sql, db);
        sql.append('.');
        SQLString.appendTable(sql, table);
        sql.append(';');
        return sql.toString();
    }

    /**
     * CREATE TABLE IF NOT EXISTS <b>table</b> (columns);
     */
    public static String createTable(String table, List<ColumnInfo<?>> columns) {
        return createTable(null, table, columns, true);
    }


    /**
     * CREATE TABLE <b>IF NOT EXISTS</b> <b>database</b><i>(if not null)</i>.<b>table</b> (<b>columns</b>);
     */
    public static String createTable(String database, String table, List<ColumnInfo<?>> columns, boolean ifNotExists) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        if (ifNotExists) {
            sql.append("IF NOT EXISTS ");
        }
        if (database != null) {
            SQLString.appendDatabase(sql, database);
            sql.append('.');
        }
        SQLString.appendTable(sql, table);
        sql.append('(');
        // columns
        boolean first = true;
        for (ColumnInfo<?> column : columns) {
            if (first) {
                first = false;
            } else {
                sql.append(" ,");
            }
            column.toSQL(sql);
        }
        sql.append(");");
        return sql.toString();
    }

    /**
     * CREATE UNIQUE INDEX <b>name</b> ON <b>table</b> (<b>columns</b>);
     */
    public static String createUniqueIndex(String table, String name, String... columns) {
        return createIndex(table, true, name, columns);
    }

    /**
     * CREATE INDEX <b>name</b> ON <b>table</b> (<b>columns</b>);
     */
    public static String createIndex(String table, String name, String... columns) {
        return createIndex(table, false, name, columns);
    }

    private static String createIndex(String table, boolean unique, String name, String... columns) {
        StringBuilder sql = new StringBuilder(unique ? "CREATE UNIQUE INDEX " : "CREATE INDEX ");
        SQLString.appendColumn(sql, name);
        sql.append(" ON ");
        SQLString.appendTable(sql, table);
        sql.append(" (");
        boolean first = true;
        for (String column : columns) {
            if (first) {
                first = false;
            } else {
                sql.append(", ");
            }
            SQLString.appendColumn(sql, column);
        }
        sql.append(");");
        return sql.toString();
    }

    /**
     * ALTER TABLE <b>table</b> ADD <b>column</b>;
     */
    public static String addColumn(String table, ColumnInfo<?> column) {
        StringBuilder sql = new StringBuilder("ALTER TABLE ");
        SQLString.appendTable(sql, table);
        sql.append(" ADD ");
        SQLString.appendColumn(sql, column.name);
        sql.append(' ');
        column.toSQL(sql);
        sql.append(";");
        return sql.toString();
    }

    /**
     * ALTER TABLE <b>table</b> RENAME TO <b>newName</b>;
     */
    public static String renameTable(String table, String newName) {
        StringBuilder sql = new StringBuilder("ALTER TABLE ");
        SQLString.appendTable(sql, table);
        sql.append(" RENAME TO");
        SQLString.appendColumn(sql, newName);
        sql.append(";");
        return sql.toString();
    }

    /**
     * sqlite not supported alter table, but can do it by transaction,
     * make sure temporary tale with suffix "_2" not exist.
     *
     * @param table   table name
     * @param columns <b>key:</b> column info after modify<br>
     *                <b>value:</b> column name in the table before modify, or null if newly added
     * @return transaction sql statements :<br>
     *     1: BEGIN TRANSACTION;<br>
     *     2: CREATE TABLE <b>table_2</b> IF NOT EXIST (<b>columns.keys</b>);<br>
     *     3: INSERT INTO <b>table_2</b> SELECT <b>columns.values</b> FROM <b>table</b>;<br>
     *     4: DROP TABLE <b>table</b>;<br>
     *     5: ALTER TABLE <b>table_2</b> RENAME TO <b>table</b>;<br>
     *     6: END TRANSACTION;<br>
     */
    public static String[] alterTable(String table, LinkedHashMap<ColumnInfo<?>, String> columns) {
        String table2 = table + "_1";
        Select select = new Select(table);
        ArrayList<String> column2 = new ArrayList<>();
        for (Map.Entry<ColumnInfo<?>, String> col : columns.entrySet()) {
            String before = col.getValue();
            if (before != null) {
                select.add(col.getKey().name);
                column2.add(before);
            }
        }
        return new String[]{
                "BEGIN TRANSACTION;",
                createTable(table, new ArrayList<>(columns.keySet())),
                selectInto(select, table2, column2),
                dropTable(table),
                renameTable(table2, table),
                "END TRANSACTION;"
        };
    }

}
