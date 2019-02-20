package org.monet.space.kernel.sql;

import org.monet.space.kernel.exceptions.DataException;

public class QueryBuilder {

	StringBuilder queryBuilder = new StringBuilder();

	public QueryBuilder(String baseQuery) {
		this.queryBuilder.append(baseQuery);
	}

	public void insertSubQuery(String name, String subQuery) {
		if (subQuery == null) subQuery = "";
		int index = this.queryBuilder.indexOf("::" + name + "::");
		if (index < 0) throw new DataException(String.format("Subquery %s doesn't exists", name), name);
		do {
			this.queryBuilder.replace(index, index + name.length() + 4, subQuery);
		} while ((index = this.queryBuilder.indexOf("::" + name + "::", index + subQuery.length())) > -1);
	}

	public String build() {
		return this.queryBuilder.toString();
	}
}
