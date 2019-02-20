package org.simpleframework.xml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface Element {
  public String name() default "";
  public boolean data() default false;
  public boolean required() default true;
}
