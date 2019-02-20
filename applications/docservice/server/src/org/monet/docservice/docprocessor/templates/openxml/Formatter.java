package org.monet.docservice.docprocessor.templates.openxml;

import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringEscapeUtils;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.util.Resources;
import org.monet.docservice.core.util.StreamHelper;

public class Formatter {
  
  public static final String FIELD_BEGIN = "FIELD_BEGIN";
  public static final String FIELD_SEPARATE = "FIELD_SEPARATE";
  public static final String FIELD_END = "FIELD_END";
  public static final String NEW_STYLE = "NEW_STYLE";
  public static final String NEW_TAB = "NEW_TAB";
  public static final String NEW_LINE = "NEW_LINE";
  public static final String NEW_WORD = "NEW_WORD";
  public static final String STYLE_SUPERSCRIPT_TEXT = "STYLE_SUPERSCRIPT_TEXT";
  public static final String STYLE_SUBSCRIPT_TEXT = "STYLE_SUBSCRIPT_TEXT";
  public static final String STYLE_BOLD_TEXT = "STYLE_BOLD_TEXT";
  public static final String STYLE_ITALIC_TEXT = "STYLE_ITALIC_TEXT";
  public static final String STYLE_STRIKE_TEXT = "STYLE_STRIKE_TEXT";
  public static final String STYLE_UNDERLINE_TEXT = "STYLE_UNDERLINE_TEXT";
  public static final String START_TEXT = "START_TEXT";
  public static final String END_FIELD = "END_FIELD";
  public static final String CLOSE_TEXT = "CLOSE_TEXT";
  public static final String SINGLE_TAB = "SINGLE_TAB";
  
  private static final String[] TAGS = new String[] { "<b>", "</b>", 
                                                      "<i>", "</i>",
                                                      "<u>", "</u>",
                                                      "<s>", "</s>",
                                                      "<sup>", "</sup>",
                                                      "<sub>", "</sub>",
                                                      "<br/>", "<tab/>"};
  private static final String[] CODED_TAGS = new String[] { "&lt;b&gt;", "&lt;/b&gt;",
                                                            "&lt;i&gt;", "&lt;/i&gt;",
                                                            "&lt;u&gt;", "&lt;/u&gt;",
                                                            "&lt;s&gt;", "&lt;/s&gt;",
                                                            "&lt;sup&gt;", "&lt;/sup&gt;",
                                                            "&lt;sub&gt;", "&lt;/sub&gt;",
                                                            "(\r\n|\n)", "\t"};
  private static final String XML_TEMPLATE = "<c>%s</c>";
  
  private static Formatter instance = null;
  
  private Properties properties;
  
  private Formatter() {
    InputStream templatesStream = null;
    
    this.properties = new Properties();
    
    try {
      templatesStream = Resources.getAsStream("/templates/openxml.templates");
      this.properties.load(templatesStream);
    } catch (Exception e) {
      throw new ApplicationException("Error loading openxml templates", e);
    } finally {
      StreamHelper.close(templatesStream);
    }
  }
  
  public String getFormatterString(String key){
    return properties.getProperty(key);
  }
  
  public static StringFormatter formatString(String content, List<String> styles) {
    if(instance == null) instance = new Formatter();
    return instance.new StringFormatter(content, styles);
  }
  
  public static Formatter getInstance(){
    if(instance == null) instance = new Formatter();
    return instance;
  }
  
  class StringFormatter {
    
    private String content;
    private List<String> styles;
    private int style=0,b=0, i=0, u=0, s=0, sup=0, sub=0, br=0, tab=0;
    private boolean fisrtWord = true;
    
    public StringFormatter(String content, List<String> styles) {
      content = StringEscapeUtils.escapeXml(content);
      for(int i=0;i<TAGS.length;i++)
        content = content.replaceAll(CODED_TAGS[i], TAGS[i]);

      this.content = content;
      this.styles = styles;
    }
        
