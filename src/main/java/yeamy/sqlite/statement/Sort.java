package yeamy.sqlite.statement;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * order by e.g. new Asc("column1").desc("column2")
 * @see Select
 * @see Asc
 * @see Desc
 * @author Yeamy0754
 */
public abstract class Sort implements SQLString {
	static final String ASC = "ASC";
	static final String DESC = "DESC";

	private final LinkedHashMap<Object, String> sort = new LinkedHashMap<>();

	Sort(AbstractColumn<?> column, String sort) {
		this.sort.put(column, sort);
	}

	Sort(String column, String sort) {
		this.sort.put(column, sort);
	}

	public Sort asc(AbstractColumn<?> column) {
		sort.put(column, ASC);
		return this;
	}

	public Sort desc(AbstractColumn<?> column) {
		sort.put(column, DESC);
		return this;
	}

	public Sort asc(String column) {
		sort.put(column, ASC);
		return this;
	}

	public Sort desc(String column) {
		sort.put(column, DESC);
		return this;
	}

	@Override
	public void toSQL(StringBuilder sql) {
		if (sort.size() == 0) {
			return;
		}
		sql.append(" ORDER BY ");
		boolean f = true;
		Set<Entry<Object, String>> set = sort.entrySet();
		for (Entry<Object, String> cell : set) {
			if (f) {
				f = false;
			} else {
				sql.append(", ");
			}
			Object column = cell.getKey();
			if (column instanceof AbstractColumn<?>) {
				AbstractColumn<?> c = (AbstractColumn<?>) column;
				c.nameInSort(sql);
			} else {
				SQLString.appendColumn(sql, column.toString());
			}
			String sc = cell.getValue();
			if (sc != null) {
				sql.append(" ");
				sql.append(sc);
			}
		}
	}

}
