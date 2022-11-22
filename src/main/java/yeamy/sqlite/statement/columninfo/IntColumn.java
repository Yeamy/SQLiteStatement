package yeamy.sqlite.statement.columninfo;

import yeamy.sqlite.statement.CreateTable;

/**
 * integer column
 * @see CreateTable
 */
public class IntColumn extends ColumnInfo<IntColumn> {

	public IntColumn(String name) {
		super(name);
	}

	public IntColumn autoIncrement() {
		increment = true;
		return this;
	}

	@Override
	protected void dataType(StringBuilder sql) {
		sql.append("INTEGER");
	}
}