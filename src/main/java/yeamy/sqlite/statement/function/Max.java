package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.Column;
import yeamy.sqlite.statement.Searchable;

public class Max extends Column {

	public Max(String name) {
		super(name);
	}

	public Max(String table, String name) {
		super(table, name);
	}

	public Max(Searchable<?> table, String tableAlias, String name) {
		super(table, tableAlias, name);
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append("MAX(");
		super.toSQL(sql);
		sql.append(')');
	}
}