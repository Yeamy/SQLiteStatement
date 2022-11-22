package yeamy.sqlite.statement;

/**
 * order by column desc
 * @see Select
 * @see Asc
 */
public class Desc extends Sort {
	public Desc(AbstractColumn<?> column) {
		super(column, DESC);
	}

	public Desc(String column) {
		super(column, DESC);
	}
}