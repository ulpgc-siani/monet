package org.monet.docservice.docprocessor.templates.portabledocument;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;

public interface Formatter {

  public StringFormatter formatString(String content);
  
  public static interface StringFormatter {
    public StringFormatterWithFont withFont(Font font);
    public Phrase asPhrase() throws XMLStreamException, FactoryConfigurationError;
  }
  
  public static interface StringFormatterWithFont extends StringFormatter {
    public StringFormatter ofSize(float size);
  }
  
}
