package yeamy.sqlite.statement;

/**
 * @see Select
 * @see Union
 */
public abstract class Searchable<T extends Searchable<T>> implements SQLString {
	private Sort orderBy;
	private int limitOffset = -1, limit = 0;

	/**
	 * @see Sort
	 * @see Asc
	 * @see Desc
	 */
	@SuppressWarnings("unchecked")
	public T orderBy(Sort orderBy) {
		this.orderBy = orderBy;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T limit(int limit) {
		this.limit = limit;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T limit(int limit, int offset) {
		this.limitOffset = offset;
		this.limit = limit;
		return (T) this;
	}

	// order by
	protected void orderBy(StringBuilder sql) {
		if (orderBy != null) {
			orderBy.toSQL(sql);
		}
	}

	// limit
	protected void limit(StringBuilder sql) {
		if (limit > 0) {
			sql.append(" LIMIT ");
			if (limitOffset >= 0) {
				sql.append(limitOffset).append(',');
			}
			sql.append(limit);
		}
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder();
		toSQL(sql);
		sql.append(';');
		return sql.toString();
	}

	public abstract Union union(String select);

	public abstract Union union(Select select);

	public abstract Union unionAll(Select select);

	public abstract Union unionAll(String select);
}