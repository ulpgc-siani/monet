package org.simpleframework.xml;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
public @interface ElementList {
  public String name() default "";
  public String entry() default "";
  @SuppressWarnings("rawtypes")
  public Class type() default void.class;
  public boolean data() default false;
  public boolean required() default true;
  public boolean inline() default false;
  public boolean empty() default true;
}
