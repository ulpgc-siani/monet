package org.monet.docservice.core.util;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AttachmentExtractor {

  public static NodeList extract(String xmlData) throws ParserConfigurationException, XPathExpressionException, IOException, SAXException {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;

    if(xmlData == null || xmlData.trim().isEmpty()) return null;

    docBuilder = docFactory.newDocumentBuilder();
    org.w3c.dom.Document doc = docBuilder.parse(new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8)));

    XPath xpath = XPathFactory.newInstance().newXPath();
    String expression = "//*[@is-attachment=\"true\"]/text()";

    return (NodeList) xpath.evaluate(expression,doc,XPathConstants.NODESET);
  }

}
