package org.monet.modelling.compiler.stages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.monet.modelling.agents.AgentFilesystem;
import org.monet.modelling.compiler.Configuration;
import org.monet.modelling.libraries.LibraryFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ProcessTheme {

  private static final String THEME_PATH = "THEME_PATH";
  private static final String PNG_EXTENSION = "png";
  private static final String LOGO_LOADING_MAX_HEIGHT = "LOGO_LOADING_MAX_HEIGHT";
  private static final String LOGO_TOP_MAX_HEIGHT = "LOGO_TOP_MAX_HEIGHT";
  public final String ICON_NAME         = "ICON_NAME";
  public final String LOGO_LOADING_NAME = "LOGO_LOADING_NAME";
  public final String LOGO_TOP_NAME     = "LOGO_TOP_NAME";  
  public final String IMAGES_PATH       = "IMAGES_PATH";
  
  private final String    iconPropertyXPath = "/model/view/icon";
  private final String    logoTopPropertyXPath = "/model/view/logo[@type ='top']";
  private final String    logoLoadingPropertyXPath = "/model/view/logo[@type ='loading']";
  
  private XPathExpression iconPropertyXp;
  private XPathExpression logoTopPropertyXp;
  private XPathExpression logoLoadingPropertyXp;
  
  private Configuration configuration;
  
  public ProcessTheme() {
    configuration = Configuration.getInstance();
    
    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();
    try {
      iconPropertyXp = xpath.compile(iconPropertyXPath);
      logoTopPropertyXp = xpath.compile(logoTopPropertyXPath);
      logoLoadingPropertyXp = xpath.compile(logoLoadingPropertyXPath);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
  }
  
  public void execute(String path) {
    try {
      System.out.println("Processing images");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder;
      String modelPath = path + "/model.xml";
      
      builder = factory.newDocumentBuilder();
      Document document = builder.parse(modelPath);
      
      processIcon(document, path);
      processLogoLoading(document, path);
      processLogoTop(document, path);
      System.out.println("Images processed");
    } catch (Exception e) {
      System.out.println("Error. Message: " + e.getMessage());
    }
  }
  
  private void processIcon(Document document, String path) throws Exception {   
    Node iconNode = (Node) iconPropertyXp.evaluate(document, XPathConstants.NODE);
    String targetPath =  path + configuration.getValue(IMAGES_PATH) + "/";
    AgentFilesystem.createDir(targetPath);
    
    if (iconNode != null) {
      Node attrFilename = iconNode.getAttributes().getNamedItem("filename");
      if (attrFilename == null) { 
        System.out.println("Error. Attribute filename can't be null");
        return;
      }
      
      String filename = attrFilename.getNodeValue();
      String sourceImagePath = path + "/images/" + filename;
      if (!AgentFilesystem.existFile(sourceImagePath)) { 
        System.out.println("The image: " + filename + " don't exists.");
        return;
      }
                 
      String targetFilename = targetPath + configuration.getValue(ICON_NAME);      
      AgentFilesystem.copyFile(sourceImagePath, targetFilename);
    
    } else {
      String templatesPath = configuration.getValue(THEME_PATH);
      String sourceImagePath = templatesPath + "/" + configuration.getValue(ICON_NAME);
      
      if (!AgentFilesystem.existFile(sourceImagePath)){
        System.out.println("The image: " + sourceImagePath + " don't exists.");
        return;
      }
      String targetFilename = targetPath + configuration.getValue(ICON_NAME); 
      AgentFilesystem.copyFile(sourceImagePath, targetFilename);
      
    }
  }
  
  private void processLogoLoading(Document document, String path) throws Exception {     
    Node logoLoadingNode = (Node) logoLoadingPropertyXp.evaluate(document, XPathConstants.NODE);
    String targetPath =  path + configuration.getValue(IMAGES_PATH) + "/";
    
    AgentFilesystem.createDir(targetPath);
    
    if (logoLoadingNode != null) {
      Node attrFilename = logoLoadingNode.getAttributes().getNamedItem("filename");
      if (attrFilename == null) { 
        System.out.println("Error. Attribute filename can't be null");
        return;
      }
      
      String filename = attrFilename.getNodeValue();
      String sourceImagePath = path + "/images/" + filename;
      if (!AgentFilesystem.existFile(sourceImagePath)) { 
        System.out.println("The image: " + filename + " don't exists.");
        return;
      }
           
      String fileExtension = LibraryFile.getExtension(sourceImagePath);
      if(fileExtension != PNG_EXTENSION) {
        String targetFileConverted = path + "/images/" + configuration.getValue(LOGO_LOADING_NAME);
        convertImage(sourceImagePath, targetFileConverted, PNG_EXTENSION);
        sourceImagePath = targetFileConverted;
      }
      
      BufferedImage image = ImageIO.read(new File(sourceImagePath));
      int maxHeight = configuration.getIntValue(LOGO_LOADING_MAX_HEIGHT);
       
      String targetFilename = targetPath + configuration.getValue(LOGO_LOADING_NAME);      
      if(image.getWidth() > maxHeight) {
          scaleImage(sourceImagePath, targetFilename, PNG_EXTENSION, maxHeight);
      } else {
        AgentFilesystem.copyFile(sourceImagePath, targetFilename);      
      }     
      
    } else {
      String templatesPath = configuration.getValue(THEME_PATH);
      String iconFilename = templatesPath + "/" + configuration.getValue(LOGO_LOADING_NAME);
      
      if (!AgentFilesystem.existFile(iconFilename)){
        System.out.println("The image: " + iconFilename + " don't exists.");
        return;
      }
      String targetFilename = targetPath + configuration.getValue(LOGO_LOADING_NAME); 
      AgentFilesystem.copyFile(iconFilename, targetFilename);
    }
  }

  private void processLogoTop(Document document, String path) throws Exception { 
    Node logoTopNode = (Node) logoTopPropertyXp.evaluate(document, XPathConstants.NODE);
    String targetPath =  path + configuration.getValue(IMAGES_PATH) + "/";
    
    if (logoTopNode != null) {
      Node attrFilename = logoTopNode.getAttributes().getNamedItem("filename");
      if (attrFilename == null) { 
        System.out.println("Error. Attribute filename can't be null");
        return;
      }
      
      String filename = attrFilename.getNodeValue();
      String sourceImagePath = path + "/images/" + filename;
      if (!AgentFilesystem.existFile(sourceImagePath)) { 
        System.out.println("The image: " + filename + " don't exists.");
        return;
      }
           
      String fileExtension = LibraryFile.getExtension(sourceImagePath);
      if(fileExtension != PNG_EXTENSION){
        String targetFileConverted = path + "/images/" + configuration.getValue(LOGO_TOP_NAME);
        convertImage(sourceImagePath, targetFileConverted, PNG_EXTENSION);
        sourceImagePath = targetFileConverted;
      }
      
      BufferedImage image = ImageIO.read(new File(sourceImagePath));
      int maxHeight = configuration.getIntValue(LOGO_TOP_MAX_HEIGHT);
       
      String targetFilename = targetPath + configuration.getValue(LOGO_TOP_NAME);      
      if(image.getWidth() > maxHeight) {
          scaleImage(sourceImagePath, targetFilename, PNG_EXTENSION, maxHeight);
      } else {
        AgentFilesystem.copyFile(sourceImagePath, targetFilename);      
      }        
     
    } else {
      String templatesPath = configuration.getValue(THEME_PATH);
      String logoTopFilename = templatesPath + "/" + configuration.getValue(LOGO_TOP_NAME);
      
      if (!AgentFilesystem.existFile(logoTopFilename)){
        System.out.println("The image: " + logoTopFilename + " don't exists.");
        return;
      }
      
      String targetFilename = targetPath + configuration.getValue(LOGO_TOP_NAME); 
      AgentFilesystem.copyFile(logoTopFilename, targetFilename);
    }
  }

  private boolean convertImage(String inputFilename, String outputFilename, String formatName) throws Exception {
    BufferedImage image = ImageIO.read(new File(inputFilename));
    File outputImage = new File(outputFilename);
    return ImageIO.write(image, formatName, outputImage);
  }
  
  private boolean scaleImage(String inputFilename, String outputFilename, String formatName, int height) throws Exception {
    BufferedImage image = ImageIO.read(new File(inputFilename));
    
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();
    int maxHeight = height;
    
    imageWidth = (imageWidth * maxHeight) / imageHeight;
    imageHeight = maxHeight;
    
    BufferedImage finalImage = new BufferedImage(imageWidth, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = finalImage.createGraphics();
    
    AffineTransform transform = AffineTransform.getScaleInstance((double) imageWidth / image.getWidth(), (double) imageHeight / image.getHeight());
    
    graphics.drawRenderedImage(image, transform);
    return ImageIO.write(finalImage, formatName, new File(outputFilename));    
  }  

}
