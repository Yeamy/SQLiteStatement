package yeamy.sqlite.statement.columninfo;

public class RealColumn extends ColumnInfo<RealColumn> {
	private final int length;

	/**
	 * @param name column name
	 * @param length length of column data, 1 ~ 254
	 */
	public RealColumn(String name, int length) {
		super(name);
		this.length = length;
	}

	@Override
	protected void dataType(StringBuilder sql) {
		sql.append("REAL").append('(').append(length).append(')');
	}

}
