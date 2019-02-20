package org.monet.docservice.tests.document;

import org.junit.Test;

public class RepositoryTest {

  @Test
  public void testDocument() throws Exception {
//    Logger logger = mock(Logger.class);
//    ServerConfigurator serverConfigurator = mock(ServerConfigurator.class);
//    when(serverConfigurator.getUserPath()).thenReturn("assets/user_data");
//    
//    ServletContext servletContext = mock(ServletContext.class);
//    when(servletContext.getServletContextName()).thenReturn("docserver");
//    
//    Configuration configuration = new ConfigurationImpl(logger, serverConfigurator);
//    QueryStore queryStore = new QueryStoreImpl(logger, serverConfigurator, configuration);
//    
//    Context context = new InitialContext();
//    new DataContextConfiguratorImpl(logger, configuration, context, servletContext);
//    DataSource dataSource = (DataSource)context.lookup(servletContext.getServletContextName());
//    
//    DatabaseRepository rep = new DatabaseRepository(queryStore, logger, dataSource);
//    rep.createDocument("miDocumento", "plantilla_A", Document.STATE_EDITABLE);
//    
//    ByteArrayInputStream stream = new ByteArrayInputStream("Hola mundo NUM 2 AAAAAA".getBytes());
//    rep.saveDocumentData("miDocumento", stream, "text/xml");
//    
//    stream = new ByteArrayInputStream("IMAGEN".getBytes());
//    rep.saveDocumentPreviewData("miDocumento", 1, stream, "image/png", PreviewType.PAGE, 100, 100, 1.0F);
//    stream.reset();
//    rep.saveDocumentPreviewData("miDocumento", 2, stream, "image/png", PreviewType.PAGE, 100, 100, 1.0F);
//    stream.reset();
//    rep.saveDocumentPreviewData("miDocumento", 3, stream, "image/png", PreviewType.PAGE, 100, 100, 1.0F);
//    
//    Document myDocument = rep.getDocument("miDocumento");
//    DocumentMetadata metadata = rep.getDocumentMetadata(myDocument);
//    assertTrue(metadata.getPages().size() == 3);
//    
//    rep.clearDocumentPreviewData("miDocumento");
//    rep.removeDocument("miDocumento");
  }
  
  @Test
  public void testTemplate() throws Exception {
//    Logger logger = mock(Logger.class);
//    ServerConfigurator serverConfigurator = mock(ServerConfigurator.class);
//    when(serverConfigurator.getUserPath()).thenReturn("assets/user_data");
//    
//    ServletContext servletContext = mock(ServletContext.class);
//    when(servletContext.getServletContextName()).thenReturn("docserver");
//    
//    Configuration configuration = new ConfigurationImpl(logger, serverConfigurator);
//    QueryStore queryStore = new QueryStoreImpl(logger, serverConfigurator, configuration);
//    
//    Context context = new InitialContext();
//    new DataContextConfiguratorImpl(logger, configuration, context, servletContext);
//    DataSource dataSource = (DataSource)context.lookup(servletContext.getServletContextName());
//    
//    DatabaseRepository rep = new DatabaseRepository(queryStore, logger, dataSource);
//    String instanceId = String.valueOf(rep.createTemplate("miTemplate", DocumentType.OPEN_DOCUMENT));
//    ByteArrayInputStream stream = new ByteArrayInputStream("TEMPLATE".getBytes());
//    rep.saveTemplateData(instanceId, stream, "binary/openDocument");
//    stream = new ByteArrayInputStream("PARTE".getBytes());
//    rep.addTemplatePart(instanceId, "parte1", stream);
//    stream.reset();
//    rep.addTemplatePart(instanceId, "parte2", stream);
    
    //ByteArrayOutputStream stream = new ByteArrayOutputStream();
    //rep.readDocumentData("documento1", stream);
    //Assert.assertEquals("Hola mundo NUM 2 AAAAAA", new String(stream.toByteArray()));
  }

}
