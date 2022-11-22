package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.Column;
import yeamy.sqlite.statement.Searchable;

public class Lower extends Column {

	public Lower(String name) {
		super(name);
	}

	public Lower(String table, String name) {
		super(table, name);
	}

	public Lower(Searchable<?> table, String tableAlias, String name) {
		super(table, tableAlias, name);
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append("LOWER(");
		super.toSQL(sql);
		sql.append(')');
	}

}
