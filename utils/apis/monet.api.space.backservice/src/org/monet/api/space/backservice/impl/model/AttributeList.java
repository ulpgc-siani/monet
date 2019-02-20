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

package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.*;

public class AttributeList extends BaseModelList<Attribute> {
  protected LinkedHashMap<String, LinkedHashMap<String, Attribute>> itemsByCode;

  public AttributeList() {
    super();
    this.itemsByCode = new LinkedHashMap<String, LinkedHashMap<String, Attribute>>();
  }
  
  public AttributeList(AttributeList attributeList) {
    this();
    this.clone(attributeList);
  }

  public Attribute getFirstByCode(String code) {
    if(this.itemsByCode.containsKey(code)) return this.itemsByCode.get(code).values().iterator().next();
    LinkedHashMap<String, Attribute> attributes = this.searchByCode(code);
    if (attributes.size() == 0) return null;
    return attributes.values().iterator().next();
  }

  public LinkedHashMap<String, Attribute> getByCode(String code) {
    if (!this.itemsByCode.containsKey(code)) return new LinkedHashMap<String, Attribute>();
    return this.itemsByCode.get(code);
  }

  public LinkedHashMap<String, Attribute> searchByCode(String code) {
    LinkedHashMap<String, Attribute> result = new LinkedHashMap<String, Attribute>();
    Iterator<Attribute> iterator;
    
    if (this.itemsByCode.containsKey(code)) {
      LinkedHashMap<String, Attribute> elements = this.itemsByCode.get(code);
      result.putAll(elements);
    }
    
    iterator = this.items.values().iterator();
    while (iterator.hasNext()) {
      Attribute attribute = iterator.next();
      LinkedHashMap<String, Attribute> elements = attribute.getAttributeList().searchByCode(code);
      result.putAll(elements);
    }
    
    return result;
  }
  
  public List<Attribute> searchAllByCode(String code) {
    List<Attribute> result = new ArrayList<Attribute>();
    Iterator<Attribute> iterator;
    
    if (this.itemsByCode.containsKey(code)) {
      result.addAll(this.itemsByCode.get(code).values());
    }
    
    iterator = this.items.values().iterator();
    while (iterator.hasNext()) {
      Attribute attribute = iterator.next();
      result.addAll(attribute.getAttributeList().searchAllByCode(code));
    }
    
    return result;
  }

  public void add(Attribute attribute) {
    String code = attribute.getCode();
    
    super.add(attribute);

    if (! this.itemsByCode.containsKey(code)) this.itemsByCode.put(code, new LinkedHashMap<String, Attribute>());
    this.itemsByCode.get(code).put(attribute.getId(), attribute);

  }
  
  public void delete(String key) {
    String id = null;

    if (this.items.containsKey(key)) id = key;
    if ((id == null) && (this.codes.containsKey(key))) id = (String)this.codes.get(key);
    
    if (id == null) {
      Iterator<String> oIterator = this.items.keySet().iterator();
      while (oIterator.hasNext()) {
        Attribute oAttribute = (Attribute)this.items.get(oIterator.next());
        oAttribute.getAttributeList().delete(key);
        return;
      }
      return;
    }
    
    this.items.remove(id);
    this.codes.remove(id);
    this.itemsByCode.remove(id);
  }
  
  public void clear() {
    super.clear();
    this.itemsByCode.clear();
  }

  public void merge(AttributeList newAttributeList) {
    MonetHashMap<Attribute> newAttributes = newAttributeList.get();
    Iterator<Attribute> iter = newAttributes.values().iterator();

    HashSet<String> insertedCodes = new HashSet<String>();
    while (iter.hasNext()) {
      Attribute newAttribute = iter.next();
      LinkedHashMap<String, Attribute> newAttributesByCode = newAttributeList.itemsByCode.get(newAttribute.code);
      LinkedHashMap<String, Attribute> currentAttributesByCode = null;
      if (this.itemsByCode.containsKey(newAttribute.code)) currentAttributesByCode = this.itemsByCode.get(newAttribute.code);
      if(newAttributesByCode.size() > 1 || (currentAttributesByCode != null && currentAttributesByCode.size() > 1)) { //Multiple field
        if(!insertedCodes.contains(newAttribute.code)) {
          if(currentAttributesByCode != null) {
            for(Attribute attribute : currentAttributesByCode.values())
              this.items.remove(attribute.id);
          }
          insertedCodes.add(newAttribute.code);
        }
        this.add(newAttribute);
      } else {
        Attribute myAttribute = this.getFirstByCode(newAttribute.getCode());
        if (myAttribute != null) myAttribute.merge(newAttribute);
        else this.add(newAttribute);
      }
    }
  }

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    Iterator<String> iterator = this.items.keySet().iterator();

    serializer.startTag("", "attributelist");
    while(iterator.hasNext()) {
      String id = iterator.next();
      Attribute attribute = this.items.get(id);
      attribute.serializeToXML(serializer);
    }
    serializer.endTag("", "attributelist");
  }

  @SuppressWarnings("unchecked")
  public void deserializeFromXML(Element attributeList) throws ParseException {
    List<Element> attributes;
    Iterator<Element> iterator;
    
    if (attributeList == null) return;

    attributes = attributeList.getChildren("attribute");
    iterator = attributes.iterator();

    this.clear();

    while (iterator.hasNext()) {
      Attribute attribute = new Attribute();
      int attributeId = this.items.size()+1;
      attribute.deserializeFromXML(iterator.next());
      attribute.setId(String.valueOf(attributeId).toString());
      if (!attribute.getCode().isEmpty()) 
        this.add(attribute);
    }
   
  }

  public static HashMap<String, String> extractIndicators(String indicatorCode, String content) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
    HashMap<String, String> indicators = new HashMap<String, String>();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
   
    XPathFactory xPathFactory = XPathFactory.newInstance();
    XPath xpath = xPathFactory.newXPath();
    XPathExpression indicatorsXp = null;
    indicatorsXp = xpath.compile(String.format("//indicator[@code='%s']", indicatorCode));
    
    if (content.equals("")) return indicators;
    
    builder = factory.newDocumentBuilder();
    Document document = builder.parse(new InputSource(new StringReader(content)));
    NodeList indicatorNodes = (NodeList) indicatorsXp.evaluate(document, XPathConstants.NODESET);
    int length = indicatorNodes.getLength();
    for(int i=0;i<length;i++) {
      org.w3c.dom.Node node = indicatorNodes.item(i);
      String code = node.getParentNode().getParentNode().getAttributes().getNamedItem("code").getNodeValue();
      String value = node.getTextContent();
      if(value.trim().isEmpty())
        break;
      indicators.put(code, value);
    }
    return indicators;
  }
  
}
