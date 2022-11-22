package yeamy.sqlite.statement;

import java.util.Collection;
import java.util.List;

public abstract class Clause extends AbstractColumn<Clause> {

    /**
     * @return if clause == null return self, otherwise return a MultiClause
     */
    public Clause and(Clause clause) {
        if (clause == null) {
            return this;
        }
        MultiClause m = (this instanceof MultiClause)//
                ? (MultiClause) this//
                : new MultiClause(this);
        m.add(" AND ", clause);
        return m;
    }

    /**
     * @return if clause == null return self, otherwise return a MultiClause
     */
    public Clause or(Clause clause) {
        if (clause == null) {
            return this;
        }
        MultiClause m = (this instanceof MultiClause)//
                ? (MultiClause) this//
                : new MultiClause(this);
        m.add(" OR ", clause);
        return m;
    }

    // ---------------------------------------------------------------------------

    private static class SingleClause extends Clause {
        private Object column;
        private final Object[] objs;
        private Object[] value;

        SingleClause(Object column, Object... objs) {
            this.column = column;
            this.objs = objs;
        }

        SingleClause setValues(Object... value) {
            this.value = value;
            return this;
        }

        @Override
        public final void toSQL(StringBuilder sql) {
            if (column != null) {
                if (column instanceof SQLString) {
                    ((SQLString) column).toSQL(sql);
                } else {
                    SQLString.appendColumn(sql, column.toString());
                }
            }
            for (Object obj : objs) {
                sql.append(' ');
                if (obj instanceof Collection) {
                    boolean f = true;
                    for (Object o : (Collection<?>) obj) {
                        if (f) {
                            f = false;
                        } else {
                            sql.append(", ");
                        }
                        append(sql, o);
                    }
                } else if (obj.getClass().isArray()) {
                    boolean f = true;
                    for (Object o : (Object[]) obj) {
                        if (f) {
                            f = false;
                        } else {
                            sql.append(", ");
                        }
                        append(sql, o);
                    }
                } else {
                    sql.append(obj);
                }
            }
        }

        private void append(StringBuilder sql, Object o) {
            if (isAppendValue(o)) {
                SQLString.appendValue(sql, o);
            } else {
                sql.append(o);
            }
        }

