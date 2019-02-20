package org.monet.modelling.compiler;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Configuration {
  
  private static final String CONFIG_FILE = "./config/configuration.properties";
  private static Configuration instance;
  private static Properties properties;
  
  private Configuration() {
    properties = new Properties();
    properties = loadProperties();
  }
  
  public static Configuration getInstance() {
    if(instance == null) {
      instance = new Configuration();
    }
    
    return instance;
  }
 
  private Properties loadProperties() {
    String configFile = CONFIG_FILE;
          
    Properties properties = new Properties();
    try {
        FileInputStream is = new FileInputStream(configFile);
        properties.load(is);
    } catch (IOException exception) {       
        
    }
    return properties;
  }
  
  public String getValue(String sName) {
    if (instance == null)
      getInstance();
    if (!properties.containsKey(sName)) 
      return "";
    
    return properties.getProperty(sName).trim();
  }
  
  public int getIntValue(String sName){
    if (instance == null)
      getInstance();
    if (!properties.containsKey(sName)) 
      return (Integer) null;
    
    return Integer.parseInt(properties.getProperty(sName).trim());
  }
  
  

}
