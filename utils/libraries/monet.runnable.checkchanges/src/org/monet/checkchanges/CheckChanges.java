package org.monet.checkchanges;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.monet.checkchanges.model.Configuration;
import org.monet.checkchanges.model.Configuration.TargetDatabase;
import org.monet.checkchanges.model.Configuration.TargetFile;
import org.monet.checkchanges.model.Configuration.TargetUserdata;
import org.monet.encrypt.Hasher;
import org.monet.filesystem.StreamHelper;
import org.monet.filesystem.files.LibraryFile;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class CheckChanges {

  private static  String CHECK_CHANGES      =  "/install/config/checkchanges.xml";
  private static  String WEB_XML      =  "/web.xml";
  private static  String WEB_PATH     = "/WebContent/WEB-INF";
  private static  String USER_DATA_PATH  = "/user_data";
  


  public static void main(String[] args) {
    FileInputStream hashInputStream = null;
    FileOutputStream outHashProperties = null;
    try {
      String path = CheckChanges.class.getProtectionDomain().getCodeSource().getLocation().getPath();
      String decodedPath;
      decodedPath = URLDecoder.decode(path, "UTF-8");
      String basePath = decodedPath.substring(0, decodedPath.indexOf("/install/"));
      WEB_XML = basePath + WEB_PATH + WEB_XML;
      CHECK_CHANGES =  basePath + CHECK_CHANGES;
      USER_DATA_PATH = LibraryFile.getDirname(basePath) + USER_DATA_PATH;

      File checkChangesFile = new File(CHECK_CHANGES);
      if(!checkChangesFile.exists()){
        System.out.println("Skiping check changes!!");
        return;
      }


      String userFoldername = getUserFolderName();
      String hashsPath =  System.getProperty("user.home") + File.separator + "." + userFoldername + "/.install/hashs.xml";

      File installFolder = new File(System.getProperty("user.home") + File.separator + "." + userFoldername + "/.install");
      if(!installFolder.exists())
        installFolder.mkdir();
      
      Properties hashsProperties = new Properties();
      File hashFile = new File(hashsPath);
      if(hashFile.exists()){
        hashInputStream = new FileInputStream(hashFile);
        hashsProperties.loadFromXML(hashInputStream);
      }

      Serializer serializer = new Persister();
      Configuration config = serializer.read(Configuration.class, checkChangesFile);

      List<TargetFile> files = config.getTargetFiles();
      if(files != null){
        for (TargetFile targetFile : files) {
          if(config.needCheckAll() || targetFile.needCheck()){
            checkHash(hashsProperties, basePath + targetFile.getRelativePath());  
          }
        }
      }
      
      TargetDatabase targetDatabase = config.getTargeDatabaset();
      if(targetDatabase != null && (config.needCheckAll() || targetDatabase.needCheck())){
        List<File> filesDatabase = LibraryFile.getAllFilesinDirectory(basePath + targetDatabase.getRelativePath(), true);
        List<File> checkQueries = new ArrayList<File>();
        boolean flagQueries = false;
        for (File file : filesDatabase) {
          boolean flag = checkHash(hashsProperties, file.getAbsolutePath());
          if(file.getName().contains("queries")){
            checkQueries.add(file);
            flagQueries |= flag;
          }
        }
        if(flagQueries) checkQueriesFiles(checkQueries);
      }
      
      TargetUserdata targetUserdata = config.getTargetUserData();
      if(targetUserdata != null && (config.needCheckAll() || targetUserdata.needCheck())){
        List<File> filesDatabase = LibraryFile.getAllFilesinDirectory(USER_DATA_PATH, true);
        for (File file : filesDatabase) {
          checkHash(hashsProperties, file.getAbsolutePath());
        }
      }
      
      outHashProperties = new FileOutputStream(hashFile);
      hashsProperties.storeToXML(outHashProperties, null);
      
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      StreamHelper.close(hashInputStream);
      StreamHelper.close(outHashProperties);
    }


  }
  
  private static void checkQueriesFiles(List<File> checkQueries) throws Exception {
    
    Map<String,Properties> propertiesFiles = new HashMap<String,Properties>();
    for (File file : checkQueries) {
      FileInputStream input = new FileInputStream(file);
      Properties tmp = new Properties();
      if(LibraryFile.getExtension(file.getName()).equals("xml"))
        tmp.loadFromXML(input);
      else tmp.load(input);
      propertiesFiles.put(file.getName(),tmp);
    }
    
    if(propertiesFiles.size() < 1) return;
    
    
    for (String iFilename : propertiesFiles.keySet()) {
      Properties iQueries = propertiesFiles.get(iFilename);
      
      for (Object iQuery : iQueries.keySet()) {
        String iQueryValue = iQueries.getProperty((String)iQuery);
        String iQueryFilename = iFilename + "." + iQuery;
        
        for (String jFilename : propertiesFiles.keySet()) {
          if(jFilename.equals(iFilename)) continue;
          
          Properties jQueries = propertiesFiles.get(jFilename);
          String jQueryValue = jQueries.getProperty((String)iQuery);
          String jQueryFilename = jFilename + "." + iQuery;
          if(jQueryValue == null)
            System.out.println("WARNING: Query " + iQuery + " not exist in "+ jFilename +" !!!!");
          else{
            checkQueryParams(iQueryFilename,jQueryFilename,iQueryValue, jQueryValue);
            jQueries.remove(iQuery);
          }
        }
      }
    }
  }

 

  private static void checkQueryParams(String iQuery, String jQuery, String iQueryValue, String jQueryValue) {
    Pattern paramPattern = Pattern.compile("@([^\\s|,|\\)])*");
    Matcher iMatcher = paramPattern.matcher(iQuery);
    Matcher jMatcher = paramPattern.matcher(jQuery);
    List<String> iParams = new ArrayList<String>();
    List<String> jParams = new ArrayList<String>();
    while(iMatcher.find()) iParams.add(iMatcher.group(0));
    while(jMatcher.find()) jParams.add(jMatcher.group(0));
       
    for (String param : iParams) {
      if(!jParams.remove(param)) {
        System.out.println(String.format("WARNING: Param %s found in %s but not found in %s",param,iQuery,jQuery));
      }
    }
    
    for (String param : jParams) {
      System.out.println(String.format("WARNING: Param %s found in %s but not found in %s",param,jQuery,iQuery));
    }
    
  }

  private static boolean checkHash(Properties hashsProperties, String filePath) throws Exception{
    String hash = getFileHash(filePath);
    String filename = LibraryFile.getFilename(filePath);
    boolean flag = false;
    
    
    String oldHash = (String) hashsProperties.get(filename);
    if(oldHash == null){
      System.out.println("WARNING: " + filename + " is new !!!!");
      flag = true;
    }else if(!oldHash.equals(hash)){
      System.out.println("WARNING: " + filename + " changed !!!!");
      flag = true;
    }
    hashsProperties.put(filename, hash);
    return flag;
  }

  private static String getFileHash(String path) throws Exception {
    FileInputStream fileInputStream = null;

    fileInputStream = new FileInputStream(new File(path));
    String fileContent = StreamHelper.toString(fileInputStream);

    return Hasher.getMD5asHexadecimal(fileContent);
  }

  private static String getUserFolderName() throws Exception{
    File webxmlFile = new File(WEB_XML);
    FileInputStream webXMLInputStream = null;

    try{
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder;
      XPath xpath = XPathFactory.newInstance().newXPath();
      docBuilder = docFactory.newDocumentBuilder();
      webXMLInputStream = new FileInputStream(webxmlFile);
      Document doc = docBuilder.parse(new InputSource(webXMLInputStream));
      Node node = (Node) xpath.evaluate("/web-app/display-name", doc, XPathConstants.NODE);
      if (node == null) return null;
      return node.getTextContent();
    }
    finally{
      StreamHelper.close(webXMLInputStream);
    }
  }

}
