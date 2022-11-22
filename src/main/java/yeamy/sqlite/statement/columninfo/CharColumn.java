package yeamy.sqlite.statement.columninfo;

import yeamy.sqlite.statement.CreateTable;

/**
 * char column
 * @see CreateTable
 */
public class CharColumn extends ColumnInfo<CharColumn> {
	private final int length;

	/**
	 * @param name column name
	 * @param length length of column data, 1 ~ 254
	 */
	public CharColumn(String name, int length) {
		super(name);
		this.length = length;
	}

	@Override
	protected void dataType(StringBuilder sql) {
		sql.append("CHAR").append('(').append(length).append(')');
	}

}