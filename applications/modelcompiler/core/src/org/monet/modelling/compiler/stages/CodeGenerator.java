package org.monet.modelling.compiler.stages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.monet.modelling.agents.AgentFilesystem;
import org.monet.modelling.compiler.utils.ResourcesManager;
import org.monet.modelling.constants.Strings;
import org.monet.modelling.libraries.LibraryString;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class CodeGenerator {

  private final String    parentAttrXPath = "/model/@parent";
  private XPathExpression parentAttrXP;

  public CodeGenerator() {
    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();
    try {
      parentAttrXP = xpath.compile(parentAttrXPath);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
  }

  public void execute(String path) {
    ResourcesManager manager = new ResourcesManager();
    Collection<File> parentFiles = new ArrayList<File>();
    Collection<File> childFiles = new ArrayList<File>();
    Collection<File> totalFiles = new ArrayList<File>();
    String projectParentPath;
    try {
      
      projectParentPath = getParent(path);
      if (projectParentPath != null) {
        projectParentPath = LibraryString.replaceAll(projectParentPath, Strings.BAR135, Strings.BAR45);
        path = LibraryString.replaceAll(path, Strings.BAR135, Strings.BAR45);
        parentFiles = manager.getFiles(projectParentPath);
      }
      
      childFiles = manager.getFiles(path);
      totalFiles.addAll(childFiles);
      totalFiles.addAll(parentFiles);
      
      if (totalFiles.isEmpty()) {
        System.out.println("Not found files in path: " + path);
        return;
      }

      Iterator<File> fileIterator = totalFiles.iterator();
      
      while (fileIterator.hasNext()) {
        File currentFile = fileIterator.next();
        if (currentFile.getName().endsWith("xml")) {
          FileInputStream inputStream = new FileInputStream(currentFile);
          BufferedReader data = new BufferedReader(new InputStreamReader(inputStream));
          String result = "";
          String line = "";
          String pattern = "code=\"\"";
          while (null != ((line = data.readLine()))) {
            int patternPosition = 0;
            int y = 0;
            while ((patternPosition = line.indexOf(pattern, y)) > -1) {
              String code = getCode();
              String substitute = "code=\"" + code + "\"";
              result += line.substring(y, patternPosition);
              result += substitute;
              y = patternPosition + pattern.length();
            }
            result += line.substring(y) + "\n";
          }
          
          data.close();
          inputStream.close();
          
          AgentFilesystem.writeFile(currentFile.getAbsolutePath(), result);
        }
      }
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  private static String getCode() {
    UUID autoCode = UUID.randomUUID();
    return autoCode.toString().replaceAll("-", "");
  }

  private String getParent(String projectChildPath) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    String path = projectChildPath + "/model.xml";
    String parentPath = new File(projectChildPath).getParent();
    builder = factory.newDocumentBuilder();
    Document document = builder.parse(path);
    Node parentNode = (Node) parentAttrXP.evaluate(document, XPathConstants.NODE);
    if (parentNode != null)
      return parentPath + "/" + parentNode.getNodeValue();
    else return null;
  }

  
}
