package yeamy.sqlite.statement;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import yeamy.sqlite.statement.function.Expression;

public class Update implements SQLString {
    private final String table;
    private final LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    private Clause where;

    public Update(String table) {
        this.table = table;
    }

    public Update set(String column, Object value) {
        map.put(column, value);
        return this;
    }

    public Update increase(String column, int number) {
        map.put(column, new Expression(new Column(column), '-', number));
        return this;
    }

    public Update reduce(String column, int number) {
        map.put(column, new Expression(column, "-", number));
        return this;
    }

    public Update increase(String column, long number) {
        map.put(column, new Expression(column, "+", number));
        return this;
    }

    public Update reduce(String column, long number) {
        map.put(column, new Expression(column, "-", number));
        return this;
    }

    public Update increase(String column, Number number) {
        map.put(column, new Expression(column, "+", number));
        return this;
    }

    public Update reduce(String column, Number number) {
        map.put(column, new Expression(column, "-", number));
        return this;
    }

    public Update where(Clause where) {
        this.where = where;
        return this;
    }

    @Override
    public void toSQL(StringBuilder sql) {
        sql.append("UPDATE ");
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
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toSQL(sb);
        return sb.toString();
    }
}
