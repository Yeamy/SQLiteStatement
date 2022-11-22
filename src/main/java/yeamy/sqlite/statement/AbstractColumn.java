package yeamy.sqlite.statement;

/**
 * Virtual, Column, Clause and class in package yeamy.sqlite.statement.function, such as Now, Sum
 * @see Virtual
 * @see Column
 * @see Clause
 */
public abstract class AbstractColumn<T extends AbstractColumn<T>> implements SQLString {
	protected String nameAlias;

	@SuppressWarnings("unchecked")
	public T as(String nameAlias) {
		this.nameAlias = nameAlias;
		return (T) this;
	}

	/**
	 * tableAlias.name AS alias -> table.name AS alias
	 */
	public void nameInColumn(StringBuilder sql) {
		toSQL(sql);
		if (nameAlias != null) {
			sql.append(" AS ");
			SQLString.appendColumn(sql, nameAlias);
		}
	}

	public void nameInGroup(StringBuilder sql) {
		nameInSort(sql);
	}

	public void nameInInsert(StringBuilder sql) {
		this.nameInSort(sql);
	}

	public void nameInSort(StringBuilder sql) {
		if (nameAlias != null) {
			SQLString.appendColumn(sql, nameAlias);
		} else {
			toSQL(sql);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toSQL(sb);
		return sb.toString();
	}

}