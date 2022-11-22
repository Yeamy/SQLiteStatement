package yeamy.sqlite.statement;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface SQLString {

    /**
     * serialize current object into sql
     * @param sql the sql statement
     */
    void toSQL(StringBuilder sql);

    static void appendDatabase(StringBuilder sql, String database) {
        sql.append(database);
    }

    static void appendTable(StringBuilder sql, String table) {
        sql.append(table);
    }

    static void appendColumn(StringBuilder sql, String column) {
        sql.append(column);
    }

    /**
     * append value into sql
     * @param sql serialize value into this StringBuilder
     * @param value if is null append "NULL", if is number keep it, if is SQLString do toSQL()
     *              otherwise value.toString() then add quotation marks and escape characters
     */
    static void appendValue(StringBuilder sql, Object value) {
        if (value == null) {
            sql.append("NULL");
        } else if (value instanceof Number) {
            sql.append(value);
        } else if (value instanceof Searchable) {
            Searchable<?> select = (Searchable<?>) value;
            sql.append('(');
            select.toSQL(sql);
            sql.append(')');
        } else if (value instanceof SQLString) {
            ((SQLString) value).toSQL(sql);
        } else if (value instanceof Date) {
            String pattern;
            if (value instanceof java.sql.Date) {
                pattern = "yyyy-MM-dd";
            } else if (value instanceof Time) {
                pattern = "HH:mm:ss";
            } else {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sql.append('\'').append(sdf.format((Date) value)).append('\'');
        } else {// as String
            String out = value.toString();
            sql.append('\'');
            // replace all '
            int l = out.length();
            int start = 0;
            int end;
            while (true) {
                end = out.indexOf('\'', start);
                if (end == -1) {
                    sql.append(out, start, l);
                    break;
                }
                end += 1;
                sql.append(out, start, end);
                sql.append('\'');
                start = end;
                if (start >= l) {
                    break;
                }
            }
            sql.append('\'');
        }
    }
}