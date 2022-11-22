package yeamy.sqlite.statement;

import java.util.ArrayList;

public class Union extends Searchable<Union> {

	private static class UnionLi {
		Object select;
		String logic;

		private UnionLi(Object select, String logic) {
			this.select = select;
			this.logic = logic;
		}
	}

	private final ArrayList<UnionLi> list = new ArrayList<>();

	public Union(String select) {
		list.add(new UnionLi(select, null));
	}

	public Union(Select select) {
		list.add(new UnionLi(select, null));
	}

	@Override
	public Union union(String select) {
		list.add(new UnionLi(select, " UNION "));
		return this;
	}

	@Override
	public Union union(Select select) {
		list.add(new UnionLi(select, " UNION "));
		return this;
	}

	@Override
	public Union unionAll(Select select) {
		list.add(new UnionLi(select, " UNION ALL "));
		return this;
	}

	@Override
	public Union unionAll(String select) {
		list.add(new UnionLi(select, " UNION ALL "));
		return this;
	}

	@Override
	public void toSQL(StringBuilder sql) {
		for (UnionLi li : list) {
			if (li.logic != null) {
				sql.append(li.logic);
			}
			if (li.select instanceof SQLString) {
				sql.append('(');
				((SQLString) li.select).toSQL(sql);
				sql.append(')');
			} else {
				sql.append('(').append(li.select).append(')');
			}
		}
		orderBy(sql);
		limit(sql);
	}

}
