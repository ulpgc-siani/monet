package org.monet.grided.core.configuration;

public interface Configuration {
    public static final String CONFIGFILE              = "grided.config";
    public static final String FEDERATION_SERVICE      = "FEDERATION_SERVICE";
    public static final String FEDERATION_SERVICE_PORT = "FEDERATION_SERVICE_PORT";
    public static final String FEDERATION_SERVICE_SOCKET_PORT = "FEDERATION_SERVICE_SOCKET_PORT";
    public static final String API_KEY = "API_KEY";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String LANGUAGE_FOLDER = "LANGUAGE_FOLDER";
    public static final String DATABASE_TYPE = "DATABASE_TYPE";
    public static final String DATABASE_DATASOURCE = "DATABASE_DATASOURCE";
    public static final String DATA_STORE_PATH = "DATA_STORE_PATH";
    public static final String TEMP_PATH = "TEMP_PATH";
    public static final String CERTIFICATE_FILENAME = "CERTIFICATE_FILENAME";
    public static final String CERTIFICATE_PASSWORD = "CERTIFICATE_PASSWORD";
    	
	public String getImagesPath();
	
	public String getProperty(String key);
	public String getLabel(String key);    
}

