package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.AbstractColumn;

public class Now extends AbstractColumn<Now> {
	public static final Now now = new Now(null);

	public Now(String alias) {
		as(alias);
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append("NOW()");
	}

}
