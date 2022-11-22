package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.Column;
import yeamy.sqlite.statement.Searchable;

public class Sum extends Column {

	public Sum(String name) {
		super(name);
	}

	public Sum(String table, String name) {
		super(table, name);
	}

	public Sum(Searchable<?> table, String tableAlias, String name) {
		super(table, tableAlias, name);
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append("SUM(");
		super.toSQL(sql);
		sql.append(')');
	}

}