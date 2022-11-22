package yeamy.sqlite.statement;

/**
 * cross join table
 */
public class CrossJoin extends Join {

	CrossJoin(Object table, String alias) {
		super(" CROSS JOIN ", table, alias);
	}

	public CrossJoin(String table) {
		super(" CROSS JOIN ", table, null);
	}

	public CrossJoin(String table, String alias) {
		super(" CROSS JOIN ", table, alias);
	}

	public CrossJoin(Searchable<?> table, String alias) {
		super(" CROSS JOIN ", table, alias);
	}

}