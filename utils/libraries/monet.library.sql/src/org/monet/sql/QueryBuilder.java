package org.monet.sql;

public class QueryBuilder {

  StringBuilder queryBuilder = new StringBuilder();
  
  public QueryBuilder(String baseQuery) {
    this.queryBuilder.append(baseQuery);
  }
  
  public void insertSubQuery(String name, String subQuery) throws Exception {
    if(subQuery == null) subQuery = "";
    int index = this.queryBuilder.indexOf("::" + name + "::");
    if(index < 0) throw new Exception(String.format("Subquery %s doesn't exists", name));
    do {
      this.queryBuilder.replace(index, index+name.length()+4, subQuery);
    } while((index = this.queryBuilder.indexOf("::" + name + "::", index+subQuery.length())) > -1);
  }
  
  public String build() {
    return this.queryBuilder.toString();
  }
}
