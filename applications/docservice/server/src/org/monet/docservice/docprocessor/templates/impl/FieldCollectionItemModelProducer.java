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

public class FieldCollectionItemModelProducer implements ModelProducer {

  private final Provider<Model> modelProvider;
  private Logger logger;
  
  @Inject
  public void injectLogger(Logger logger) {
    this.logger = logger;
  }
  
  @Inject
  public FieldCollectionItemModelProducer(Provider<Model> modelProvider) {
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

  private void process(XMLStreamReader reader, Model modelDest) throws XMLStreamException {
    logger.debug("process(%s, %s)", reader, modelDest);
    
    String sLocalName = null, 
           sCode = null,
           sValue = null;
    Collection<Model> currentCollection = null;
    Boolean isField = false, 
            isCollection = false;
    int eventType = reader.getEventType();
    while(reader.hasNext()) {
      switch(eventType) {
        case XMLStreamConstants.START_ELEMENT:
          sLocalName = reader.getLocalName();
          if(sLocalName.equals("field")) {
            sCode = reader.getAttributeValue(null, "code");
            isField = true;
          } else if(sLocalName.equals("collection")) {
            sCode = reader.getAttributeValue(null, "code");
            modelDest.createCollection(sCode);
            currentCollection = modelDest.getPropertyAsCollection(sCode);
            isCollection = true;
          } else if(sLocalName.equals("item") && isCollection) {
            reader.next();
            Model nextItem = modelProvider.get();
            currentCollection.add(nextItem);
            process(reader, nextItem);
          } 
          break;
        case XMLStreamConstants.END_ELEMENT:
          sLocalName = reader.getLocalName();
          if(sLocalName.equals("field")) {
            if(sValue != null)
              modelDest.setProperty(sCode, sValue);
            else
              modelDest.setProperty(sCode, "");
            sValue = null;
            isField = false;
          } else if(sLocalName.equals("collection")) {
            currentCollection = null;
            isCollection = false;
          } else if(sLocalName.equals("item")) {
            return;
          }
          break;
        case XMLStreamConstants.CHARACTERS:
          if(isField)
            sValue = reader.getText();
          break;
       }
       eventType = reader.next();
     }
  }
}
