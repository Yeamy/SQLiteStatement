package yeamy.sqlite.statement;

/**
 * Virtual column
 * @see Select
 */
public class Virtual extends AbstractColumn<Virtual> {
	private final Object value;

	public Virtual(Object value, String nameAlias) {
		this.value = value;
		this.nameAlias = nameAlias;
	}

	@Override
	public void toSQL(StringBuilder sql) {
		SQLString.appendValue(sql, value);
	}

}
