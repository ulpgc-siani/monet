/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.docservice.tests.templates;

import org.junit.Test;

public class OpenXmlTest {

//  private void mockField(Model mockModel, String key, String value) {
//    when(mockModel.isPropertyAString(key)).thenReturn(true);
//    when(mockModel.getPropertyAsString(key)).thenReturn(value);
//  }
//  
//  private void mockField(Model mockModel, String key, Collection<Model> value) {
//    when(mockModel.isPropertyACollection(key)).thenReturn(true);
//    when(mockModel.getPropertyAsCollection(key)).thenReturn(value);
//  }
  
  @Test
  public void testProcess() throws Exception {
//    Collection<Model> aTableMock = new ArrayList<Model>();
//    for(int i=0;i<5;i++) {
//      Model rowMock = mock(Model.class);
//      mockField(rowMock, "Col1", "PaC" + i + "01");
//      mockField(rowMock, "Col2", "PaC" + i + "02");
//      mockField(rowMock, "Col3", "PaC" + i + "03");
//      mockField(rowMock, "Col4", "PaC" + i + "04");
//      aTableMock.add(rowMock);
//    }
//    
//    Collection<Model> aSubTableMock = new ArrayList<Model>();
//    for(int i=0;i<5;i++) {
//      Model rowMock = mock(Model.class);
//      mockField(rowMock, "SubCol1", "Nombre " + i);
//      mockField(rowMock, "SubCol2", "Apellido " + i);
//      mockField(rowMock, "SubCol3", i + "/" + i + "/" + i);
//      mockField(rowMock, "SubCol4", "Obser...." + i);
//      aSubTableMock.add(rowMock);
//    }
//    
//    Collection<Model> aListMock = new ArrayList<Model>();
//    for(int i=0;i<5;i++) {
//      Model rowMock = mock(Model.class);
//      mockField(rowMock, "Nombre", "Nombre " + i);
//      mockField(rowMock, "Apellidos", "Apellido " + i);
//      mockField(rowMock, "FechaNac", i + "/" + i + "/" + i);
//      mockField(rowMock, "Observaciones", "Obser...." + i);
//      mockField(rowMock, "MiSubTabla", aSubTableMock);
//      aListMock.add(rowMock);
//    }
//    
//    Model mockModel = mock(Model.class);
//    mockField(mockModel, "Apellido1", "Araña");
//    mockField(mockModel, "Apellido2", "Rodríguez");
//    mockField(mockModel, "Nombre", "Rayco");
//    mockField(mockModel, "MiTabla", aTableMock);
//    mockField(mockModel, "MiLista", aListMock);
//    mockField(mockModel, "FirmaInformatica", "true");
//    mockField(mockModel, "FirmaIngenieria", "false");
//    
//    Logger logger = mock(Logger.class);
//
//    ServerConfigurator serverConfigurator = mock(ServerConfigurator.class);
//    when(serverConfigurator.getUserPath()).thenReturn("assets/user_data");
//    
//    ServletContext servletContext = mock(ServletContext.class);
//    when(servletContext.getServletContextName()).thenReturn("docserver");
//    
//    Configuration configuration = new ConfigurationImpl(logger, serverConfigurator);
//    
//    QueryStore queryStore = new QueryStoreImpl(logger, serverConfigurator, configuration);
//    Context context = new InitialContext();
//    new DataContextConfiguratorImpl(logger, configuration, context, servletContext);
//    DataSource dataSource = (DataSource)context.lookup(servletContext.getServletContextName());
//    
//    DatabaseRepository repository = new DatabaseRepository(queryStore, logger, dataSource);
//    
//    AgentFilesystemImpl oAgentFileSystem = new AgentFilesystemImpl();
//    oAgentFileSystem.injectLogger(logger);
//
//    LibraryFileImpl oLibraryFile = new LibraryFileImpl();
//    oLibraryFile.injectLogger(logger);
//    
//    LibraryZipImpl oLibraryZip = new LibraryZipImpl();
//    oLibraryZip.injectLogger(logger);
//    oLibraryZip.injectLibraryFile(oLibraryFile);
//    
//    OpenXmlDocument doc = new OpenXmlDocument();
//    doc.injectLogger(logger);
//    doc.injectConfiguration(configuration);
//    doc.setModel(mockModel);
//    doc.injectRepository(repository);
//    doc.injectAgentFilesystem(oAgentFileSystem);
//    doc.setDocumentId("45");
//    doc.injectLibraryZip(oLibraryZip);
//    
//    //oAgentFileSystem.copyFile("assets/oxf/Template.docx", "bin/Document.docx");
//    doc.process("bin/Document.docx");
//    
//    verify(mockModel).getPropertyAsString("Apellido1");
//    verify(mockModel).getPropertyAsCollection("MiLista");
  }

  @Test
  public void testTemplatePartsExtractor() throws Exception {
//
//    Logger logger = mock(Logger.class);
//    ServerConfigurator serverConfigurator = mock(ServerConfigurator.class);
//    when(serverConfigurator.getUserPath()).thenReturn("assets/user_data");
//    
//    ServletContext servletContext = mock(ServletContext.class);
//    when(servletContext.getServletContextName()).thenReturn("docserver");
//    
//    Configuration configuration = new ConfigurationImpl(logger, serverConfigurator);
//    
//    QueryStore queryStore = new QueryStoreImpl(logger, serverConfigurator, configuration);
//    Context context = new InitialContext();
//    new DataContextConfiguratorImpl(logger, configuration, context, servletContext);
//    DataSource dataSource = (DataSource)context.lookup(servletContext.getServletContextName());
//    
//    DatabaseRepository repository = new DatabaseRepository(queryStore, logger, dataSource);
//    
//    AgentFilesystemImpl oAgentFileSystem = new AgentFilesystemImpl();
//    oAgentFileSystem.injectLogger(logger);
//
//    LibraryFileImpl oLibraryFile = new LibraryFileImpl();
//    oLibraryFile.injectLogger(logger);
//    
//    LibraryZipImpl oLibraryZip = new LibraryZipImpl();
//    oLibraryZip.injectLogger(logger);
//    oLibraryZip.injectLibraryFile(oLibraryFile);
//    
//    OpenXmlDocumentExtractor doc = new OpenXmlDocumentExtractor();
//    doc.setLogger(logger);
//    doc.setConfiguration(configuration);
//    doc.setAgentFilesystem(oAgentFileSystem);
//    doc.setLibraryZip(oLibraryZip);
//    doc.setRepository(repository);
//    
//    oAgentFileSystem.copyFile("assets/oxf/Template.docx", "bin/Document.docx");
//    doc.process("bin/Document.docx", "4");
    
  }
  
}
