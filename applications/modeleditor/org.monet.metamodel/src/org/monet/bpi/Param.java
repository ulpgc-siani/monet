package org.monet.bpi;

import java.sql.Timestamp;
import java.util.Date;

import org.monet.bpi.types.File;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Picture;
import org.monet.bpi.types.Term;

public class Param {

  private boolean isValueParam = false;
  private String name;
  private Object value;
  
  public Param(String name) {
    this.name = name;
  }
  
  public Param(String name, Object value) {
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

  public boolean hasSuffix() {
    return value instanceof Timestamp || value instanceof Term || value instanceof Link || value instanceof Picture || value instanceof File;
  }

  public String getColumn() {
    return name + (this.hasSuffix() ? "$ex" : "");
  }
  
  public boolean isValueParam() {
    return this.isValueParam;
  }
  
  public Object getValue() {
    return this.value;
  }
  
  //Like
  
  public OperationExpression Like(String value) {
    return new OperationExpression(this, OperationExpression.LIKE, new Param(this.name, value));
  }
  
  //Equals
  
  public OperationExpression Eq(Param param) {
    return new OperationExpression(this, OperationExpression.EQ, param);
  }
  
  public OperationExpression Eq(Enum<?> value) {
    return Eq(new Param(this.name, value.toString()));
  }
  
  public OperationExpression Eq(String value) {
    return Eq(new Param(this.name, value));
  }
  
  public OperationExpression Eq(Date value) {
    return Eq(new Param(this.name, value));
  }
  
  public OperationExpression Eq(int value) {
    return Eq(new Param(this.name, value));
  }
  
  public OperationExpression Eq(Term value) {
    return Eq(new Param(this.name, value));
  }

  public OperationExpression Eq(Link value) {
    return Eq(new Param(this.name, value));
  }

  //Greater equals
  
  public OperationExpression Ge(Param param) {
    return new OperationExpression(this, OperationExpression.GE, param);
  }
  
  public OperationExpression Ge(String value) {
    return Ge(new Param(this.name, value));
  }
  
  public OperationExpression Ge(Date value) {
    return Ge(new Param(this.name, value));
  }
  
  public OperationExpression Ge(int value) {
    return Ge(new Param(this.name, value));
  }
  
  //Greater than
  
  public OperationExpression Gt(Param param) {
    return new OperationExpression(this, OperationExpression.GT, param);
  }
  
  public OperationExpression Gt(String value) {
    return Gt(new Param(this.name, value));
  }
  
  public OperationExpression Gt(Date value) {
    return Gt(new Param(this.name, value));
  }
  
  public OperationExpression Gt(int value) {
    return Gt(new Param(this.name, value));
  }
  
  //Less equals
  
  public OperationExpression Le(Param param) {
    return new OperationExpression(this, OperationExpression.LE, param);
  }
  
  public OperationExpression Le(String value) {
    return Le(new Param(this.name, value));
  }
  
  public OperationExpression Le(Date value) {
    return Le(new Param(this.name, value));
  }
  
  public OperationExpression Le(int value) {
    return Le(new Param(this.name, value));
  }

  //Less than
  
  public OperationExpression Lt(Param param) {
    return new OperationExpression(this, OperationExpression.LT, param);
  }
  
  public OperationExpression Lt(String value) {
    return Lt(new Param(this.name, value));
  }
  
  public OperationExpression Lt(Date value) {
    return Lt(new Param(this.name, value));
  }
  
  public OperationExpression Lt(int value) {
    return Lt(new Param(this.name, value));
  }

  //Not equals
  
  public OperationExpression Ne(Param param) {
    return new OperationExpression(this, OperationExpression.NE, param);
  }
  
  public OperationExpression Ne(Enum<?> value) {
    return Ne(new Param(this.name, value.toString()));
  }
  
  public OperationExpression Ne(String value) {
    return Ne(new Param(this.name, value));
  }
  
  public OperationExpression Ne(Date value) {
    return Ne(new Param(this.name, value));
  }
  
  public OperationExpression Ne(int value) {
    return Ne(new Param(this.name, value));
  }
  
  public OperationExpression Ne(Term value) {
    return Ne(new Param(this.name, value));
  }

  public OperationExpression Ne(Link value) {
    return Ne(new Param(this.name, value));
  }

}
