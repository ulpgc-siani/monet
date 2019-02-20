package org.monet.bpi;

import java.util.HashMap;
import java.util.Map.Entry;

public class Expression {

	public static final String AND = " AND ";
	public static final String OR = " OR ";

	private Expression[] expressions = null;
	private String op;

	public static class IndexGenerator {
		int value = 0;

		public String next() {
			return String.valueOf(this.value++);
		}
	}

	protected Expression(String op, Expression... args) {
		this.expressions = args;
		this.op = op;
	}

	public String toString() {
		return this.toString(new IndexGenerator());
	}

	public String toString(IndexGenerator idx) {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for (int i = 0; i < this.expressions.length; i++) {
			builder.append(this.expressions[i].toString(idx));
			builder.append(op);
		}
		builder.delete(builder.length() - this.op.length(), builder.length());
		builder.append(")");
		return builder.toString();
	}

	public HashMap<String, Object> getParameters() {
		return this.getParameters(new IndexGenerator());
	}

	public HashMap<String, Object> getParameters(IndexGenerator idx) {
		HashMap<String, Object> allValues = new HashMap<String, Object>();
		for (Expression expr : this.expressions) {
			HashMap<String, Object> values = expr.getParameters(idx);
			for (Entry<String, Object> entry : values.entrySet()) {
				allValues.put(entry.getKey(), entry.getValue());
			}
		}
		return allValues;
	}

	private static Expression build(String op, Expression expr1, Expression expr2, Expression... exprN) {
		Expression[] expressions = new Expression[exprN.length + 2];
		expressions[0] = expr1;
		expressions[1] = expr2;
		for (int i = 2; i < expressions.length; i++)
			expressions[i] = exprN[i - 2];
		return new Expression(op, expressions);
	}

	public static Expression And(Expression expr1, Expression expr2, Expression... exprN) {
		return build(AND, expr1, expr2, exprN);
	}

	public static Expression Or(Expression expr1, Expression expr2, Expression... exprN) {
		return build(OR, expr1, expr2, exprN);
	}
}
