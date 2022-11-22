package yeamy.sqlite.statement;

/**
 * inner join table
 */
public class InnerJoin extends Join {

	InnerJoin(Object table, String alias) {
		super(" INNER JOIN ", table, alias);
	}

	public InnerJoin(String table) {
		super(" INNER JOIN ", table, null);
	}

	public InnerJoin(String table, String alias) {
		super(" INNER JOIN ", table, alias);
	}

	public InnerJoin(Searchable<?> table, String alias) {
		super(" INNER JOIN ", table, alias);
	}

}