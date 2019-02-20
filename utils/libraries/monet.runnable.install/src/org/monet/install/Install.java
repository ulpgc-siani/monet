package org.monet.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.monet.filesystem.StreamHelper;
import org.monet.filesystem.files.LibraryFile;
import org.monet.install.model.Configuration;
import org.monet.install.model.Configuration.Classpath;
import org.monet.install.model.Configuration.ContextXML;
import org.monet.install.model.Configuration.Database;
import org.monet.install.model.Configuration.Generic;
import org.monet.install.model.Configuration.Option;
import org.monet.install.model.Configuration.WebXML;
import org.monet.install.model.Context;
import org.monet.install.model.Context.Resource;
import org.monet.install.model.Context.ResourceLink;
import org.monet.install.model.InsertScript;
import org.monet.install.model.InsertScript.DefinitionInsert;
import org.monet.install.model.InsertScript.Insert;
import org.monet.install.model.InsertScript.Param;
import org.monet.sql.ConnectionHelper;
import org.monet.sql.NamedParameterStatement;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class Install {

  //  private static final String TEMP_PATH = "C:\\Users\\fsantana\\Monet_workspace\\Monet\\labs\\monet.libraries\\monet.library.install\\test-resources\\";
  //  private static final String INSTALL_XML = TEMP_PATH + "install.xml";
  //  private static final String WEB_XML = TEMP_PATH + "web.dist.xml";


  private static final String INSTALL_XML  = "config/install.xml";
  private static  String CHECKCHANGES_DIST_XML  = "/install/config/checkchanges.dist.xml";
  private static  String CHECKCHANGES_XML  = "/install/config/checkchanges.xml";
  private static  String CLASSPATH_DIST = "/.classpath.dist";
  private static  String CLASSPATH_XML = "/.classpath";
  private static  String DATABASE_SCRIPT_FOLDER = "/resources/";
  private static  String WEB_PATH     = "/WebContent/WEB-INF";
  private static  String WEB_XML_DIST =  "/web.dist.xml";
  private static  String WEB_XML      =  "/web.xml";
  private static  String CONTEXT_XML  = "/WebContent/META-INF/context.xml";
  private static  String USER_DATA_PATH  = "/user_data";
  private static  String INSTALL_PATH  = "/install";


  public static void main(String[] args) throws IOException {
    try{
      String path = Install.class.getProtectionDomain().getCodeSource().getLocation().getPath();
      String decodedPath = URLDecoder.decode(path, "UTF-8");
      String basePath = decodedPath.substring(0, decodedPath.indexOf("/install/"));

      CONTEXT_XML = basePath + CONTEXT_XML;
      WEB_XML = basePath + WEB_PATH + WEB_XML;
      WEB_XML_DIST = basePath + WEB_PATH + WEB_XML_DIST;
      USER_DATA_PATH = LibraryFile.getDirname(basePath) + USER_DATA_PATH;
      DATABASE_SCRIPT_FOLDER = basePath + DATABASE_SCRIPT_FOLDER;
      INSTALL_PATH = basePath + INSTALL_PATH;
      CLASSPATH_DIST = basePath + CLASSPATH_DIST;
      CLASSPATH_XML = basePath + CLASSPATH_XML;
      CHECKCHANGES_DIST_XML = basePath + CHECKCHANGES_DIST_XML;
      CHECKCHANGES_XML = basePath + CHECKCHANGES_XML;


      File configurationFile = new File(INSTALL_XML);
      System.out.println("Install file: " + configurationFile.getAbsolutePath());
      if(configurationFile.exists()) {
        System.out.println("Installing...");

        File checkConfig = new File(CHECKCHANGES_XML);
        File checkConfigDist = new File(CHECKCHANGES_DIST_XML);
        if(!checkConfig.exists() && checkConfigDist.exists())
          LibraryFile.copy(checkConfigDist, checkConfig);

        Serializer serializer = new Persister();
        Configuration config = serializer.read(Configuration.class, configurationFile);

        String appConfigFolderName = createWebXML(config);
        if(config.getWebxml() != null) config.getWebxml().setBuild(false);
        createContextXML(serializer, config);
        if(config.getContextxml() != null) config.getContextxml().setBuild(false);
        createClasspath(config);
        if(config.getClasspath() != null) config.getClasspath().setBuild(false);

        if(appConfigFolderName != null){
          appConfigFolderName = System.getProperty("user.home") + File.separator + "." + appConfigFolderName;

          File appFolder = new File(appConfigFolderName);
          File appInstallFolder = new File(appConfigFolderName + File.separator + ".install");
          if(!appFolder.exists()){
            appFolder.mkdir();
            appInstallFolder.mkdir();
          }else{
            if(!appInstallFolder.exists())
              appInstallFolder.mkdir();
          }

          if(config.getGeneric() != null){
            for (Generic genericConfig : config.getGeneric()) {
              createGeneric(config.needBuildAll(), appConfigFolderName, genericConfig);
              genericConfig.setBuild(false);
            }
          }
          copyOthresUserDataFiles(config, appFolder);
          if(config.getCopyOthers() != null) config.getCopyOthers().setBuild(false);
          createDDBB(config);
          if(config.getDatabase() != null) config.getDatabase().setBuild(false);
          config.setBuildAll(false);
          serializer.write(config, configurationFile);
        }
        else{
          System.err.println("File web xml not found or bad formmated");
          return;
        }

        System.out.println("Installing finished successfully!!!");
      }
      else{
        System.out.println("Skiping Install");
      }
    }catch (Exception e) {
      System.err.println("FATAL ERROR " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static String createWebXML(Configuration config) throws Exception {
    WebXML webxml = config.getWebxml();
    File webxmlFile = new File(WEB_XML);

    if(webxml != null && (config.needBuildAll() || webxml.needBuild())){
      System.out.println("Creating web xml...");
      if(webxmlFile.exists()) webxmlFile.delete(); 
      LibraryFile.copy(new File(WEB_XML_DIST), webxmlFile);
      LibraryFile.replaceTextInFile(webxmlFile, "#.*#", webxml.getDisplayName());
      System.out.println("Created web xml successfully!!!");
      return webxml.getDisplayName();
    }

    if(!webxmlFile.exists()) return null;
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


  private static void createClasspath(Configuration config) throws Exception {
    Classpath classpath = config.getClasspath();
    File classpathFile = new File(CLASSPATH_XML);
    if(classpath != null && (config.needBuildAll() || classpath.needBuild())){
      System.out.println("Creating classpath...");
      if(classpathFile.exists()) classpathFile.delete(); 
      File classpathDistFile = new File(CLASSPATH_DIST);
      if(classpathDistFile.exists()){
        LibraryFile.copy(classpathDistFile, classpathFile);
        System.out.println("Created classpath successfully!!!");
      }else{
        System.err.println("WARNING: File classpath.dist not exist!!");
      }
    }
  }

  private static void createContextXML(Serializer serializer, Configuration config) throws Exception {
    ContextXML contestxml = config.getContextxml();
    if(contestxml != null && (config.needBuildAll() || contestxml.needBuild())){
      System.out.println("Creating Context...");
      File contextxmlFile = new File(CONTEXT_XML);
      if(contextxmlFile.exists()) contextxmlFile.delete(); 

      Context context = new Context();
      String global = (String) contestxml.getOption(Context.GLOBAL);
      String name = (String) contestxml.getOption(Context.NAME);
      if(contestxml.isAsResourceLink()){
        ResourceLink resourceLink = new ResourceLink(global,name);
        context.setResourceLink(resourceLink);
      }
      else{
        Resource resource = new Resource();
        resource.fill(contestxml.getOptionsMap());
        context.setResource(resource);
      }

      serializer.write(context, new OutputStreamWriter(new FileOutputStream(contextxmlFile), "UTF-8"));
      System.out.println("Created context successfully!!!");
    }
  }

  private static void createGeneric(Boolean needBuildAll, String appConfigFolderName, Generic generic) throws Exception {
    if(needBuildAll || generic.needBuild()){
      System.out.println("Creating "+ generic.getFileName().replace(".dist.", ".") +"...");
      FileInputStream tmpInputFile = null;
      File genericDistFile = new File(USER_DATA_PATH + File.separator+ generic.getFileName());
      File genericTmpFile = null;
      File genericFile = null;

      if(generic.needMoveToUserData()){
        genericTmpFile = new File(appConfigFolderName + File.separator + generic.getFileName().replace(".dist.", ".tmp."));
        genericFile = new File(appConfigFolderName + File.separator + generic.getFileName().replace(".dist.", "."));
      } else {
        genericTmpFile = new File(generic.getFileName().replace(".dist.", ".tmp."));
        genericFile = new File(generic.getFileName().replace(".dist.", "."));
      }

      try{
        if(genericFile.exists()) genericFile.delete();

        LibraryFile.copy(genericDistFile, genericTmpFile);

        if(generic.isPropertiesFile()){
          Properties properties = new Properties();
          tmpInputFile = new FileInputStream(genericTmpFile);
          properties.loadFromXML(tmpInputFile);
          for (Option option : generic.getOptions()) {
            properties.put(option.getName(),option.getValue());
          }
          properties.storeToXML(new FileOutputStream(genericFile), null);
        }
        else{
          LibraryFile.copy(genericDistFile, genericFile);
          Map<String,String> replaceOptions = new HashMap<String,String>();
          for (Option option : generic.getOptions()) {
            replaceOptions.put("#"+option.getName()+"#", option.getValue());
          }
          LibraryFile.replaceTextInFile(genericFile, replaceOptions);
        }
        System.out.println("Created " + generic.getFileName().replace(".dist.", ".") +" successfully!!!");
      }
      catch (Exception e) {
        throw e;
      }
      finally{
        StreamHelper.close(tmpInputFile);
        if(genericTmpFile.exists()) genericTmpFile.delete();
      }
    }
  }




  private static void copyOthresUserDataFiles(Configuration config, File appFolder) throws Exception {
    if(config.getCopyOthers() != null && (config.needBuildAll() || config.getCopyOthers().needBuild())){
      List<File> files = LibraryFile.getAllFilesinDirectory(USER_DATA_PATH, true);

      for (File file : files) {
        if(!file.getName().contains(".dist.")){
          String relativePathtmp = file.getAbsolutePath().substring(USER_DATA_PATH.length() - 1);
          File destinationFile = null;
          if(!relativePathtmp.startsWith(File.separator + file.getName())){
            String relativePath = LibraryFile.getDirname(relativePathtmp);
            if(relativePath.lastIndexOf(File.separator) == relativePath.length() - 1)
              relativePath = relativePath.substring(0, relativePath.length() - 1);
            File absoluteFolder = new File(appFolder.getAbsolutePath() + relativePath);
            if(!absoluteFolder.exists()) absoluteFolder.mkdirs();
            destinationFile = new File(absoluteFolder + File.separator + file.getName());
          }
          else{
            destinationFile = new File(appFolder.getAbsoluteFile() + File.separator +file.getName());
          }

          if(destinationFile.exists()) destinationFile.delete();
          LibraryFile.copy(file, destinationFile);
        }
      }
    }
  }






  private static void createDDBB(Configuration config) throws Exception {

    Database database = config.getDatabase();
    if(database != null && (config.needBuildAll() || database.needBuild())){
      String dabaseScriptPath =  DATABASE_SCRIPT_FOLDER + database.getFolder() + File.separator;
      StringBuilder  cleanScript = new StringBuilder(dabaseScriptPath + "%s.clean.sql");
      StringBuilder  createScript = new StringBuilder(dabaseScriptPath + "%s.sql");
      StringBuilder  sQueriesFile = new StringBuilder(dabaseScriptPath + "%s.queries.sql");
      StringBuilder  sQueriesFileXML = new StringBuilder(dabaseScriptPath + "%s.queries.xml");

      Properties queries = new Properties();
      FileInputStream queriesInputStream = null;
      if(((String)database.getOption(Database.TYPE)).equalsIgnoreCase("mysql"))
        configureDatabasePath("mysql", "com.mysql.jdbc.Driver",cleanScript,createScript,sQueriesFile,sQueriesFileXML,queriesInputStream,queries);
      else
        configureDatabasePath("oracle", "oracle.jdbc.OracleDrive",cleanScript,createScript,sQueriesFile,sQueriesFileXML,queriesInputStream,queries);

      Connection connection = null;
      try{
        connection = DriverManager.getConnection((String)database.getOption(Database.URL),
            (String)database.getOption(Database.USER),
            (String)database.getOption(Database.PASSWORD));

        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(null);
        scriptRunner.setDelimiter("$$");
        runScript(scriptRunner, cleanScript.toString());
        System.out.println("Database cleaned successfully!!!");
        scriptRunner.setDelimiter(";");
        runScript(scriptRunner, createScript.toString());
        System.out.println("Database created successfully!!!");

        if(database.getInsertScripts() != null){
          for (org.monet.install.model.Configuration.InsertScript script : database.getInsertScripts()) {
            insert(connection, script.getFile(), queries);  
          }
        }
      }
      finally{
        ConnectionHelper.close(connection);
        StreamHelper.close(queriesInputStream);
      }
    }
  }

  private static void runScript(ScriptRunner scriptRunner, String script) throws Exception{
    InputStreamReader scriptReader = null;
    try{
      scriptReader = new InputStreamReader(new FileInputStream(script));
      scriptRunner.runScript(scriptReader);
    }
    catch (Exception e) {
      throw e;
    }
    finally{
      if(scriptReader != null) scriptReader.close();
    }
  }

  private static void configureDatabasePath(String type, String driverClass, StringBuilder cleanScript, StringBuilder createScript,
      StringBuilder sQueriesFile, StringBuilder sQueriesFileXML, InputStream queriesInputStream, Properties queries) throws Exception{
    Class.forName(driverClass);

    String cleanTmp = String.format(cleanScript.toString(), type);
    String createTmp = String.format(createScript.toString(), type);

    cleanScript.delete(0, cleanScript.length());
    createScript.delete(0, createScript.length());

    cleanScript.append(cleanTmp);
    createScript.append(createTmp);

    File queriesFile = new File(String.format(sQueriesFile.toString(), type));
    File queriesFileXML = new File(String.format(sQueriesFileXML.toString(), type));
    if(queriesFile.exists()){
      queriesInputStream = new FileInputStream(queriesFile);
      queries.load(queriesInputStream);
    }else{
      queriesInputStream = new FileInputStream(queriesFileXML);
      queries.loadFromXML(queriesInputStream);
    }
  }



  public static boolean insert(Connection connection,  String scriptname, Properties queries) throws Exception{

    try{
      Serializer serializer = new Persister();
      InsertScript script = serializer.read(InsertScript.class, new File(INSTALL_PATH + scriptname));

      List<Insert> inserts = script.getInserts();
      for (Insert insert : inserts) {
        DefinitionInsert definition = script.getDefinitionInsertMap().get(insert.getExtends());
        Map<String,Object> mapParam = definition.getParmsMap();
        List<Param> params = insert.getParams();
        for (int i = 0; i < params.size(); i++) {
          mapParam.put(params.get(i).getName(), params.get(i).getValue());
        }
        executeInsert(connection, queries.getProperty(definition.getQuery()), mapParam);
      }

    }catch (Exception e) {
      throw e;
    }
    return true;
  }

  private static void executeInsert(Connection connection, String query, Map<String, Object> mapParam) throws Exception {
    NamedParameterStatement statement = null;
    try{
      statement = new NamedParameterStatement(connection, query);
      for (String nameParam : mapParam.keySet()) 
        statement.setObject(nameParam, mapParam.get(nameParam));
          statement.executeUpdate();
    }
    finally{
      ConnectionHelper.close(statement);
    }
  }


}
