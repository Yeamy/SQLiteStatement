package yeamy.sqlite.statement;

public class On {
	private final Select select;
	private final Join join;

	public On(Select select, Join join) {
		this.select = select;
		this.join = join;
	}

	public Select on(Clause clause) {
		join.on(clause);
		return select;
	}

	public Select on(String src, String dest) {
		String srcTable = (select.alias != null) ? select.alias : select.from.toString();
		String destTable = (join.alias != null) ? join.alias : join.table.toString();
		return on(Clause.equals(new Column(srcTable, src), new Column(destTable, dest)));
	}
}
