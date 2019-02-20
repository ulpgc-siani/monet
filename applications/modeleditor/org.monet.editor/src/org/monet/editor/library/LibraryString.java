package org.monet.editor.library;

public class LibraryString {
  
  public String capitalize(String source){
    String target = source.substring(0, 1).toUpperCase() + source.substring(1);
    return target;
  }
  
  public String replace(String source, String replace, String replacement){
    return source.replaceAll(replace, replacement);
  }
  
  public static String toJavaIdentifier(String aString) {
    String identifier = toAttributeJavaIdentifier(aString);
    return Character.toUpperCase(identifier.charAt(0)) + identifier.substring(1);
  }
  
  public static String toAttributeJavaIdentifier(String aString) {
    StringBuffer res = new StringBuffer();
    int idx = 0;
    char c;
    boolean toUpper = false;
    while (idx < aString.length()) {  
      c = aString.charAt(idx++);
      
      if (Character.isJavaIdentifierPart(c)){
        if(toUpper) {
          c = Character.toUpperCase(c);
          toUpper = false;
        }
        res.append(c);
      } else {
        toUpper = true;
      }
    }
    return res.toString();
  }
  
  public static boolean isValidIdentifier(String identifier){
    boolean isValid = true;
    int idx = 0;
    char c;
    while(idx < identifier.length()) {
      c = identifier.charAt(idx);
      
      if(!Character.isJavaIdentifierPart(c))
        isValid = false;
      
      idx++;
    }
    
    return isValid;
  }
  
}
