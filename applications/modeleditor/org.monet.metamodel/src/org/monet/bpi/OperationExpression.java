package org.monet.bpi;

import java.util.HashMap;

public class OperationExpression extends Expression {

  public static final String EQ   = "=";
  public static final String NE   = "<>";
  public static final String GT   = ">";
  public static final String LT   = "<";
  public static final String GE   = ">=";
  public static final String LE   = "<=";
  public static final String LIKE = "LIKE";

  private Param              leftParam;
  private Param              rightParam;
  private String             op;

  public OperationExpression(Param leftParam, String op, Param rightParam) {
    super(null);
    this.leftParam = leftParam;
    this.op = op;
    this.rightParam = rightParam;
  }

  public String toString(IndexGenerator idx) {
    String leftColumn = null;
    if(this.leftParam.isValueParam())
      leftColumn = this.leftParam.getName(idx.next());
    else
      leftColumn = this.leftParam.getColumn();
    
    String rightColumn = null;
    if(this.rightParam.isValueParam())
      rightColumn = this.rightParam.getName(idx.next());
    else
      rightColumn = this.rightParam.getColumn();

    return String.format("(%s %s %s)", leftColumn, this.op, rightColumn);
  }

  public HashMap<String, Object> getParameters(IndexGenerator idx) {
    HashMap<String, Object> values = new HashMap<String, Object>();
    if (this.leftParam.isValueParam())
      values.put(this.leftParam.getParamName(idx.next()), this.leftParam.getValue());
    if (this.rightParam.isValueParam())
      values.put(this.rightParam.getParamName(idx.next()), this.rightParam.getValue());
    return values;
  }
}
