package org.monet.docservice.docprocessor.templates.portabledocument;

import java.io.StringReader;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringEscapeUtils;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;

public class FormatterImpl implements Formatter {
  
  private static final String[] TAGS = new String[] { "<b>", "</b>", 
                                                      "<i>", "</i>",
                                                      "<sup>", "</sup>",
                                                      "<sub>", "</sub>",};
  private static final String[] CODED_TAGS = new String[] { "&lt;b&gt;", "&lt;/b&gt;",
                                                            "&lt;i&gt;", "&lt;/i&gt;",
                                                            "&lt;sup&gt;", "&lt;/sup&gt;",
                                                            "&lt;sub&gt;", "&lt;/sub&gt;",};
  private static final String XML_TEMPLATE = "<c>%s</c>";
  private static final float TEXT_RISE_FACTOR = 3.0f;
  private static final float TEXT_RISE_FONT_FACTOR = 0.66f;
  
  public StringFormatter formatString(String content) {
    return new StringFormatterImpl(content);
  }
  
  class StringFormatterImpl implements Formatter.StringFormatter, Formatter.StringFormatterWithFont {
    
    private Font font;
    private float size = -1;
    private String content;
    private int b=0, i=0, sup=0, sub=0;
    
    public StringFormatterImpl(String content) {
      content = StringEscapeUtils.escapeXml(content);
      for(int i=0;i<TAGS.length;i++)
        content = content.replaceAll(CODED_TAGS[i], TAGS[i]);

      this.content = content;
    }
    
    public StringFormatterWithFont withFont(Font font) {
      this.font = font;
      return this;
    }
  
    public StringFormatter ofSize(float size) {
      this.size = size;
      return this;
    }
    
    public Phrase asPhrase() throws XMLStreamException, FactoryConfigurationError {
      Phrase phrase = new Phrase();
      StringReader stringReader = new StringReader(String.format(XML_TEMPLATE, content));
      XMLStreamReader reader = XMLInputFactory.newInstance()
                                              .createXMLStreamReader(stringReader);

      if(this.size > 0) font.setSize(this.size);
      else this.size = font.getSize();
      phrase.setFont(font);
      
      int eventType = reader.getEventType();
      while(reader.hasNext()) {
        switch(eventType) {
          case XMLStreamConstants.START_ELEMENT:
            if(reader.getLocalName().equals("b"))
              this.b++;
            else if(reader.getLocalName().equals("i"))
              this.i++;
            else if(reader.getLocalName().equals("sup"))
              this.sup++;
            else if(reader.getLocalName().equals("sub"))
              this.sub++;
            break;
          case XMLStreamConstants.END_ELEMENT:
            if(reader.getLocalName().equals("b"))
              this.b--;
            else if(reader.getLocalName().equals("i"))
              this.i--;
            else if(reader.getLocalName().equals("sup"))
              this.sup--;
            else if(reader.getLocalName().equals("sub"))
              this.sub--;
            break;
          case XMLStreamConstants.CHARACTERS:
            String value = reader.getText();
            if(value != null && value.length() != 0) {
              Font font = new Font(this.font);
              Chunk chunk = new Chunk(value);
              if(this.sub > 0 || this.sup > 0) {
                chunk.setTextRise((this.sub * TEXT_RISE_FACTOR * -1) + (this.sup * TEXT_RISE_FACTOR));
                font.setSize(this.size * TEXT_RISE_FONT_FACTOR);
              } else {
                chunk.setTextRise(0.0f);
                font.setSize(this.size);
              }
              if(this.b > 0 && this.i > 0)
                font.setStyle(Font.BOLDITALIC);
              else if(this.b > 0)
                font.setStyle(Font.BOLD);
              else if(this.i > 0)
                font.setStyle(Font.ITALIC);
              chunk.setFont(font);
              phrase.add(chunk);
            }
            break;
        }
        eventType = reader.next();
      }
      return phrase;
    }
    
  }

}
