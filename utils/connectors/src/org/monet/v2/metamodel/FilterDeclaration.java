package org.monet.v2.metamodel;


import org.monet.v2.model.LibraryString;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// FilterDeclaration
// Se utiliza para indicar un filtro sobre el valor de un atributo

public class FilterDeclaration extends FilterDeclarationBase {

  public String getValue(HashMap<String, String> parameters) {
    Pattern pattern = Pattern.compile("parameter\\(([^\\)]*)\\)");
    Matcher matcher;
    String result = this.getValue();

    matcher = pattern.matcher(result);
    while (matcher.find()) {
      String name = matcher.group(1);
      String value = (parameters.containsKey(name) ? parameters.get(name) : "");
      result = LibraryString.replaceAll(result, matcher.group(0), value);
    }

    return result;
  }

  public String getOperatorAsString() {
    if (_operator == null)
      return "=";
    switch (_operator) {
      case GE:
        return ">=";
      case GT:
        return ">";
      case LE:
        return "<=";
      case LT:
        return "<";
      case NEQ:
        return "<>";
      default:
        return "=";
    }
  }

}
