package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.Column;
import yeamy.sqlite.statement.Searchable;

/**
 * Absolute value
 */
public class Abs extends Column {

	public Abs(String name) {
		super(name);
	}

	public Abs(String table, String name) {
		super(table, name);
	}

	public Abs(Searchable<?> table, String tableAlias, String name) {
		super(table, tableAlias, name);
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append("ABS(");
		super.toSQL(sql);
		sql.append(')');
	}

}
