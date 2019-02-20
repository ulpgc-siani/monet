package org.monet.metacompiler.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.monet.core.metamodel.MetaClass;
import org.monet.core.metamodel.MetaModel;
import org.monet.metacompiler.util.FileUtils;

public class MetaModelTranslator {

  private MetaModel metamodel;

  public enum TranslateMode {
    html, xsd, core_java, editor_java, editor_semantic, editor_sync_ddbb
  }

  public class JavaSyntax {
    private String capitalize(String name) {
      return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getToken(String name, boolean capitalize, boolean isMember) {
      name = name.substring(0, 1).toLowerCase() + name.substring(1);
      String[] names = name.split("-");
      String result = (capitalize) ? "" : names[0];
      int index = (capitalize) ? 0 : 1;
      for (int i = index; i < names.length; i++)
        result += capitalize(names[i]);
      if(isMember)
        result = "_" + result;
      return result;
    }

    public String getTokenMethod(String name, String parameters) {
      return "get" + getToken(name, true, false) + "(" + parameters + ")";
    }
    
    public String getTokenSetMethod(String name, String parameters) {
      return "set" + getToken(name, true, false) + "(" + parameters + ")";
    }

    public String getAttributeType(String type) {
      if (type.equals("language")) return "String";
      else if (type.equals("string") || type.equals("key") || type.equals("file") || type.equals("code")|| type.equals("formula")) return "String";
      else if (type.equals("natural") || type.equals("integer")) return "int";
      else if (type.equals("real")) return "double";
      else if (type.equals("boolean")) return "boolean";
      else return "unknown(" + type + ")";
    }

    public String getEnumeration(String values) {
      return values;
    }
  }
  
  public MetaModelTranslator(MetaModel metamodel) {
    this.metamodel = metamodel;
  }

  public void translate(TranslateMode mode, String output) throws Exception {
    String templateFilename = "";

    switch (mode) {
      case html:
        templateFilename = "resources/templates/html.vm";
        break;
      case xsd:
        templateFilename = "resources/templates/xsd.vm";
        break;
      case core_java:
        templateFilename = "resources/templates/core_model_serializer.vm";
        break;
      case editor_java:
        templateFilename = "resources/templates/editor_model_serializer.vm";
        break;
      case editor_semantic:
        templateFilename = "resources/templates/editor_semantic_rules.vm";
        break;
      case editor_sync_ddbb:
        templateFilename = "resources/templates/editor_syncro_ddbb.vm";
        break;
    }

    VelocityEngine engine = new VelocityEngine();
    engine.init();
    Template template = engine.getTemplate(templateFilename);
    VelocityContext context = new VelocityContext();

    if (mode == TranslateMode.core_java || 
        mode == TranslateMode.editor_java ||
        mode == TranslateMode.editor_semantic ||
        mode == TranslateMode.editor_sync_ddbb) {
      ArrayList<MetaClass> list = metamodel.getMetaClassList();
      Iterator<MetaClass> iterator = list.iterator();
      File outputFile = new File(output);
      if(outputFile.exists()) outputFile.delete();
      outputFile.mkdirs();
      while (iterator.hasNext()) {
        MetaClass metaclass = iterator.next();
        JavaSyntax syntax = new JavaSyntax();
        context.put("metaclass", metaclass);
        context.put("syntax", syntax);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        String filename = null;
        switch (mode) {
          case editor_semantic:
            filename = metaclass.getName() + "SemanticChecks";
            break;
          case editor_sync_ddbb:
            filename = metaclass.getName() + "SyncDDBB";
            break;
          default:
            filename = (metaclass.isExtensible()) ? metaclass.getName() + "Base" : metaclass.getName();
            break;
        }
        
        saveJavaFile(output + "/" + filename + ".java", writer.toString());
      }
    }
    else {
      context.put("metamodel", metamodel);
      StringWriter writer = new StringWriter();
      template.merge(context, writer);
      saveXMLFile(output, writer.toString(), mode);
    }

  }

  private void saveXMLFile(String filename, String content, TranslateMode mode) {
    ArrayList<String> xsdTokens = new ArrayList<String>();
    xsdTokens.add("targetNamespace");
    xsdTokens.add("elementFormDefault");
    xsdTokens.add("complexType");
    xsdTokens.add("simpleType");
    xsdTokens.add("complexContent");
    xsdTokens.add("simpleContent");
    xsdTokens.add("maxOccurs");
    xsdTokens.add("minOccurs");

    try {
      HtmlCleaner cleaner = new HtmlCleaner();
      CleanerProperties props = cleaner.getProperties();
      props.setOmitHtmlEnvelope(mode == TranslateMode.xsd);
      props.setUseEmptyElementTags(false);
      props.setOmitXmlDeclaration(mode == TranslateMode.html);
      
      String result = "";
      if (mode == TranslateMode.html)
        result = content;
      else {
        TagNode node = cleaner.clean(content);
        PrettyXmlSerializer serializer = new PrettyXmlSerializer(props);
        result = serializer.getXmlAsString(node, "UTF-8");
      }

      for (String token : xsdTokens) {
        result = result.replace(token.toLowerCase(), token);
      }

      FileUtils.writeToFileAsUTF8(filename, result);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void saveJavaFile(String filename, String content) {
    try {
      File file = new File(filename);
      if(!file.exists()) file.createNewFile();
      OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(filename), "UTF-8");
      output.write(content);
      output.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}