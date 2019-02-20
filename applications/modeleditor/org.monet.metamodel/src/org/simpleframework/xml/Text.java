package org.simpleframework.xml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface Text {
  public String empty() default "";
  public boolean data() default false;
  public boolean required() default true;
}
