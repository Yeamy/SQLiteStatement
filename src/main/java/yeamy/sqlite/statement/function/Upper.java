package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.Column;
import yeamy.sqlite.statement.Searchable;

public class Upper extends Column {

	public Upper(String name) {
		super(name);
	}

	public Upper(String table, String name) {
		super(table, name);
	}

	public Upper(Searchable<?> table, String tableAlias, String name) {
		super(table, tableAlias, name);
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append("UPPER(");
		super.toSQL(sql);
		sql.append(')');
	}

}
