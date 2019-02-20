package org.simpleframework.xml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface Attribute {
  public String name() default "";
  public String empty() default "";
  public boolean required() default true;
}
