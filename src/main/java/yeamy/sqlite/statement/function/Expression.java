package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.AbstractColumn;
import yeamy.sqlite.statement.SQLString;

/**
 * custom expression e.g.
 * <pre>
 *     new Expression(1, "+", "2", '-', 3);
 * </pre>
 */
public class Expression extends AbstractColumn<Expression> {
    private static final char[] symbol = {'+', '-', '*', '/', '%', '(', ')', '|', '>', '<', '='};
    private final Object[] obj;
    private final String expression;

    /**
     * @param obj support:
     *            {@linkplain String}(will not process symbol / escape characters) <br>
     *            {@linkplain SQLString}(value) <br>
     *            {@linkplain Number}
     * @see SQLString#appendValue(StringBuilder, Object)
     */
    public Expression(Object... obj) {
        this.obj = obj;
        expression = null;
    }

    public Expression(String expression) {
        this.obj = new Object[0];
        this.expression = expression;
    }

    @Override
    public void toSQL(StringBuilder sql) {
        if (expression != null) {
            sql.append(expression);
            return;
        }
        boolean f = true;
        for (Object o : obj) {
            if (f) {
                f = false;
            } else {
                sql.append(' ');
            }
            if (o instanceof String) {
                sql.append(o);
            } else {
                SQLString.appendValue(sql, o);
            }
        }
    }

    private boolean allSymbol(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!isSymbol(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSymbol(char c) {
        for (char s : symbol) {
            if (s == c) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void nameInColumn(StringBuilder sql) {
        if (nameAlias == null) {
            toSQL(sql);
        } else {
            sql.append('(');
            toSQL(sql);
            sql.append(") AS ");
            SQLString.appendColumn(sql, nameAlias);
        }
    }

}
