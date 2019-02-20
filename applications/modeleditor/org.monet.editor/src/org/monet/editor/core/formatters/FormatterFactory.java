package org.monet.editor.core.formatters;

import java.util.HashMap;
import java.util.Map;

public class FormatterFactory {
  private Map<Type, Formatter> formatters;
  
  private static FormatterFactory instance = null;
  
  public enum Type {
    Xtext, Other
  }
  
  public static FormatterFactory getInstance() {
    if (instance == null)
      instance = new FormatterFactory();
    return instance;
  }
  
  private FormatterFactory() {
    init();
  }
  
  private void init() {
    formatters = new HashMap<Type, Formatter>();
    formatters.put(Type.Xtext, new XtextFormatter());
    formatters.put(Type.Other, new OtherFormatter());
  }
  
  public Formatter create(String extension) {
    
    if (extension.equalsIgnoreCase("mml"))
      return formatters.get(Type.Xtext);
    
    return formatters.get(Type.Other);
  }

}