    public String asString() throws XMLStreamException, FactoryConfigurationError {
      StringBuilder builder = new StringBuilder();
      StringReader stringReader = new StringReader(String.format(XML_TEMPLATE, content));
      XMLStreamReader reader = XMLInputFactory.newInstance()
                                              .createXMLStreamReader(stringReader);

      int eventType = reader.getEventType();
      while(reader.hasNext()) {
        switch(eventType) {
          case XMLStreamConstants.START_ELEMENT:
            if(reader.getLocalName().equals("b")) {
              this.b++;
              this.style++;
            } else if(reader.getLocalName().equals("i")) {
              this.i++;
              this.style++;
            } else if(reader.getLocalName().equals("u")) {
              this.u++;
              this.style++;
            } else if(reader.getLocalName().equals("s")) {
              this.s++;
              this.style++;
            } else if(reader.getLocalName().equals("sup")) {
              this.sup++;
              this.style++;
            } else if(reader.getLocalName().equals("sub")) {
              this.sub++;
              this.style++;
            } else if(reader.getLocalName().equals("br")) {
              this.br++;
            } else if(reader.getLocalName().equals("tab")) {
              this.tab++;
            }
            break;
          case XMLStreamConstants.END_ELEMENT:
            if(reader.getLocalName().equals("b")) {
              this.b--;
              this.style--;
            } else if(reader.getLocalName().equals("i")) {
              this.i--;
              this.style--;
            } else if(reader.getLocalName().equals("u")) {
              this.u--;
              this.style--;
            } else if(reader.getLocalName().equals("s")) {
              this.s--;
              this.style--;
            } else if(reader.getLocalName().equals("sup")) {
              this.sup--;
              this.style--;
            } else if(reader.getLocalName().equals("sub")) {
              this.sub--;
              this.style--;
            }
            break;
          case XMLStreamConstants.CHARACTERS:
            String value = reader.getText();
            if(value != null && value.length() != 0) {
              if(this.style > 0 && this.br == 0 && this.tab == 0){
                builder.append(Formatter.getInstance().getFormatterString(Formatter.NEW_STYLE));
                addStylesOfFather(builder);
              }else {
                if(this.br > 0){
                  for (int i = 0; i < this.br-1; i++) {
                    builder.append(Formatter.getInstance().getFormatterString(Formatter.NEW_LINE));
                  }
                  builder.append(Formatter.getInstance().getFormatterString(Formatter.NEW_LINE));
                }
                if(this.tab == 1){
                  builder.append(Formatter.getInstance().getFormatterString(Formatter.NEW_TAB));
                  addStylesOfFather(builder);
                }
                else if(this.tab > 1){
                  builder.append(Formatter.getInstance().getFormatterString(Formatter.CLOSE_TEXT));
                  for (int i = 0; i < this.tab; i++) {
                    builder.append(Formatter.getInstance().getFormatterString(Formatter.SINGLE_TAB));
                  }
                  builder.append(Formatter.getInstance().getFormatterString(Formatter.NEW_WORD));
                  addStylesOfFather(builder);
                }
              }
              
              if(this.sub > 0) {
                builder.append(Formatter.getInstance().getFormatterString(Formatter.STYLE_SUBSCRIPT_TEXT));
              }
              if(this.sup > 0) {
                builder.append(Formatter.getInstance().getFormatterString(Formatter.STYLE_SUPERSCRIPT_TEXT));
              }
              if(this.b > 0) {
                builder.append(Formatter.getInstance().getFormatterString(Formatter.STYLE_BOLD_TEXT));
              }
              if(this.i > 0) {
                builder.append(Formatter.getInstance().getFormatterString(Formatter.STYLE_ITALIC_TEXT));
              }
              if(this.u > 0) {
                builder.append(Formatter.getInstance().getFormatterString(Formatter.STYLE_UNDERLINE_TEXT));
              }
              if(this.s > 0) {
                builder.append(Formatter.getInstance().getFormatterString(Formatter.STYLE_STRIKE_TEXT));
              }
              if(this.style > 0 || this.tab > 0) {
                builder.append(Formatter.getInstance().getFormatterString(Formatter.START_TEXT));
              }
              else if(!fisrtWord){
                builder.append(Formatter.getInstance().getFormatterString(Formatter.NEW_STYLE));
                addStylesOfFather(builder);
                builder.append(Formatter.getInstance().getFormatterString(Formatter.START_TEXT));
              }
              //builder.append(StringEscapeUtils.escapeXml(value));
              builder.append(escape(value));
              this.fisrtWord = false;
              if(tab > 0) tab = 0;
              if(br > 0) br = 0;
            }
            break;
        }
        eventType = reader.next();
      }
      return builder.toString();
    }
    
    
    private void addStylesOfFather(StringBuilder builder){
      for (String styleRPR : this.styles) {
        if(style == 0){
          builder.append(styleRPR);
        }
        else if(styleRPR.startsWith("<w:b")){
          if(b == 0) builder.append(styleRPR);
        }
        else if(styleRPR.startsWith("<w:i")){
          if(i == 0) builder.append(styleRPR);
        }
        else if(styleRPR.startsWith("<w:u")){
          if(u == 0) builder.append(styleRPR);
        }
        else if(styleRPR.startsWith("<w:strike")){
          if(s == 0) builder.append(styleRPR);
        }
        else if(styleRPR.startsWith("<w:vertAlign w:val=\"superscript\"")){
          if(sup == 0) builder.append(styleRPR);
        }
        else if(styleRPR.startsWith("<w:vertAlign w:val=\"subscript\"")){
          if(sub == 0) builder.append(styleRPR);
        }
        else if(!styleRPR.startsWith("<w:noProof")){
          builder.append(styleRPR);
        }
      }
    }
    
  }

  public String escape(String value) {
    return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }

}
