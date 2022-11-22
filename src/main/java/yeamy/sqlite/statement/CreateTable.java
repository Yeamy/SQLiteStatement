package yeamy.sqlite.statement;

import java.util.ArrayList;

import yeamy.sqlite.statement.columninfo.ColumnInfo;

public class CreateTable implements SQLString {
	private String database;
	private final String table;
	private final ArrayList<ColumnInfo<?>> columns = new ArrayList<>();
	private boolean ifNotExists = true;

	public CreateTable(String database, String table) {
		this.database = database;
		this.table = table;
	}

	public CreateTable(String table) {
		this.table = table;
	}

	public CreateTable add(ColumnInfo<?> info) {
		columns.add(info);
		return this;
	}

	public CreateTable ifNotExists(boolean ifNotExists) {
		this.ifNotExists = ifNotExists;
		return this;
	}

	@Override
	public void toSQL(StringBuilder sql) {
		sql.append("CREATE TABLE ");
		if (ifNotExists) {
			sql.append("IF NOT EXISTS ");
		}
		if (database != null) {
			SQLString.appendDatabase(sql, database);
			sql.append('.');
		}
		SQLString.appendTable(sql, table);
		sql.append('(');
		// columnse
		boolean first = true;
		for (ColumnInfo<?> column : columns) {
			if (first) {
				first = false;
			} else {
				sql.append(" ,");
			}
			column.toSQL(sql);
		}
		sql.append(')');
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder();
		toSQL(sql);
		sql.append(';');
		return sql.toString();
	}

}
