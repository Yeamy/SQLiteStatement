package yeamy.sqlite.statement.columninfo;

import yeamy.sqlite.statement.CreateTable;

/**
 * blob column
 * @see CreateTable
 */
public class BlobColumn extends ColumnInfo<BlobColumn> {

	public BlobColumn(String name) {
		super(name);
	}

	@Override
	protected void dataType(StringBuilder sql) {
		sql.append("BLOB");
	}

}