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

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.monet.docservice.core.agent.AgentFilesystem;
import org.monet.docservice.core.agent.impl.AgentFilesystemImpl;
import org.monet.docservice.core.library.impl.LibraryFileImpl;
import org.monet.docservice.core.library.impl.LibraryZipImpl;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.templates.common.Model;
import org.monet.docservice.docprocessor.templates.opendocument.OpenDocument;
import org.junit.Test;

public class OpenDocumentTest {

  private void mockField(Model mockModel, String key, String value) {
    when(mockModel.isPropertyAString(key)).thenReturn(true);
    when(mockModel.getPropertyAsString(key)).thenReturn(value);
  }
  
  private void mockField(Model mockModel, String key, Collection<Model> value) {
    when(mockModel.isPropertyACollection(key)).thenReturn(true);
    when(mockModel.getPropertyAsCollection(key)).thenReturn(value);
  }
  
  @Test
  public void testProcess() {  
	  Logger logger = mock(Logger.class);
	    Collection<Model> aTableMock = new ArrayList<Model>();
    for(int i=0;i<5;i++) {
      Model rowMock = mock(Model.class);
      mockField(rowMock, "Col1", "PaC" + i + "01");
      mockField(rowMock, "Col2", "PaC" + i + "02");
      mockField(rowMock, "Col3", "PaC" + i + "03");
      mockField(rowMock, "Col4", "PaC" + i + "04");
      aTableMock.add(rowMock);
    }
    
    Collection<Model> aListMock = new ArrayList<Model>();
    for(int i=0;i<5;i++) {
      Model rowMock = mock(Model.class);
      mockField(rowMock, "Nombre", "Nombre " + i);
      mockField(rowMock, "Apellidos", "Apellido " + i);
      mockField(rowMock, "FechaNac", i + "/" + i + "/" + i);
      mockField(rowMock, "Observaciones", "Obser...." + i);
      aListMock.add(rowMock);
    }
    
    Model mockModel = mock(Model.class);
    mockField(mockModel, "Apellido1", "Ara�a");
    mockField(mockModel, "Apellido2", "Rodr�guez");
    mockField(mockModel, "Nombre", "Rayco");
    mockField(mockModel, "MiTabla", aTableMock);
    mockField(mockModel, "MiLista", aListMock);

    OpenDocument doc = new OpenDocument();
    doc.injectLogger(logger);
    doc.setModel(mockModel);
    AgentFilesystem oAgentFileSystem = new AgentFilesystemImpl(); 
    doc.injectAgentFilesystem(oAgentFileSystem);
   
    LibraryZipImpl oLibraryZip = new LibraryZipImpl();
    oLibraryZip.injectLibraryFile(new LibraryFileImpl());
    doc.injectLibraryZip(oLibraryZip);
    oAgentFileSystem.copyFile("assets/odf/Template.odt", "bin/Document.odt");
    doc.process("bin/Document.odt");
    
    verify(mockModel).getPropertyAsString("Apellido1");
    verify(mockModel, atLeast(1)).getPropertyAsCollection("MiTabla");
  }

}
