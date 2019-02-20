package org.monet.docservice.docprocessor.templates.common;

import java.util.HashMap;

import javax.xml.stream.XMLStreamReader;

public class Attributes {
    
    private XMLStreamReader reader;
	private HashMap<String, String> modifiedValues;
    
    public Attributes(XMLStreamReader reader, HashMap<String, String> modifiedValues) {
      this.reader = reader;
      this.modifiedValues = modifiedValues;
    }
    
    public String getName(int index) {
      return this.reader.getAttributeLocalName(index);
    }
    
    public String getValue(int index) {
      return this.reader.getAttributeValue(index);
    }
    
    public int getCount() {
      return this.reader.getAttributeCount();
    }
    
    public boolean isStartElement(){
      return this.reader.isStartElement();
    }
    
    public boolean isEndElement(){
      return this.reader.isEndElement();
    }
    
    public String getValue(String name) {
      int separatorIndex = name.indexOf(':');
      String localName = null;
      String namespaceURI = null;
      if(separatorIndex > -1) {
        localName = name.substring(separatorIndex+1);
        namespaceURI = reader.getNamespaceURI(name.substring(0,separatorIndex));
      } else {
        localName = name;
        namespaceURI = null;
      }
      
      return reader.getAttributeValue(namespaceURI, localName);
    }
    
    public void setValue(String name, String value) {
    	this.modifiedValues.put(name, value);
    }

  }