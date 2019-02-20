package org.monet.bpi;

import java.util.HashMap;

public class OperationExpression extends Expression {

	public static final String EQ = "=";
	public static final String NE = "<>";
	public static final String GT = ">";
	public static final String LT = "<";
	public static final String GE = ">=";
	public static final String LE = "<=";
	public static final String LIKE = "LIKE";

	private static final String ExtendedPattern = "((%s %s %s) OR (%s %s %s))";
	private static final String Pattern = "(%s %s %s)";

	private Param leftParam;
	private Param rightParam;
	private String op;

	public OperationExpression(Param leftParam, String op, Param rightParam) {
		super(null);
		this.leftParam = leftParam;
		this.op = op;
		this.rightParam = rightParam;
	}

	public String toString(IndexGenerator idx) {
		String leftColumn = null, leftColumnExtended = null;

		if (this.leftParam.isValueParam()) {
			if (!this.leftParam.isTimestamp())
				leftColumn = this.leftParam.getName(idx.next());
			if (this.leftParam.isExtended())
				leftColumnExtended = this.leftParam.getName(idx.next());
		}
		else {
			if (!this.leftParam.isTimestamp())
				leftColumn = this.leftParam.getColumn();
			if (this.leftParam.isExtended())
				leftColumnExtended = this.leftParam.getColumnExtended();
		}

		String rightColumn = null, rightColumnExtended = null;
		if (this.rightParam.isValueParam()) {
			if (!this.rightParam.isTimestamp())
				rightColumn = this.rightParam.getName(idx.next());
			if (this.rightParam.isExtended())
				rightColumnExtended = this.rightParam.getName(idx.next());
		}
		else {
			if (!this.rightParam.isTimestamp())
				rightColumn = this.rightParam.getColumn();
			if (this.rightParam.isExtended())
				rightColumnExtended = this.rightParam.getColumnExtended();
		}


		if (leftColumnExtended != null && rightColumnExtended != null) {
			if (this.leftParam.isTimestamp())
				return String.format(Pattern, leftColumnExtended, this.op, rightColumnExtended);

			return String.format(ExtendedPattern, leftColumn, this.op, rightColumn, leftColumnExtended, this.op, rightColumnExtended);
		}

		return String.format(Pattern, leftColumn, this.op, rightColumn);
	}

	public HashMap<String, Object> getParameters(IndexGenerator idx) {
		HashMap<String, Object> values = new HashMap<String, Object>();

		if (this.leftParam.isValueParam()) {
			if (!this.leftParam.isTimestamp())
				values.put(this.leftParam.getParamName(idx.next()), this.leftParam.getValue());
			if (this.leftParam.isExtended())
				values.put(this.leftParam.getParamName(idx.next()), this.leftParam.getValueExtended());
		}

		if (this.rightParam.isValueParam()) {
			if (!this.rightParam.isTimestamp())
				values.put(this.rightParam.getParamName(idx.next()), valueOf(this.rightParam.getValueAsString()));
			if (this.rightParam.isExtended())
				values.put(this.rightParam.getParamName(idx.next()), this.rightParam.getValueExtended());
		}

		return values;
	}

	private Object valueOf(Object value) {
		if (!this.op.equals(LIKE)) return value;
		return "%" + value + "%";
	}
}
