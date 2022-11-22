package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.Column;
import yeamy.sqlite.statement.Searchable;

public class Length extends Column {

	public Length(String name) {
		super(name);
	}

	public Length(String table, String name) {
		super(table, name);
	}

	public Length(Searchable<?> table, String tableAlias, String name) {
		super(table, tableAlias, name);
	}

	@Override
	public void toSQL(StringBuilder sb) {
		sb.append("LENGTH(");
		super.toSQL(sb);
		sb.append(')');
	}
}