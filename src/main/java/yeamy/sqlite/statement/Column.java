package yeamy.sqlite.statement;

/**
 * table column
 */
public class Column extends AbstractColumn<Column> {
	public static final String ALL = "*";
	public final String name;
	public Object table;
	public String tableAlias;

	/**
	 * @param name column label / name
	 */
	public Column(String name) {
		this.table = null;
		this.name = name;
	}

	public Column(String table, String name) {
		this.table = table;
		this.name = name;
	}

	public Column(Searchable<?> table, String tableAlias, String name) {
		this.table = table;
		this.tableAlias = tableAlias;
		this.name = name;
	}

	Column setTable(Object table, String tableAlias) {
		this.table = table;
		this.tableAlias = tableAlias;
		return this;
	}

	public Column as(String tableAlias, String nameAlias) {
		this.tableAlias = tableAlias;
		this.nameAlias = nameAlias;
		return this;
	}

	public Column tableAs(String tableAlias) {
		this.tableAlias = tableAlias;
		return this;
	}

	@Override
	public void nameInInsert(StringBuilder sql) {
		if (nameAlias != null) {
			sql.append('`').append(nameAlias).append('`');
		} else if (name == null) {
			throw new NullPointerException("column name is null");
		} else {
			sql.append('`').append(name).append('`');
		}
	}

	@Override
	public void toSQL(StringBuilder sql) {
		if (name == null) {
			throw new NullPointerException("column name is null");
		}
		if (tableAlias != null) {
			sql.append('`').append(tableAlias).append('`').append('.');
		} else if (table != null && table instanceof String) {
			sql.append('`').append(table).append('`').append('.');
		}
		if ("*".equals(name)) {
			sql.append('*');
		} else {
			sql.append('`').append(name).append('`');
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Column) {
			Column t = (Column) obj;
			return compare(name, t.name)//
					&& compare(table, t.table)//
					&& compare(nameAlias, t.nameAlias)//
					&& compare(tableAlias, t.tableAlias);
		}
		return super.equals(obj);
	}

	private boolean compare(Object a, Object b) {
		if (a == null) {
			return b == null;
		}
		return a.equals(b);
	}
}