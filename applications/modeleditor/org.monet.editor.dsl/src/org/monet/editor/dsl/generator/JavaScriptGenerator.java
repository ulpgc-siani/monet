package org.monet.editor.dsl.generator;

import java.util.Iterator;

import org.monet.editor.dsl.monetModelingLanguage.Definition;
import org.monet.editor.dsl.monetModelingLanguage.Property;

public class JavaScriptGenerator {

  public static String buildClientBehaviour(Definition definition, Iterator<Property> rules) {
    StringBuilder x = new StringBuilder();
    String context = inferConstructor(x, definition);
    inferRefresh(x, context, definition);
    inferOnChangeField(x, context, definition);
    return x.toString();
  }
  
  private static String inferConstructor(StringBuilder x, Definition definition) {
    String context = definition.getCode().getValue();
    x.append("var ");
    x.append(context);
    x.append(" = new Object();");
    return context;
  }
  
  private static String inferRefresh(StringBuilder x, String context, Definition definition) {
    x.append(context);
    x.append(".refresh = function (node) {");
    x.append("if (!node.isEditable()) return;");
    //TODO
    x.append("};");
    return context;
  }
  
  private static String inferOnChangeField(StringBuilder x, String context, Definition definition) {
    x.append(context);
    x.append(".onChangeField = function (node, field) {");
    //TODO
    x.append("};");
    return context;
  }
  
}
