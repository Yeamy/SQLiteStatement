package yeamy.sqlite.statement;

import java.util.ArrayList;

/**
 * @see Clause
 */
public class MultiClause extends Clause {
	private final Clause clause;
	private final ArrayList<ClauseLi> clauses = new ArrayList<>();

	public MultiClause(Clause clause) {
		this.clause = clause;
	}

	private static class ClauseLi {
		String logic;
		Clause clause;

		private ClauseLi(String logic, Clause clause) {
			this.logic = logic;
			this.clause = clause;
		}
	}

	@Override
	public final void toSQL(StringBuilder sql) {
		if (clauses.size() == 0) {
			this.clause.toSQL(sql);
			return;
		}
		sql.append('(');
		this.clause.toSQL(sql);
		for (ClauseLi li : clauses) {
			sql.append(li.logic);
			li.clause.toSQL(sql);
		}
		sql.append(')');
	}

	void add(String logic, Clause clause) {
		clauses.add(new ClauseLi(logic, clause));
	}
}