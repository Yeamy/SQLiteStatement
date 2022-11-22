package yeamy.sqlite.statement;

/**
 * order by column asc
 * @see Select
 * @see Desc
 */
public class Asc extends Sort {
	public Asc(AbstractColumn<?> column) {
		super(column, ASC);
	}

	public Asc(String column) {
		super(column, ASC);
	}
}