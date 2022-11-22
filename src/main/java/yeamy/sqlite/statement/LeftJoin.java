package yeamy.sqlite.statement;

public class LeftJoin extends Join {

	LeftJoin(Object table, String alias) {
		super(" LEFT OUTER JOIN ", table, alias);
	}

	public LeftJoin(String table) {
		super(" LEFT OUTER JOIN ", table, null);
	}

	public LeftJoin(String table, String alias) {
		super(" LEFT OUTER JOIN ", table, alias);
	}

	public LeftJoin(Searchable<?> table, String alias) {
		super(" LEFT OUTER JOIN ", table, alias);
	}

}