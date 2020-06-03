package org.monet.deployservice_engine.configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {

	private static Configuration instance;
	private static Properties properties;
	private static Logger logger;

	public static final String CONST_AppName = "deployservice";
	public static final String CONST_AppCaption = "Monet Deploy Service";
	public static final String CONST_Version = "0.99.259";
	public static final String CONST_CONFIG_FILE = CONST_AppName + ".config";
	public static final String CONST_FileBiEngineWar = "biengine.war";
	public static final String CONST_FileSaikuWar = "saiku.war";
	public static final String CONST_WarDocServerPrefix = "ds_";
	public static final String CONST_WarBiEnginePrefix = "bi_";
	public static final String CONST_WarSaikuPrefix = "sa_";
	public static final String CONST_FederationDefault = "federation";
	public static final String CONST_MonetVersionDefault = "3.2";
	public static final String CONST_FileEtcFederationDomainOld = "/etc/monet/federation_domain";	
	public static final String CONST_FileEtcFederationPort = "/etc/monet/federation_port"; // Se usa para redirigir puertos en local en las máquinas virtuales de Monet	
	public static final String CONST_FileEtcDeployServicePort = "/etc/monet/deployservice_port";	
	public static final String CONST_FileEtcSSHPort = "/etc/monet/ssh_port";	
		
	public static final String VAR_Debug = "Debug";
	public static final String VAR_ServerUDP = "ServerUDP";	
	public static final String VAR_ServerHTTPS = "ServerHTTPS";	
	public static final String VAR_SendUDPInterface = "SendUDPInterface";
	public static final String VAR_SendUDPInterval = "SendUDPInterval";
	public static final String VAR_Port = "Port";
	public static final String VAR_FileServersXML = "FileServersXML";
	public static final String VAR_FileDocServersXML = "FileDocServersXML";
	public static final String VAR_User ="User";
	public static final String VAR_Password ="Password";
	public static final String VAR_DirTemp = "DirTemp";
	public static final String VAR_HTTPS_KeyStoreFile = "HTTPSKeyStoreFile";
	public static final String VAR_HTTPS_KeyStorePassword = "HTTPSKeyStorePassword";	

	public static final String VAR_DirConfigMonet = "DirConfigMonet";
	public static final String VAR_FileConfigMonetKernel = "FileConfigMonetKernel";
	public static final String VAR_FileConfigMonetBackoffice = "FileConfigMonetBackoffice";
	public static final String VAR_FileConfigMonetBoot = "FileConfigMonetBoot";
	public static final String VAR_FileConfigMonetConsole = "FileConfigMonetConsole";
	public static final String VAR_FileConfigMonetManager = "FileConfigMonetManager";
	public static final String VAR_FileLog4jMonet = "FileLog4jMonet";
	public static final String VAR_FileDbMysqlMonetClean = "FileDbMysqlMonetClean";
	public static final String VAR_FileDbOracleMonetClean = "FileDbOracleMonetClean";
	public static final String VAR_FileDbMysqlMonet = "FileDbMysqlMonet";
	public static final String VAR_FileDbOracleMonet = "FileDbOracleMonet";
	public static final String VAR_FileConfigMonetCache = "FileConfigMonetCache";
	
	public static final String VAR_DirConfigDocServer = "DirConfigDocServer";	
	public static final String VAR_FileConfigDocServer = "FileConfigDocServer";
	public static final String VAR_FileLog4jDocServer = "FileLog4jDocServer";
	public static final String VAR_FileDbMysqlDocServerClean = "FileDbMysqlDocServerClean";
	public static final String VAR_FileDbOracleDocServerClean = "FileDbOracleDocServerClean";
	public static final String VAR_FileDbMysqlDocServer = "FileDbMysqlDocServer";
	public static final String VAR_FileDbOracleDocServer = "FileDbOracleDocServer";
	
	public static final String VAR_DirConfigBiEngine = "DirConfigBiEngine";
	public static final String VAR_FileConfigBiEngine = "FileConfigBiEngine";
	public static final String VAR_FileLog4jBiEngine = "FileLog4jBiEngine";
	public static final String VAR_FileDbMysqlBiEngineClean = "FileDbMysqlBiEngineClean";
	public static final String VAR_FileDbOracleBiEngineClean = "FileDbOracleBiEngineClean";
	public static final String VAR_FileDbMysqlBiEngine = "FileDbMysqlBiEngine";
	public static final String VAR_FileDbOracleBiEngine = "FileDbOracleBiEngine";
	
	public static final String VAR_DeployUrlBiEngine = "DeployUrlBiEngine";
	
	public static final String VAR_UpdateUrl = "UpdateUrl";
	public static final String VAR_UpdateManagerName = "UpdateManagerName";
	public static final String VAR_UpdateManagerPath = "UpdateManagerPath";
	
	public static final String VAR_ResourceMysql = "ResourceMysql";
	public static final String VAR_ResourceOracle = "ResourceOracle";

  public static final String VAR_FileConfigSetup = "FileConfigSetup";
	
  public static final String VAR_FederationServiceGridHost = "FederationServiceGridHost"; 
  public static final String VAR_FederationServiceGridSocketPort = "FederationServiceGridSocketPort";
  public static final String VAR_FederationServiceGridServicePort = "FederationServiceGridServicePort";
  public static final String VAR_FederationServiceGridSecret = "FederationServiceGridSecret";
  public static final String VAR_FederationServiceGridSsl = "FederationServiceGridSsl";
  public static final String VAR_FederationServiceGridPath = "FederationServiceGridPath";

	public static final String VAR_FileMonetWar = "FileMonetWar";
	public static final String VAR_FileDocServiceWar = "FileDocServiceWar";
	
	public static final String VAR_MonetModelType = "MonetModelType";
	public static final String VAR_MonetVersionApp = "MonetVersion";

	public static final String VAR_MonetSpaceURI = "MonetSpaceURI";
	public static final String VAR_MonetSpaceSecret = "MonetSpaceSecret";	

	public static final String VAR_FileCertificateSpace = "FileCertificateSpace";	
	public static final String VAR_FileCertificateSpacePassword = "FileCertificateSpacePassword";	
	
	public static final String VAR_MailAdminSpaceHost = "MailAdminSpaceHost";	
	public static final String VAR_MailAdminSpaceFrom = "MailAdminSpaceFrom";	
	public static final String VAR_MailAdminSpaceTo = "MailAdminSpaceTo";	
	public static final String VAR_MailAdminSpaceUsername = "MailAdminSpaceUsername";	
	public static final String VAR_MailAdminSpacePassword = "MailAdminSpacePassword";	
	public static final String VAR_MailAdminSpacePort = "MailAdminSpacePort";	
	public static final String VAR_MailAdminSpaceSecure = "MailAdminSpaceSecure";
	
	public static final String VAR_UpdateManagerDestName = "UpdateManagerDestName";

	public static final String VAR_StartCommandToBegin = "StartCommandToBegin";
	
	public static final String VAR_FileConfigMonetAnalytics = "FileConfigMonetAnalytics";
	public static final String VAR_AddSocketLog = "AddSocketLog";

  public static final String VAR_FederationDefault = "FederationDefault";
  public static final String VAR_SpaceCleanForceDeleteDB = "SpaceCleanForceDeleteDB"; 
  public static final String VAR_DeployServicePath = "DeployServicePath"; 

  public static final String VAR_DataWareHouseDir = "DataWareHouseDir"; 
  public static final String VAR_DocumentDisksDir = "DocumentDisksDir"; 

	public static final String VAR_FileConfigMonetExplorer = "FileConfigMonetExplorer";

  public static final String VAR_SpacesDir = "SpacesDir"; 
  public static final String VAR_DocumentsDir = "DocumentsDir"; 
	
	private static final String CONST_HomeDir = "." + CONST_AppName;
	private static final String CONST_WarDir = "war";	
	private static final String CONST_CertificatesDir = "certificates";	

	public static final String CONST_DeployServiceDir = "/opt/deployservice";
	
	private Configuration() {
		logger = Logger.getLogger(this.getClass());

		properties = new Properties();
		properties = loadProperties();
	}

	public static String getConfigurationFile() {
		return getHomePath() + "/" + CONST_CONFIG_FILE;
	}
	
	private Properties loadProperties() {
		logger.info("Loading config: " + getConfigurationFile());
		try {
			FileInputStream is = new FileInputStream(getConfigurationFile());
			properties.load(is);
			logger.info("The configuration is loaded successfully.");
		} catch (IOException exception) {
			logger.error("Unable to read configuration.");
		}
		return properties;
	}

	public static String getPath() {
		String path = "";
		try {
			path = new java.io.File(".").getCanonicalPath();
		} catch (Exception exception) {
			logger.error("Unable to read current path.");
		}
		return path;
	}

	public static String getJarPath() {
		return getValue(VAR_DeployServicePath, CONST_DeployServiceDir); 
	}
	
	public static String getJarName() {
	  File file = new File(ManagementFactory.getRuntimeMXBean().getClassPath());
	  return file.getName();
	}
	
	public static String getHomePath() {
		return System.getProperty("user.home") + "/" + CONST_HomeDir;
	}

	public synchronized static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	public static String getValue(String sName) {
		return getValue(sName, "");
	}

	public static String getValue(String sName, String sDefault) {
		if (instance == null)	getInstance();
		if (!properties.containsKey(sName))	return sDefault;
		return properties.getProperty(sName).trim();
	}
	
	public static String getServersConfig() {
		String message = "";
		try {
			try (BufferedReader bf = new BufferedReader(new FileReader(getHomePath() + "/" + getValue(VAR_FileServersXML)))) {

				String Char;
				while ((Char = bf.readLine()) != null) {
					message = message + Char;
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("The message file not found: " + getValue(VAR_FileServersXML));
			message = "";
		} catch (IOException e) {
			logger.error("Error to read message file: " + getValue(VAR_FileServersXML));
			message = "";
		}

		return message;
	}

	public static void setServersConfig(String config) throws IOException {
		try (Writer output = new BufferedWriter(new FileWriter(new File(getHomePath() + "/" + getValue(VAR_FileServersXML))))) {
			output.write(config);
		}
	}
	
	public static String getDocServersConfig() {
		String message = "";
		try {
			try (BufferedReader bf = new BufferedReader(new FileReader(getHomePath() + "/" + getValue(VAR_FileDocServersXML)))) {

				String Char;
				while ((Char = bf.readLine()) != null) {
					message = message + Char;
				}

				bf.close();
			}
		} catch (FileNotFoundException e) {
			logger.error("The message file not found: " + getValue(VAR_FileDocServersXML));
			message = "";
		} catch (IOException e) {
			logger.error("Error to read message file: " + getValue(VAR_FileDocServersXML));
			message = "";
		}

		return message;
	}

	public static String getPathWar() {
		return getHomePath() + "/" + CONST_WarDir; 
	}
	
	public static String getPathCertificates() {
		return getHomePath() + "/" + CONST_CertificatesDir; 
	}
	public static String getTempDir() {
		return getValue(VAR_DirTemp);	
	}
	
	public static Boolean isConfigUpdated() {
		return ! getValue(VAR_DocumentsDir, "null").equals("null");
	}
	
	public static Integer MonetVersionMayor() {
		String version = Configuration.getValue(Configuration.VAR_MonetVersionApp);
		if (version.equals("")) version = CONST_MonetVersionDefault;
		if (! version.contains(".")) return Integer.parseInt(version);		
		return Integer.parseInt(version.split("\\.")[0]);		
	}
	
	public static Integer MonetVersionMinor() {
		String version = Configuration.getValue(Configuration.VAR_MonetVersionApp); 
		if (version.equals("")) version = CONST_MonetVersionDefault;
		if (! version.contains(".")) return 0;		
		return Integer.parseInt(version.split("\\.")[1]);		
	}

	public static void restartApplication() {
    StringBuilder cmd = new StringBuilder();   
    String JavaBin;
    if (isWindows()) 
    	JavaBin = "\"" + System.getProperty("java.home") + "\\bin" + File.separator + "java.exe\"";
    else
    	JavaBin = System.getProperty("java.home") + "/bin" + File.separator + "java";
    	
    cmd.append(JavaBin).append(" ");
    
    for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
        cmd.append(jvmArg).append(" ");
    }    
//    cmd.append("-Dfile.encoding=UTF8 -jar ").append(getJarPath()).append("/").append(getJarName()).append(" ");
    cmd.append("-jar ").append(getJarPath()).append("/").append(getJarName()).append(" ");

    try {
			logger.warn("Try restart application, command: " + cmd.toString());
      Runtime.getRuntime().exec(cmd.toString());
			logger.warn("Shutdown application. PID: "+ ManagementFactory.getRuntimeMXBean().getName());
      System.exit(0);		
    } catch (IOException e) {
			logger.error("I can't restart application. Message: " + e.getMessage());
    }
	}

	public static boolean isWindows()  {
     return System.getProperty("os.name").startsWith("Windows");
  }	
}
