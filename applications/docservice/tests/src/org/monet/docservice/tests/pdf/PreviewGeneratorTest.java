package org.monet.docservice.tests.pdf;

import org.junit.Test;

public class PreviewGeneratorTest {

  @Test
  public void testGeneratePreview() throws Exception {
//    String sDestination = "resources/document1/Document.pdf";
//    AgentFilesystemImpl oAgentFileSystem = new AgentFilesystemImpl();
//    oAgentFileSystem.injectLogger(mock(Logger.class));
//    oAgentFileSystem.copyFile("assets/pdf/37207.pdf", sDestination);
//    
//    Logger logger = mock(Logger.class);
//    ServerConfigurator serverConfigurator = mock(ServerConfigurator.class);
//    when(serverConfigurator.getUserPath()).thenReturn("assets/user_data");
//    
//    Configuration configuration = new ConfigurationImpl(logger, serverConfigurator);
//    QueryStore queryStore = new QueryStoreImpl(logger, serverConfigurator, configuration);
//    
//    ServletContext servletContext = mock(ServletContext.class);
//    when(servletContext.getServletContextName()).thenReturn("docserver");
//    
//    Context context = new InitialContext();
//    new DataContextConfiguratorImpl(logger, configuration, context, servletContext);
//    DataSource dataSource = (DataSource)context.lookup(servletContext.getServletContextName());
//    
//    final DatabaseRepository rep = new DatabaseRepository(queryStore, logger, dataSource);
//    rep.clearDocumentPreviewData("document1");
//    
//    JPedalPreviewGenerator oGenerator = new JPedalPreviewGenerator(configuration, logger, new Provider<Repository>() {
//
//      public Repository get() {
//        return rep;
//      } });
//    oGenerator.generatePreview(sDestination, "1");
  }
  
}
