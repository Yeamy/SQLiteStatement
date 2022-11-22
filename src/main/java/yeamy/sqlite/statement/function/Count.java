package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.Column;
import yeamy.sqlite.statement.Searchable;

public class Count extends Column {

	public Count(String name) {
		super(name);
	}

	public Count(String table, String name) {
		super(table, name);
	}

	public Count(Searchable<?> table, String tableAlias, String name) {
		super(table, tableAlias, name);
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append("COUNT(");
		super.toSQL(sql);
		sql.append(')');
	}

}
