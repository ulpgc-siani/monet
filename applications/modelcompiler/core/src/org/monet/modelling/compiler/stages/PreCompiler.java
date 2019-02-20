package org.monet.modelling.compiler.stages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.monet.modelling.agents.AgentFilesystem;
import org.monet.modelling.compiler.Configuration;
import org.monet.modelling.compiler.utils.ResourcesManager;
import org.monet.modelling.libraries.LibraryFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PreCompiler {
  
  private final String  preCompilerSentenceXPath = "//include";
  private final String codeAttrXPath = "//*[@code]";
  private static final String TEMP_PATH = "TEMP_PATH";
  private List<String> filesToRemove;
  
  private XPathExpression preCompilerSentenceXp;
  private XPathExpression codeAttrXp;
  private Configuration configuration;
  

  public PreCompiler() {    
    configuration = Configuration.getInstance();
    filesToRemove = new ArrayList<String>(); 
    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();
    try {
      preCompilerSentenceXp = xpath.compile(preCompilerSentenceXPath);
      codeAttrXp = xpath.compile(codeAttrXPath);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
  }
  
  public void execute(String path) {
    System.out.println("Compiling include sentences");
    
    configuration = Configuration.getInstance();
    ResourcesManager manager = new ResourcesManager();
    HashMap<String, File> declarations;

    try {
      String tempPath = configuration.getValue(TEMP_PATH);
      declarations = manager.getDeclarations(tempPath);
      
      Set<String> filenames = declarations.keySet();
      Iterator<String> filenamesIterator = filenames.iterator();
      
      while(filenamesIterator.hasNext()){
        String filename = filenamesIterator.next();
        
        if (!LibraryFile.getExtension(filename).equals("xml")) continue; 
        
        File currentFile = declarations.get(filename);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(currentFile.getAbsolutePath());
                
        NodeList includeSentences = processIncludeSentence(document);
        
        for (int i = 0; i < includeSentences.getLength(); i++) {
          Node includeNode = includeSentences.item(i);
          importFile(document, includeNode);
        }
        
        saveFile(document, currentFile.getAbsolutePath());        
      }
      

    } catch (Exception e){
      System.out.println("Error. " + e.getMessage());
      return;
    } finally {
      removeFiles();
      System.out.println("Compiled");
    }
        
  }

  private void importFile(Document document, Node includeNode) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;
    
    Node parentNode = includeNode.getParentNode();
    Node externalFilenameNode = includeNode.getAttributes().getNamedItem("filename");
   
    if (externalFilenameNode == null) {
      System.out.println("Error. Attribute filename in include declaration must be exist.");
      return;
    }
    
    String externalFilename = externalFilenameNode.getNodeValue();
    String externalFilePath = configuration.getValue(TEMP_PATH) + "/definitions/" + externalFilename;
    
    if (!filesToRemove.contains(externalFilePath))
      filesToRemove.add(externalFilePath);
    
    if(!AgentFilesystem.existFile(externalFilePath)) {
      System.out.println("Error. The file " + externalFilename + " not exists.");
      return;
    }
    
    File externalFile = new File(externalFilePath);
    
    // Deleted includes node.
    parentNode.removeChild(includeNode); 
    
    docBuilder = factory.newDocumentBuilder();
    Document externalDocument = docBuilder.parse(new FileInputStream(externalFile));

    NodeList list = (NodeList) externalDocument.getDocumentElement().getChildNodes();    
    for(int i = 0; i < list.getLength(); i++) {
      Node adoptNode = list.item(i);
      Node newChild = document.importNode(adoptNode, true);
      processCodes(document, newChild);
      parentNode.appendChild(newChild);      
    }    
  }

  private void processCodes(Document document, Node node) throws XPathExpressionException {
    NodeList nodes = (NodeList) codeAttrXp.evaluate(node, XPathConstants.NODESET);
    for (int i = 0; i < nodes.getLength(); i++){
      String code = getCode();
      Node currentNode = nodes.item(i);
      currentNode.getAttributes().getNamedItem("code").setNodeValue(code);
    }
  }

  private NodeList processIncludeSentence(Document document) throws XPathExpressionException {
    NodeList includeSentences = (NodeList) preCompilerSentenceXp.evaluate(document, XPathConstants.NODESET);
    return includeSentences;
  }
  
  private void saveFile(Document document, String filepath) throws Exception {
    //String filePath = configuration.getValue(TEMP_PATH) + "/definitions/" + filename;
    FileOutputStream oOutput = new FileOutputStream(new File(filepath));
    Transformer xformer = TransformerFactory.newInstance().newTransformer();
    xformer.transform(new DOMSource(document), new StreamResult(oOutput));
    oOutput.close(); 
  }
  
  private void removeFiles() {
    for (int i = 0; i < filesToRemove.size(); i++) {
      AgentFilesystem.removeFile(filesToRemove.get(i));
    }
  }
  
  private String getCode() {
    UUID autoCode = UUID.randomUUID();
    return autoCode.toString().replaceAll("-", "");
  }
}
