package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.Column;
import yeamy.sqlite.statement.Searchable;

public class Min extends Column {

	public Min(String name) {
		super(name);
	}

	public Min(String table, String name) {
		super(table, name);
	}

	public Min(Searchable<?> table, String tableAlias, String name) {
		super(table, tableAlias, name);
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append("MIN(");
		super.toSQL(sql);
		sql.append(')');
	}
}