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
import org.monet.docservice.docprocessor.templates.impl.FieldCollectionItemModelProducer;
import org.monet.docservice.docprocessor.templates.impl.ModelImpl;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.Provider;

@SuppressWarnings("deprecation")
public class FieldItemCollectionModelProducerTest {

  @SuppressWarnings({ "unchecked" })
  @Test
  public void testCreate() throws IOException {
    Logger logger = mock(Logger.class);
    String sModelFile = "bin/sampleModel.xml";
    AgentFilesystemImpl oAgentFileSystem = new AgentFilesystemImpl();
    oAgentFileSystem.injectLogger(logger);
    
    try
    {
      oAgentFileSystem.copyFile("assets/docModels/sampleModel.xml", sModelFile);
      
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
      
      FieldCollectionItemModelProducer producer = new FieldCollectionItemModelProducer(mockProvider);
      producer.injectLogger(logger);
      StringBufferInputStream inputStream = new StringBufferInputStream(sData.toString());
      Model model = producer.create(inputStream);
      
      assertTrue(model.isPropertyAString("Apellido1"));
      assertEquals(model.getPropertyAsString("Nombre"), "Rayco");
      assertEquals(model.getPropertyAsString("Apellido1"), "Araña");
      assertEquals(model.getPropertyAsString("Apellido2"), "Rodríguez");
      
      assertTrue(model.isPropertyACollection("MiLista"));
      
      Collection<Model> miLista = model.getPropertyAsCollection("MiLista");
      assertEquals(miLista.size(), 5);
      Model[] aModels = miLista.toArray(new Model[0]);
      assertEquals(aModels[0].getPropertyAsString("Nombre"), "Nombre 0");
      assertEquals(aModels[2].getPropertyAsString("Apellidos"), "Apellido 2");
      assertEquals(aModels[4].getPropertyAsString("FechaNac"), "4/4/4");
      
      assertTrue(model.isPropertyACollection("MiTabla"));
      
      Collection<Model> miTabla = model.getPropertyAsCollection("MiTabla");
      assertEquals(miTabla.size(), 5);
      aModels = miTabla.toArray(new Model[0]);
      assertEquals(aModels[0].getPropertyAsString("Col1"), "PaC001");
      assertEquals(aModels[2].getPropertyAsString("Col3"), "PaC203");
      assertEquals(aModels[4].getPropertyAsString("Col4"), "PaC404");
    }
    finally
    {
      oAgentFileSystem.removeFile(sModelFile);      
    }
  }

}
