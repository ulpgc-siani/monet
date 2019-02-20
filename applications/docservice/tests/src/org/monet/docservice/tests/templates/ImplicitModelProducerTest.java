package org.monet.docservice.tests.templates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.Collection;

import org.monet.docservice.core.agent.impl.AgentFilesystemImpl;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.templates.common.Model;
import org.monet.docservice.docprocessor.templates.impl.ImplicitModelProducer;
import org.monet.docservice.docprocessor.templates.impl.ModelImpl;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.Provider;

@SuppressWarnings("deprecation")
public class ImplicitModelProducerTest {

  @SuppressWarnings({ "unchecked" })
  @Test
  public void testCreate() throws IOException {
    Logger logger = mock(Logger.class);
    
    String sModelFile = "bin/implicitModelSample.xml";
    AgentFilesystemImpl oAgentFileSystem = new AgentFilesystemImpl();
    oAgentFileSystem.injectLogger(logger);
    
    try
    {
      oAgentFileSystem.copyFile("assets/docModels/implicitModelSample.xml", sModelFile);
      
      BufferedReader modelReader = new BufferedReader(new FileReader(sModelFile));
      StringBuilder sData = new StringBuilder();
      String buffer;
      while((buffer = modelReader.readLine()) != null) {
        sData.append(buffer);
      }
      
      Provider<Model> mockProvider = (Provider<Model>)mock(Provider.class);
      when(mockProvider.get()).thenAnswer(new Answer<Model>() {
        public Model answer(InvocationOnMock invocation) throws Throwable {
          return new ModelImpl();
        }
      });
      
      ImplicitModelProducer producer = new ImplicitModelProducer(mockProvider);
      producer.injectLogger(logger);
      StringBufferInputStream inputStream = new StringBufferInputStream(sData.toString());
      Model model = producer.create(inputStream);
      
      assertTrue(model.isPropertyAString("id"));
      assertEquals(model.getPropertyAsString("id"), "234234");
      assertEquals(model.getPropertyAsString("activo"), "Plaza Sancho Panza");
      assertEquals(model.getPropertyAsString("fechaInicio"), "13.05.08 (22:12)");
      
      assertTrue(model.isPropertyACollection("listaIncidencias"));
      
      Collection<Model> miLista = model.getPropertyAsCollection("listaIncidencias");
      assertEquals(miLista.size(), 2);
      Model[] aModels = miLista.toArray(new Model[0]);
      assertEquals(aModels[0].getPropertyAsString("id"), "123123");
      assertEquals(aModels[1].getPropertyAsString("id"), "55544");
      assertEquals(aModels[1].getPropertyAsString("activo"), "Banco rojo numero 10");
      
    }
    finally
    {
      oAgentFileSystem.removeFile(sModelFile);      
    }
  }
  
  @SuppressWarnings({ "unchecked" })
  @Test
  public void testEmptyCollection() throws IOException {
    Logger logger = mock(Logger.class);
    
    String sModelFile = "bin/implicitEmptyModelSample.xml";
    AgentFilesystemImpl oAgentFileSystem = new AgentFilesystemImpl();
    oAgentFileSystem.injectLogger(logger);
    
    try
    {
      oAgentFileSystem.copyFile("assets/docModels/implicitEmptyModelSample.xml", sModelFile);
      
      BufferedReader modelReader = new BufferedReader(new FileReader(sModelFile));
      StringBuilder sData = new StringBuilder();
      String buffer;
      while((buffer = modelReader.readLine()) != null) {
        sData.append(buffer);
      }
      
      Provider<Model> mockProvider = (Provider<Model>)mock(Provider.class);
      when(mockProvider.get()).thenAnswer(new Answer<Model>() {
        public Model answer(InvocationOnMock invocation) throws Throwable {
          return new ModelImpl();
        }
      });
      
      ImplicitModelProducer producer = new ImplicitModelProducer(mockProvider);
      producer.injectLogger(logger);
      StringBufferInputStream inputStream = new StringBufferInputStream(sData.toString());
      Model model = producer.create(inputStream);
      
      assertTrue(model.isPropertyAString("id"));
      assertEquals(model.getPropertyAsString("id"), "234234");
      assertEquals(model.getPropertyAsString("activo"), "Plaza Sancho Panza");
      assertEquals(model.getPropertyAsString("fechaInicio"), "13.05.08 (22:12)");
      
      assertTrue(model.isPropertyACollection("listaIncidencias"));
      
      Collection<Model> miLista = model.getPropertyAsCollection("listaIncidencias");
      assertEquals(miLista.size(), 0);      
    }
    finally
    {
      oAgentFileSystem.removeFile(sModelFile);      
    }
  }

}
