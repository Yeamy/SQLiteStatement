package yeamy.sqlite.statement.columninfo;

import yeamy.sqlite.statement.CreateTable;
import yeamy.sqlite.statement.SQLString;

/**
 * @see CreateTable
 * @see CharColumn
 * @see TextColumn
 * @see IntColumn
 * @see RealColumn
 * @see BlobColumn
 * @see NullColumn
 */
public abstract class ColumnInfo<T extends ColumnInfo<T>> implements SQLString {

	public static final Object NO_DEFAULT = "NO_DEFAULT";

	public final String name;
	private boolean primary, notNull, unique;
	private Object _default = NO_DEFAULT;
	protected boolean increment;

	public ColumnInfo(String name) {
		this.name = name;
	}

	public T primaryKey() {
		primary = true;
		return notNull();
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	@SuppressWarnings("unchecked")
	public T notNull() {
		notNull = true;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T defaultValue(Object _default) {
		this._default = _default;
		return (T) this;
	}

	protected abstract void dataType(StringBuilder sql);

	@Override
	public void toSQL(StringBuilder sql) {
		SQLString.appendColumn(sql, name);
		sql.append(' ');
		dataType(sql);
		if (primary) {
			sql.append(" PRIMARY KEY");
		}
		if (notNull) {
			sql.append(" NOT NULL");
		}
		if (increment) {// mysql
			sql.append(" AUTOINCREMENT");
		}
		if (_default != NO_DEFAULT) {// mysql
			sql.append(" DEFAULT ");
			SQLString.appendValue(sql, _default);
		}
		if (unique) {
			sql.append(" UNIQUE");
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toSQL(sb);
		return sb.toString();
	}
}