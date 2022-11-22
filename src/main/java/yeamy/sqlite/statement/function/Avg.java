package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.Column;
import yeamy.sqlite.statement.Searchable;

/**
 * average
 */
public class Avg extends Column {

	public Avg(String name) {
		super(name);
	}

	public Avg(String table, String name) {
		super(table, name);
	}

	public Avg(Searchable<?> table, String tableAlias, String name) {
		super(table, tableAlias, name);
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append("AVG(");
		super.toSQL(sql);
		sql.append(')');
	}

}
