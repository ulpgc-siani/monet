package org.monet.docservice.tests.pdf;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.monet.docservice.core.agent.impl.AgentFilesystemImpl;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.pdf.impl.AsposePdfConverter;

public class AsposePdfConverterTest {

  @Test
  public void testGeneratePdf() {
    String sDestination = "bin/";
    Logger logger = mock(Logger.class);
    Configuration configuration = mock(Configuration.class);
    when(configuration.getPath(Configuration.PATH_TRUETYPE_FONTS)).thenReturn("C:\\WINDOWS\\FONTS");    
    
    AgentFilesystemImpl oAgentFileSystem = new AgentFilesystemImpl();
    oAgentFileSystem.injectLogger(logger);
//    oAgentFileSystem.copyFile("assets/oxf/Document.docx", "bin/oxf/Document.docx");
//    oAgentFileSystem.copyFile("assets/odf/Document.odt", "bin/odf/Document.odt");
    
    AsposePdfConverter converter = new AsposePdfConverter(logger, configuration);
    converter.generatePdf("C:/Users/fsantana/Desktop/docx_test/ptv10.1.docx", "C:/Users/fsantana/Desktop/docx_test/ptv10.111.pdf");
    //converter.generatePdf("bin/odf/Document.odt", sDestination + "odf/" + "Document.pdf");
  }

}
