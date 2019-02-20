package org.monet.modelling.compiler.stages;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

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
import org.monet.modelling.libraries.LibraryFile;
import org.monet.modelling.libraries.LibraryString;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ProcessInheritance {

  private final String    parentAttrXPath = "/model/@parent";
  private final String    tempFilePath    = "/tmp/models";

  private XPathExpression parentAttrXP;

  public ProcessInheritance() {
    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();
    try {
      parentAttrXP = xpath.compile(parentAttrXPath);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
  }

  public void execute(String projectPath) {
    AgentFilesystem.forceDir(tempFilePath);
    String projectParentPath;
    try {
      projectParentPath = getParent(projectPath);
      processInheritance(projectPath, projectParentPath);

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private void processInheritance(String projectPath, String projectParentPath) throws Exception {
    ResourcesManager manager = new ResourcesManager();
    projectParentPath = LibraryString.replaceAll(projectParentPath, Strings.BAR135, Strings.BAR45);
    projectPath = LibraryString.replaceAll(projectPath, Strings.BAR135, Strings.BAR45);

    System.out.println("Copying Model");
    if (!projectParentPath.isEmpty() && !AgentFilesystem.existFile(projectParentPath))
      throw new Exception("The parent project not exists");
    
    else {
      HashMap<String, File> parentFiles = new HashMap<String, File>();
      
      if (!projectParentPath.isEmpty())
        parentFiles = manager.getDeclarations(projectParentPath);
      
      HashMap<String, File> childFiles = manager.getDeclarations(projectPath);
      HashMap<String, File> totalFiles = new HashMap<String, File>();
      
      totalFiles.putAll(parentFiles);
      totalFiles.putAll(childFiles);
      Set<String> names = totalFiles.keySet();
      
      for (String filename : names) {
        File file = totalFiles.get(filename);
        String path = LibraryString.replaceAll(file.getAbsolutePath(), Strings.BAR135, Strings.BAR45);
        String copyPath = "";
        
        if (path.indexOf(projectPath) != -1)
          copyPath = LibraryString.replaceAll(path, projectPath, tempFilePath);
        else
          copyPath = LibraryString.replaceAll(path, projectParentPath, tempFilePath);
        
        if (copyPath.contains("definitions"))
          AgentFilesystem.copyFile(file.getAbsolutePath(), tempFilePath + "/definitions/" + file.getName());
        else {
          File directory = new File(LibraryFile.getDirname(copyPath));
          directory.mkdirs();
          AgentFilesystem.copyFile(file.getAbsolutePath(), copyPath);
        }
      }
      System.out.println("Model copied");
    }
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
    else return "";
  }

}
