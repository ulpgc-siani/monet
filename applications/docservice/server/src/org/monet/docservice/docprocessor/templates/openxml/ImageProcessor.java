package org.monet.docservice.docprocessor.templates.openxml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.monet.docservice.core.Key;
import org.monet.docservice.core.library.LibraryUtils;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.guice.InjectorFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ImageProcessor {
  private final Key documentKey;
  private String dir;
  private String relsFile;
  private String pathSourcedoc;

  public ImageProcessor(Key documentKey, String dir, String sourceDocument) {
    this.documentKey = documentKey;
    this.dir = dir;
    this.pathSourcedoc = sourceDocument.substring(0, sourceDocument.lastIndexOf("/"));
    this.relsFile = sourceDocument.substring(sourceDocument.lastIndexOf("/") + 1) + ".rels";
  }

  public void replaceImages(HashSet<String> oldIds, HashSet<String> newIds) throws Exception {
    Repository repo = InjectorFactory.get().getInstance(Repository.class);
    LibraryUtils lUtils = InjectorFactory.get().getInstance(LibraryUtils.class);

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    docFactory.setNamespaceAware(true);
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    String uri = this.dir + this.pathSourcedoc + File.separator + "_rels" + File.separator + relsFile;
    if (!(new File(uri)).exists())
      return;
    Document doc = docBuilder.parse(uri);

    NamespaceContext ctx = new NamespaceContext() {
      public String getNamespaceURI(String prefix) {
        return "http://schemas.openxmlformats.org/package/2006/relationships";
      }

      @SuppressWarnings("rawtypes")
      public Iterator getPrefixes(String val) {
        return null;
      }

      public String getPrefix(String uri) {
        return null;
      }
    };

    XPath xpath = XPathFactory.newInstance().newXPath();
    xpath.setNamespaceContext(ctx);

    String expression = "/:Relationships";
    Node parent = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);

    if (parent == null)
      return;

    HashMap<String, File> imagesToRemove = new HashMap<String, File>();
    for (String idToKeep : oldIds.toArray(new String[] {})) {
      if (newIds.contains(idToKeep)) {
        oldIds.remove(idToKeep);
        newIds.remove(idToKeep);
      }
    }

    /* Dont remove unused images files from docx
    for (String currentId : oldIds) {
      expression = "/:Relationships/:Relationship[@Id=\"" + currentId + "\"]";
      Node target = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);

      if (target != null) {
        String targetValue = ((Element) target).getAttribute("Target");
        String pathImage = this.dir + this.pathSourcedoc + File.separator + targetValue;

        File image = (new File(pathImage));
        imagesToRemove.put(targetValue, image);
        parent.removeChild(target);
      }
    }*/

    expression = "/:Relationships/:Relationship";
    NodeList relationships = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
    for (int i = 0; i < relationships.getLength(); i++) {
      Node target = relationships.item(i);
      if (target != null) {
        String targetValue = ((Element) target).getAttribute("Target");
        imagesToRemove.remove(targetValue);
      }
    }

    for (File image : imagesToRemove.values())
      if (image.exists())
        image.delete();

    for (String idImagen : newIds) {
      Element element = doc.createElement("Relationship");
      String fileName = idImagen.replaceAll("[^\\w\\d\\.]", "_");

      element.setAttribute("Id", idImagen);
      element.setAttribute("Target", "media/" + fileName);
      element.setAttribute("Type", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image");
      parent.appendChild(element);

      InputStream inImage = repo.getDocumentData(new Key(documentKey.getSpace(), idImagen));
      OutputStream outImage = new FileOutputStream(this.dir + this.pathSourcedoc + File.separator + "media" + File.separator + fileName);
      lUtils.copyStream(inImage, outImage);
      outImage.flush();
      outImage.close();
      inImage.close();
    }

    flush(doc, uri);
  }

  private void flush(Document document, String uri) throws Exception {
    if (document == null)
      return;
    FileOutputStream oOutput = new FileOutputStream(uri);
    Transformer xformer = TransformerFactory.newInstance().newTransformer();
    xformer.transform(new DOMSource(document), new StreamResult(oOutput));
    oOutput.close();
  }
}
