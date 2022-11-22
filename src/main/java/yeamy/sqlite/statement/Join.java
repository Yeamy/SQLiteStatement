package yeamy.sqlite.statement;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * @see LeftJoin
 * @see InnerJoin
 * @see CrossJoin
 * @author Yeamy0754
 *
 */
public abstract class Join implements SQLString {
	private Clause clause;
	final String type;
	final Object table;
	String alias;
	private final LinkedHashSet<Object> columns = new LinkedHashSet<>();

	Join(String type, Object table, String alias) {
		this.type = type;
		this.table = table;
		this.alias = alias;
	}

	public Join add(String column) {
		columns.add(column);
		return this;
	}

	public Join add(String column, String alias) {
		columns.add(new Column(column).as(alias).setTable(table, this.alias));
		return this;
	}

	public Join add(AbstractColumn<?> column) {
		columns.add(column);
		return this;
	}

	public Join addAll(Collection<AbstractColumn<?>> columns) {
		this.columns.addAll(columns);
		return this;
	}

	public Join addAll(AbstractColumn<?>... column) {
		Collections.addAll(columns, column);
		return this;
	}

	public Join addAll(String... column) {
		Collections.addAll(columns, column);
		return this;
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append(type);
		if (table instanceof String) {
			SQLString.appendTable(sql, (String) table);
		} else {
			SQLString.appendValue(sql, (Searchable<?>) table);
		}
		if (alias != null) {
			sql.append(" AS ");
			SQLString.appendTable(sql, alias);
		}
		sql.append(" ON ");
		clause.toSQL(sql);
	}

	void on(Clause clause) {
		this.clause = clause;
	}

	void columns(StringBuilder sql) {
		boolean f = true;
		for (Object column : columns) {
			if (f) {
				f = false;
			} else {
				sql.append(", ");
			}
			if (column instanceof String) {
				if (alias != null) {
					sql.append('`').append(alias).append('`').append('.');
				} else if (table != null && table instanceof String) {
					sql.append('`').append(table).append('`').append('.');
				}
				if ("*".equals(column)) {
					sql.append('*');
				} else {
					sql.append('`').append(column).append('`');
				}
			}
			if (column instanceof Column) {
				Column c = (Column) column;
				if (c.table == null) {
					c.setTable(table, alias);
				}
				c.nameInColumn(sql);
			} else if (column instanceof AbstractColumn<?>) {
				((AbstractColumn<?>) column).nameInColumn(sql);
			} else {
				SQLString.appendColumn(sql, column.toString());
			}
		}
	}

}