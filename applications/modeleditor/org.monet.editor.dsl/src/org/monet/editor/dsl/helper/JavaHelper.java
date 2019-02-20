package org.monet.editor.dsl.helper;

import org.eclipse.xtext.naming.QualifiedName;

public class JavaHelper {
  
  public static String toInvertedName(QualifiedName qualifiedName) {
    StringBuilder builder = new StringBuilder();
    for(String segment : qualifiedName.getSegments())
      builder.insert(0, toJavaIdentifier(segment));
      
    return builder.toString();
  }
  
  public static String toJavaIdentifier(String aString) {
    if(aString == null)
      return null;
    String identifier = toAttributeJavaIdentifier(aString);
    if(identifier.length() > 0)
      identifier = Character.toUpperCase(identifier.charAt(0)) + identifier.substring(1);
    return identifier;
  }
  
  public static String toAttributeJavaIdentifier(String aString) {
    StringBuffer res = new StringBuffer();
    int idx = 0;
    char c;
    boolean toUpper = false;
    boolean isFirst = true;
    if(aString == null)
      return null;
    while (idx < aString.length()) {  
      c = aString.charAt(idx++);
      
      if (Character.isJavaIdentifierPart(c)){
        if(toUpper) {
          c = Character.toUpperCase(c);
          toUpper = false;
        }
        if(isFirst) {
          c = Character.toLowerCase(c);
          isFirst = false;
        }
        res.append(c);
      } else {
        toUpper = true;
      }
    }
    return res.toString();
  }
  
  public static String toStringLiteral(String value) {
    value = value.replaceAll("\\\\", "\\\\\\\\");
    value = value.replaceAll("\\\"", "\\\\\"");
    value = value.replaceAll("\n", "\\\\n");
    value = value.replaceAll("\r", "");
    return value;
  }
  
}
