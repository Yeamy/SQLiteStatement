package yeamy.sqlite.statement.columninfo;

import yeamy.sqlite.statement.CreateTable;

/**
 * null column
 * @see CreateTable
 */
public class NullColumn extends ColumnInfo<NullColumn> {

	public NullColumn(String name) {
		super(name);
	}

	@Override
	protected void dataType(StringBuilder sql) {
		sql.append("NULL");
	}

}