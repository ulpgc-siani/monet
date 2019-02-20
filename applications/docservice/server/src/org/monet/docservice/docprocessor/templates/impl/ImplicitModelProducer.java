package org.monet.docservice.docprocessor.templates.impl;

import java.io.InputStream;
import java.util.Collection;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.templates.ModelProducer;
import org.monet.docservice.docprocessor.templates.common.Model;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ImplicitModelProducer implements ModelProducer {

  private final Provider<Model> modelProvider;
  private Logger logger;
  
  @Inject
  public void injectLogger(Logger logger) {
    this.logger = logger;
  }
  
  @Inject
  public ImplicitModelProducer(Provider<Model> modelProvider) {
    this.modelProvider = modelProvider;
  }
  
  public Model create(InputStream dataStream) {
    logger.debug("create(%s)", dataStream);
    
    Model mDest = modelProvider.get();
    XMLStreamReader reader = null;
    try {
      if(dataStream != null) {
        reader = XMLInputFactory.newInstance()
                                .createXMLStreamReader(dataStream);
        
        process(reader, mDest);
      }
    } catch (XMLStreamException e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException(e.getMessage(), e);
    } catch (FactoryConfigurationError e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException(e.getMessage(), e.getException());
    }
    finally {
      if(reader != null) {
        try {
          reader.close();
        } catch (XMLStreamException e) {
          logger.error(e.getMessage(), e);
        }
      }
    }
    return mDest;
  }

  private void goUntilNext(XMLStreamReader reader, int xmlEventType) throws XMLStreamException {
    int eventType = reader.getEventType();
    while(eventType != xmlEventType) {
      eventType = reader.next();
    }
  }
  
  private void process(XMLStreamReader reader, Model modelDest) throws XMLStreamException {
    logger.debug("process(%s, %s)", reader, modelDest);
    
    StringBuilder valueBuilder = new StringBuilder();
    String sCode = null;
    Collection<Model> currentCollection = null;
    boolean isCollection = false,
            itemStarted = false;
    goUntilNext(reader, XMLStreamConstants.START_ELEMENT);
    String starterNode = reader.getLocalName();
    int eventType = reader.next(); //Ignore root element

    while(reader.hasNext()) {
      switch(eventType) {
        case XMLStreamConstants.START_ELEMENT:
          if(itemStarted) { //We are in a collection
            isCollection = true;
            itemStarted = false;
            modelDest.createCollection(sCode);
            currentCollection = modelDest.getPropertyAsCollection(sCode);
          } 
          if(isCollection) {
            Model nextItem = modelProvider.get();
            currentCollection.add(nextItem);
            process(reader, nextItem);
          } else {
            sCode = reader.getLocalName();
            itemStarted = true;
          }
          break;
        case XMLStreamConstants.END_ELEMENT:
          if(starterNode.equals(reader.getLocalName())) return;
          
          if(isCollection) {
            isCollection = false;
          } else if(itemStarted && reader.getLocalName().equals(sCode)) {
            modelDest.setProperty(sCode, valueBuilder.toString());
            itemStarted = false;
            valueBuilder.setLength(0);
            sCode = null;
          }
          break;
        case XMLStreamConstants.CHARACTERS:
            if(itemStarted) valueBuilder.append(reader.getText());
          break;
       }
       eventType = reader.next();
     }
  }
}
