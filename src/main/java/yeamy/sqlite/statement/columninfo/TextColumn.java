package yeamy.sqlite.statement.columninfo;

import yeamy.sqlite.statement.CreateTable;

/**
 * text column
 * @see CreateTable
 */
public class TextColumn extends ColumnInfo<TextColumn> {

	public TextColumn(String name) {
		super(name);
	}

	@Override
	protected void dataType(StringBuilder sql) {
		sql.append("TEXT");
	}

}