        private boolean isAppendValue(Object o) {
            if (o instanceof String) {
                for (Object v : value) {
                    if (o == v) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
    }

    // ---------------------------------------------------------------------------

    public static Clause isNull(AbstractColumn<?> column) {
        return new SingleClause(column, " IS NULL");
    }

    public static Clause isNotNull(AbstractColumn<?> column) {
        return new SingleClause(column, " IS NOT NULL");
    }

    public static Clause equals(AbstractColumn<?> column, Object pattern) {
        return new SingleClause(column, " = ", pattern).setValues(pattern);
    }

    public static Clause notEquals(AbstractColumn<?> column, Object pattern) {
        return new SingleClause(column, " <> ", pattern).setValues(pattern);
    }

    public static Clause greaterThan(AbstractColumn<?> column, Object pattern) {
        return new SingleClause(column, " > ", pattern).setValues(pattern);
    }

    public static Clause lessThan(AbstractColumn<?> column, Object pattern) {
        return new SingleClause(column, " < ", pattern).setValues(pattern);
    }

    public static Clause greaterEquals(AbstractColumn<?> column, Object pattern) {
        return new SingleClause(column, " >= ", pattern).setValues(pattern);
    }

    public static Clause lessEquals(AbstractColumn<?> column, Object pattern) {
        return new SingleClause(column, " <= ", pattern).setValues(pattern);
    }

    public static Clause like(AbstractColumn<?> column, String pattern) {
        return new SingleClause(column, " LIKE ", pattern).setValues(pattern);
    }

    /**
     * @param numberPattern number or string with ? or &#42;
     */
    public static Clause glob(AbstractColumn<?> column, String numberPattern) {
        return new SingleClause(column, " GLOB ", numberPattern).setValues(numberPattern);
    }

    /**
     * LIKE %<b>pattern</b>%
     */
    public static Clause contains(AbstractColumn<?> column, String pattern) {
        return like(column, '%' + pattern + '%');
    }

    /**
     * LIKE <b>pattern</b>%
     */
    public static Clause startWith(AbstractColumn<?> column, String pattern) {
        return like(column, pattern + '%');
    }

    /**
     * LIKE %<b>pattern</b>
     */
    public static Clause endWith(AbstractColumn<?> column, String pattern) {
        return like(column, '%' + pattern);
    }

    public static Clause notLike(AbstractColumn<?> column, String pattern) {
        return new SingleClause(column, " NOT LIKE ", pattern).setValues(pattern);
    }

    /**
     * NOT LIKE %<b>pattern</b>%
     */
    public static Clause notContains(AbstractColumn<?> column, String pattern) {
        return notLike(column, '%' + pattern + '%');
    }

    /**
     * NOT LIKE <b>pattern</b>%
     */
    public static Clause notStartWith(AbstractColumn<?> column, String pattern) {
        return notLike(column, pattern + '%');
    }

    /**
     * NOT LIKE <b>pattern</b>
     */
    public static Clause notEndWith(AbstractColumn<?> column, String pattern) {
        return notLike(column, '%' + pattern);
    }

    public static Clause in(AbstractColumn<?> column, int... array) {
        return new SingleClause(column, " IN(", array, ")");
    }

    public static Clause in(AbstractColumn<?> column, Object... pattern) {
        return new SingleClause(column, " IN(", pattern, ")");
    }

    public static Clause in(AbstractColumn<?> column, Collection<?> pattern) {
        return new SingleClause(column, " IN(", pattern, ")");
    }

    public static Clause in(AbstractColumn<?> column, Select pattern) {
        return new SingleClause(column, " IN ", pattern);
    }

    public static Clause notIn(AbstractColumn<?> column, int... array) {
        return new SingleClause(column, " NOT IN(", array, ")");
    }

    public static Clause notIn(AbstractColumn<?> column, Object... pattern) {
        return new SingleClause(column, " NOT IN(", pattern, ")");
    }

    public static Clause notIn(AbstractColumn<?> column, Collection<?> pattern) {
        return new SingleClause(column, " NOT IN(", pattern, ")");
    }

    public static Clause notIn(AbstractColumn<?> column, Select pattern) {
        return new SingleClause(column, " NOT IN ", pattern);
    }

    public static Clause between(AbstractColumn<?> column, Object start, Object end) {
        return new SingleClause(column, " BETWEEN ", start, " AND ", end).setValues(start, end);
    }

    // ---------------------------------------------------------------------------

    public static Clause isNull(String column) {
        return new SingleClause(column, " IS NULL");
    }

    public static Clause isNotNull(String column) {
        return new SingleClause(column, " IS NOT NULL");
    }

    public static Clause equals(String column, Object pattern) {
        return new SingleClause(column, " = ", pattern).setValues(pattern);
    }

    public static Clause notEquals(String column, Object pattern) {
        return new SingleClause(column, " <> ", pattern).setValues(pattern);
    }

    public static Clause greaterThan(String column, Object pattern) {
        return new SingleClause(column, " > ", pattern).setValues(pattern);
    }

    public static Clause lessThan(String column, Object pattern) {
        return new SingleClause(column, " < ", pattern).setValues(pattern);
    }

    public static Clause greaterEquals(String column, Object pattern) {
        return new SingleClause(column, " >= ", pattern).setValues(pattern);
    }

    public static Clause lessEquals(String column, Object pattern) {
        return new SingleClause(column, " <= ", pattern).setValues(pattern);
    }

    public static Clause like(String column, String pattern) {
        return new SingleClause(column, " LIKE ", pattern).setValues(pattern);
    }

    public static Clause glob(String column, String numberPattern) {
        return new SingleClause(column, " GLOB ", numberPattern).setValues(numberPattern);
    }

    public static Clause contains(String column, String pattern) {
        return like(column, '%' + pattern + '%');
    }

    /**
     * LIKE <b>pattern</b>%
     */
    public static Clause startWith(String column, String pattern) {
        return like(column, pattern + '%');
    }

    /**
     * NOT LIKE %<b>pattern</b>
     */
    public static Clause endWith(String column, String pattern) {
        return like(column, '%' + pattern);
    }

    // NOT LIKE
    public static Clause notLike(String column, String pattern) {
        return new SingleClause(column, " NOT LIKE ", pattern).setValues(pattern);
    }

    /**
     * NOT LIKE %<b>pattern</b>%
     */
    public static Clause notContains(String column, String pattern) {
        return notLike(column, '%' + pattern + '%');
    }

    /**
     * NOT LIKE <b>pattern</b>%
     */
    public static Clause notStartWith(String column, String pattern) {
        return notLike(column, pattern + '%');
    }

    /**
     * NOT LIKE %<b>pattern</b>
     */
    public static Clause notEndWith(String column, String pattern) {
        return notLike(column, '%' + pattern);
    }

    public static Clause in(String column, Object... pattern) {
        return new SingleClause(column, " IN(", pattern, ")");
    }

    public static Clause in(String column, Collection<?> pattern) {
        return new SingleClause(column, " IN(", pattern, ")");
    }

    public static Clause in(String column, Select pattern) {
        return new SingleClause(column, " IN ", pattern);
    }

    public static Clause notIn(String column, Object... pattern) {
        return new SingleClause(column, " NOT IN(", pattern, ")");
    }

    public static Clause notIn(String column, Collection<?> pattern) {
        return new SingleClause(column, " NOT IN(", pattern, ")");
    }

    public static Clause notIn(String column, Select pattern) {
        return new SingleClause(column, " NOT IN ", pattern);
    }

    public static Clause between(String column, Object start, Object end) {
        return new SingleClause(column, " BETWEEN ", start, " AND ", end).setValues(start, end);
    }

    public static Clause parse(String sql) {
        return new SingleClause(null, sql);
    }

    public static Clause andAll(List<Clause> list) {
        switch (list.size()) {
            case 0:
                return null;
            case 1:
                return list.get(0);
            default:
                MultiClause clause = null;
                for (Clause li : list) {
                    if (clause == null) {
                        clause = new MultiClause(li);
                    } else {
                        clause.and(li);
                    }
                }
                return clause;
        }
    }

    public static Clause orAll(List<Clause> list) {
        switch (list.size()) {
            case 0:
                return null;
            case 1:
                return list.get(0);
            default:
                MultiClause clause = null;
                for (Clause li : list) {
                    if (clause == null) {
                        clause = new MultiClause(li);
                    } else {
                        clause.or(li);
                    }
                }
                return clause;
        }
    }
}
