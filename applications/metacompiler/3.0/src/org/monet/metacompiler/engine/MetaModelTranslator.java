package org.monet.metacompiler.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.monet.core.metamodel.MetaClass;
import org.monet.core.metamodel.MetaModel;
import org.monet.core.metamodel.MetaProperty;
import org.monet.core.metamodel.MetaSyntagma;
import org.monet.metacompiler.engine.renders.EditorRender;
import org.monet.metacompiler.engine.renders.ManifestSerializerRender;
import org.monet.metacompiler.engine.renders.MetaModelRender;
import org.monet.metacompiler.engine.renders.ModelSerializerRender;
import org.monet.metacompiler.engine.renders.RendersFactory;
import org.monet.metacompiler.util.FileUtils;

public class MetaModelTranslator {

  private MetaModel metamodel;

  public enum TranslateMode {
    html, manifest_java, core_java, editor_java, editor_gramatic, editor_structure
  }

  public class JavaSyntax {
    
    private String toAttributeJavaIdentifier(String aString) {
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
    
    private String capitalize(String name) {
      return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getToken(String name, boolean capitalize, boolean isMember) {
      name = name.substring(0, 1).toLowerCase() + name.substring(1);
      String result = toAttributeJavaIdentifier(name);
      if(capitalize)
        result = capitalize(result);
      if(isMember)
        result = "_" + result;
      return result;
    }

    public String getTokenMethod(String name, String parameters) {
      return "get" + getToken(name, true, false) + "(" + parameters + ")";
    }
    
    public String getTokenAddMethod(String name, String parameters) {
      return "add" + getToken(name, true, false) + "(" + parameters + ")";
    }
    
    public String getTokenSetMethod(String name, String parameters) {
      return "set" + getToken(name, true, false) + "(" + parameters + ")";
    }

    public String getAttributeType(String type) {
      if (type.equals("language") || type.equals("value")) return "Object";
      else if (type.equals("key")) return "org.monet.metamodel.internal.Ref";
      else if (type.equals("time")) return "org.monet.metamodel.internal.Time";
      else if (type.equals("string") || type.equals("file") || type.equals("code") || type.equals("uuid") || type.equals("formula") || type.equals("classifier")) return "String";
      else if (type.equals("natural") || type.equals("integer")) return "Long";
      else if (type.equals("real")) return "Double";
      else if (type.equals("boolean")) return "Boolean";
      else if (type.equals("URI")) return "java.net.URI";
      else if (type.equals("variable")) return null;
      else if (type.startsWith("resource:")) return "String";
      else return "unknown(" + type + ")";
    }

    public String getAttributeGramaticType(MetaSyntagma syntagma, String attributename, String type, String link) {
      if(type.equals("language")) return "java.lang.String";
      if(type.equals("value")) return "java.lang.String|org.monet.metamodel." + this.getToken(link, true, false);
      if (type.equals("string")|| type.equals("file") || type.equals("code") || type.equals("uuid")|| type.equals("formula") || type.equals("classifier")) return "java.lang.String";
      else if (type.equals("key")) return "org.monet.metamodel." + this.getToken(link, true, false);
      else if (type.equals("time")) return "org.monet.metamodel.internal.Time";
      else if (type.equals("enumeration")) {
        StringBuilder builder = new StringBuilder();
        while(syntagma != null) {
          String name = null;
          MetaSyntagma current = syntagma;
          if(syntagma instanceof MetaProperty) {
            name = ((MetaProperty) syntagma).getTypeOrToken();
            syntagma = (MetaSyntagma)((MetaProperty) syntagma).getOwner();
          } else if(syntagma instanceof MetaClass){
            name = ((MetaClass) syntagma).getType();
            syntagma = null;
          }
          if(current.isExtensible())
            name += "Base";
          builder.insert(0, this.getToken(name, true, false));
          builder.insert(0, "$");
        }
        builder.append("$");
        builder.append(this.getToken(attributename, true, false));
        builder.append("Enumeration");
        
        return "org.monet.metamodel." + builder.substring(1);
      }
      else if (type.equals("URI")) return "java.net.URI";
      else if (type.equals("natural") || type.equals("integer")) return "java.lang.Integer";
      else if (type.equals("real")) return "java.lang.Double";
      else if (type.equals("boolean")) return "java.lang.Boolean";
      else if (type.startsWith("expression:")) return "Expression:" + this.getAttributeGramaticType(syntagma, attributename, type.substring(type.indexOf(":") + 1), link);
      else if (type.startsWith("resource:")) return type;
      else if (type.equals("variable")) return "variable";
      else return "unknown(" + type + ")";
    }

    public String getEnumeration(String values) {
      return values;
    }
    
    public String escapeStringToJava(String value) {
      return value != null ? value.replaceAll("\\n", "\\\\n").replaceAll("\\\"", "\\\\\"") : null;
    }
  }
  
  public MetaModelTranslator(MetaModel metamodel) {
    this.metamodel = metamodel;
  }

  public void translate(TranslateMode mode, String output) throws Exception {
    RendersFactory rendersFactory = RendersFactory.getInstance(); 
    
    if (mode == TranslateMode.core_java || 
        mode == TranslateMode.editor_java) {
      ArrayList<MetaSyntagma> list = metamodel.getMetaRootList();
      File outputFile = new File(output);
      if(outputFile.exists()) outputFile.delete();
      outputFile.mkdirs();

      ManifestSerializerRender manifestRender = (ManifestSerializerRender)rendersFactory.get(TranslateMode.manifest_java.toString());
      manifestRender.setMetaModel(metamodel);
      saveJavaFile(output + "/Manifest.java", manifestRender.getOutput());

      for(MetaSyntagma metasyntagma : list) {
        JavaSyntax syntax = new JavaSyntax();

        ModelSerializerRender render = (ModelSerializerRender)rendersFactory.get(mode.toString());
        render.setMetaModel(metamodel);
        render.setMetaSyntagma(metasyntagma);
        render.setSyntax(syntax);

        String filename = (metasyntagma.isExtensible()) ? metasyntagma.getType() + "Base" : metasyntagma.getType();
        saveJavaFile(output + "/" + filename + ".java", render.getOutput());
      }
    } else if(mode == TranslateMode.editor_gramatic ||
              mode == TranslateMode.editor_structure) {
      
      EditorRender render = (EditorRender)rendersFactory.get(mode.toString());
      render.setMetaModel(metamodel);
      render.setSyntax(new JavaSyntax());
      render.setRuleSet(new HashSet<String>());
      
      saveJavaFile(output, render.getOutput());
    } else {
      
      MetaModelRender render = (MetaModelRender)rendersFactory.get(mode.toString());
      render.setMetaModel(metamodel);
      
      saveXMLFile(output, render.getOutput(), mode);
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
      props.setOmitHtmlEnvelope(false);
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
      if(!file.getParentFile().exists())
        file.getParentFile().mkdirs();
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