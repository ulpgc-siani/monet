package org.monet.docservice.tests.templates;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.monet.docservice.core.agent.impl.AgentFilesystemImpl;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.templates.common.Model;
import org.monet.docservice.docprocessor.templates.impl.ModelImpl;
import org.monet.docservice.docprocessor.templates.impl.FieldCollectionItemModelProducer;
import org.monet.docservice.docprocessor.templates.portabledocument.Formatter;
import org.monet.docservice.docprocessor.templates.portabledocument.FormatterImpl;
import org.monet.docservice.docprocessor.templates.portabledocument.PdfProcessor;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.Provider;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;


public class PdfTest {
  
  @Test
  public void pdfTemplate1Test() throws IOException, DocumentException, XMLStreamException, FactoryConfigurationError {
    String sTemplatePath = "bin/pdf_template/Template_1.pdf";
    AgentFilesystemImpl oAgentFileSystem = new AgentFilesystemImpl();
    oAgentFileSystem.injectLogger(mock(Logger.class));
    oAgentFileSystem.copyFile("assets/pdf/Template_1.pdf", sTemplatePath);

    String content_1 = "Una prueba &lt;b&gt;de&lt;/b&gt; texto &lt;sub&gt;donde &lt;sup&gt;&lt;b&gt;ca&lt;/b&gt;si&lt;/sup&gt;&lt;/sub&gt; todo tiene &lt;i&gt;formato&lt;/i&gt;";
    //String content = "<content>Una prueba <b>de</b> texto donde <sub>casi <sup>todo</sup></sub> <b>tiene <i>formato</i></b></content>";
    
    //Phrase phrase = processContent(content);
    Font helvetica = new Font(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, true));
    Formatter formatter = new FormatterImpl();
    Phrase fieldContent = formatter.formatString(content_1)
                                   .withFont(helvetica)
                                   .ofSize(12)
                                   .asPhrase();
        
    Document document = new Document();
    PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(sTemplatePath));
    document.open();      
      Chunk c1,c2,c3;
      Font font = new Font(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, true));
      font.setSize(12);
      
      c1 = new Chunk("X");
      c1.setTextRise(0.0f);
      c1.setFont(font);
      document.add(c1);
      
      font.setSize(8);
      
      c3 = new Chunk("0");
      c3.setTextRise(-4.0f);
      c3.setFont(font);
      document.add(c3);
      
      c2 = new Chunk("2");
      c2.setTextRise(4.0f);
      
      c2.setFont(font);
      document.add(c2);
      
      document.newPage();
      
      Paragraph p = new Paragraph();
      p.add(new Chunk("Hola don pepito,"));
      Chunk sup = new Chunk(" hola don josé.");
      sup.setTextRise(4);
      p.add(sup);
      p.add(new Chunk("Pasó usted por mi casa, por su casa yo pasé. Vio usted a mi abuela, a su abuela yo la vi. Adios don pepito, adios don josé. Pasó usted por mi casa, por su casa yo pasé. Vio usted a mi abuela, a su abuela yo la vi. Adios don pepito, adios don josé"));
      p.setAlignment(Element.ALIGN_JUSTIFIED);

      ColumnText ct = new ColumnText(writer.getDirectContent());
      ct.setSimpleColumn(100, 100, 300, 300);
      ct.addText(fieldContent);
      ct.go();
      //document.add(p);
      document.add(c3);
      
    document.close();
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void pdfTemplate2Test() throws IOException, DocumentException {    
    String sModelFile = "bin/sample.xml";
    Logger mockLogger = mock(Logger.class);
    
    Configuration mockConfig = mock(Configuration.class);
    when(mockConfig.getPath(Configuration.PATH_TEMP)).thenReturn("bin/pdf_template");
    
    AgentFilesystemImpl oAgentFileSystem = new AgentFilesystemImpl();
    oAgentFileSystem.injectLogger(mockLogger);
    oAgentFileSystem.copyFile("assets/docModels/sample.xml", sModelFile);
        
    Provider<Model> mockProvider = (Provider<Model>)mock(Provider.class);
    when(mockProvider.get()).thenAnswer(new Answer<Model>() {
      public Model answer(InvocationOnMock invocation) throws Throwable {
        return new ModelImpl();
      }
    });
    
    FieldCollectionItemModelProducer producer = new FieldCollectionItemModelProducer(mockProvider);
    producer.injectLogger(mockLogger);
    Model model = producer.create(new FileInputStream(sModelFile));
    
      
    String sTemplatePath = "bin/pdf_template/MiCV_Template.pdf";
    
    oAgentFileSystem.copyFile("assets/pdf/MiCV_Template.pdf", sTemplatePath);
    
    PdfProcessor processor = new PdfProcessor();
    processor.injectLogger(mockLogger);
    processor.injectConfiguration(mockConfig);
    processor.setModel(model);
    processor.process(sTemplatePath);
  }
  
}
