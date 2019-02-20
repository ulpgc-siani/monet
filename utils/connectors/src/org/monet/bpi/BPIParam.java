package org.monet.bpi;

import java.sql.Timestamp;
import java.util.Date;

public class BPIParam {

  private boolean isValueParam = false;
  private String  name;
  private Object  value;

  public BPIParam(String name) {
    this.name = name;
  }

  public BPIParam(String name, Object value) {
    this.name = name;
    this.value = (value instanceof Date && value != null) ? new Timestamp(((Date)value).getTime()) : value;
    this.isValueParam = true;
  }

  public String getName(String idx) {
    return "@" + getParamName(idx);
  }

  public String getParamName(String idx) {
    return name + idx;
  }

  public String getColumn() {
    return name + (value instanceof Timestamp ? "$ex" : "");
  }

  public boolean isValueParam() {
    return this.isValueParam;
  }

  public Object getValue() {
    return this.value;
  }

  // Like
  
  public BPIOperationExpression Like(String value) {
    return new BPIOperationExpression(this, BPIOperationExpression.LIKE, new BPIParam(this.name, value));
  }
  
  // Equals

  public BPIOperationExpression Eq(BPIParam param) {
    return new BPIOperationExpression(this, BPIOperationExpression.EQ, param);
  }

  public BPIOperationExpression Eq(Enum<?> value) {
    return Eq(new BPIParam(this.name, value.toString()));
  }

  public BPIOperationExpression Eq(String value) {
    return Eq(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Eq(Date value) {
    return Eq(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Eq(int value) {
    return Eq(new BPIParam(this.name, value));
  }

  // Greater equals

  public BPIOperationExpression Ge(BPIParam param) {
    return new BPIOperationExpression(this, BPIOperationExpression.GE, param);
  }

  public BPIOperationExpression Ge(String value) {
    return Ge(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Ge(Date value) {
    return Ge(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Ge(int value) {
    return Ge(new BPIParam(this.name, value));
  }

  // Greater than

  public BPIOperationExpression Gt(BPIParam param) {
    return new BPIOperationExpression(this, BPIOperationExpression.GT, param);
  }

  public BPIOperationExpression Gt(String value) {
    return Gt(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Gt(Date value) {
    return Gt(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Gt(int value) {
    return Gt(new BPIParam(this.name, value));
  }

  // Less equals

  public BPIOperationExpression Le(BPIParam param) {
    return new BPIOperationExpression(this, BPIOperationExpression.LE, param);
  }

  public BPIOperationExpression Le(String value) {
    return Le(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Le(Date value) {
    return Le(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Le(int value) {
    return Le(new BPIParam(this.name, value));
  }

  // Less than

  public BPIOperationExpression Lt(BPIParam param) {
    return new BPIOperationExpression(this, BPIOperationExpression.LT, param);
  }

  public BPIOperationExpression Lt(String value) {
    return Lt(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Lt(Date value) {
    return Lt(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Lt(int value) {
    return Lt(new BPIParam(this.name, value));
  }

  // Not equals

  public BPIOperationExpression Ne(BPIParam param) {
    return new BPIOperationExpression(this, BPIOperationExpression.NE, param);
  }

  public BPIOperationExpression Ne(Enum<?> value) {
    return Ne(new BPIParam(this.name, value.toString()));
  }

  public BPIOperationExpression Ne(String value) {
    return Ne(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Ne(Date value) {
    return Ne(new BPIParam(this.name, value));
  }

  public BPIOperationExpression Ne(int value) {
    return Ne(new BPIParam(this.name, value));
  }
}
